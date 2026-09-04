package com.artdaily.harvester.nga

import com.artdaily.core.model.Artwork
import com.artdaily.core.normalize.ClassificationNormalizer
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import java.io.File
import java.net.URI
import java.nio.charset.StandardCharsets

/**
 * Cosecha National Gallery of Art (Washington) — distinta a Met/AIC/CMA/Rijks: no es una API
 * REST con búsqueda por término, es un **dataset CSV completo publicado en GitHub**
 * (`github.com/NationalGalleryOfArt/opendata`, actualizado a diario), CC0 total. Investigado
 * en vivo el 2026-09-04 descargando los CSV reales (no de memoria) — ver `docs/bitacora.md`.
 *
 * Detalle importante de licencia, citado del propio `documentation/Data Dictionary.txt` de
 * NGA: "while links to images contained in this data are being released under CC0... the
 * NGA's Open Access Policy applies to only a subset of the images". Por eso el filtro real no
 * es "está en el dataset" sino **`published_images.openaccess == 1`** — de los ~129.000
 * registros de imagen, solo ~69.000 lo cumplen (confirmado contando filas reales).
 *
 * Join necesario (5 tablas, streaming — nunca se cargan las 5 completas en memoria a la vez):
 * 1. `published_images.csv` -> mejor imagen open-access por objectID (preferida "primary").
 * 2. `objects.csv` -> filtra a los que tienen imagen open-access Y `classification` normaliza
 *    a algo de [eligibleClassifications] — evita construir/joinear registros que después se
 *    van a descartar igual.
 * 3. `objects_terms.csv` -> términos "Style"/"School" del subconjunto anterior únicamente.
 * 4. `objects_constituents.csv` -> constituentID del artista principal (roleType="artist",
 *    menor displayOrder) del mismo subconjunto.
 * 5. `constituents.csv` -> año de nacimiento/muerte de esos artistas.
 *
 * [eligibleClassifications] default = solo "painting", **no** "painting"+"print" como el
 * resto del catálogo. Decisión del 2026-09-04 (ver `docs/bitacora.md`): la cosecha completa
 * reveló que NGA cura el campo `Style` casi exclusivamente para pinturas — 43% de las
 * paintings sale con `movement` automático (mejor que el resto del catálogo), pero solo 2%
 * de los prints (27.457 de los 30.505 candidatos, el 90% del volumen) — y periodo/movimiento
 * es un filtro central de la app. Traer los prints sin clasificar hubiera diluido la cobertura
 * global del catálogo de ~29% a ~15%. Queda parametrizado (no hardcodeado a secas) por si en
 * el futuro se quiere sumar los prints que SÍ tienen `movement` real, filtrando después de
 * mapear en vez de acá.
 */
class NgaCsvIngester(
    private val cacheDir: File,
    private val eligibleClassifications: Set<String> = setOf("painting")
) {
    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com/NationalGalleryOfArt/opendata/main/data/"
    }

    fun ingest(): List<Artwork> {
        cacheDir.mkdirs()

        val imagesFile = ensureDownloaded("published_images.csv")
        val bestImageByObjectId = indexOpenAccessImages(imagesFile)
        println("[nga] ${bestImageByObjectId.size} objetos con al menos una imagen open-access (published_images.openaccess=1)")

        data class Partial(
            val objectId: String,
            val title: String?,
            val displayDate: String?,
            val beginYear: Int?,
            val endYear: Int?,
            val medium: String?,
            val attribution: String?,
            val creditLine: String?,
            val classification: String?,
            val dimensions: String?,
            val accessionNum: String?,
            val wikidataId: String?
        )

        val objectsFile = ensureDownloaded("objects.csv")
        val candidates = mutableListOf<Partial>()
        openCsv(objectsFile).use { parser ->
            for (record in parser) {
                val objectId = record.get("objectid")?.trim().orEmpty()
                if (objectId.isEmpty() || !bestImageByObjectId.containsKey(objectId)) continue
                val classification = record.getOrNull("classification")
                if (ClassificationNormalizer.normalize(classification) !in eligibleClassifications) continue

                candidates += Partial(
                    objectId = objectId,
                    title = record.getOrNull("title"),
                    displayDate = record.getOrNull("displaydate"),
                    beginYear = record.getOrNull("beginyear")?.toIntOrNull(),
                    endYear = record.getOrNull("endyear")?.toIntOrNull(),
                    medium = record.getOrNull("medium"),
                    attribution = record.getOrNull("attribution"),
                    creditLine = record.getOrNull("creditline"),
                    classification = classification,
                    dimensions = record.getOrNull("dimensions"),
                    accessionNum = record.getOrNull("accessionnum"),
                    wikidataId = record.getOrNull("wikidataid")
                )
            }
        }
        println("[nga] ${candidates.size} obras painting/print con imagen open-access (antes de los filtros de elegibilidad del catálogo, ej. año/boceto)")

        val targetIds = candidates.mapTo(HashSet()) { it.objectId }

        val termsFile = ensureDownloaded("objects_terms.csv")
        val styleTermsByObject = mutableMapOf<String, MutableList<String>>()
        val schoolTermsByObject = mutableMapOf<String, MutableList<String>>()
        openCsv(termsFile).use { parser ->
            for (record in parser) {
                val objectId = record.get("objectid")?.trim().orEmpty()
                if (objectId !in targetIds) continue
                val term = record.getOrNull("term") ?: continue
                when (record.getOrNull("termtype")) {
                    "Style" -> styleTermsByObject.getOrPut(objectId) { mutableListOf() }.add(term)
                    "School" -> schoolTermsByObject.getOrPut(objectId) { mutableListOf() }.add(term)
                }
            }
        }

        val relFile = ensureDownloaded("objects_constituents.csv")
        val artistConstituentIdByObject = mutableMapOf<String, String>()
        val bestDisplayOrderByObject = mutableMapOf<String, Int>()
        openCsv(relFile).use { parser ->
            for (record in parser) {
                val objectId = record.get("objectid")?.trim().orEmpty()
                if (objectId !in targetIds) continue
                if (!"artist".equals(record.getOrNull("roletype"), ignoreCase = true)) continue
                val constituentId = record.getOrNull("constituentid") ?: continue
                val displayOrder = record.getOrNull("displayorder")?.toIntOrNull() ?: Int.MAX_VALUE
                val currentBest = bestDisplayOrderByObject[objectId]
                if (currentBest == null || displayOrder < currentBest) {
                    bestDisplayOrderByObject[objectId] = displayOrder
                    artistConstituentIdByObject[objectId] = constituentId
                }
            }
        }

        val neededConstituentIds = artistConstituentIdByObject.values.toHashSet()
        data class ConstituentYears(val beginYear: Int?, val endYear: Int?)

        val constituentsFile = ensureDownloaded("constituents.csv")
        val yearsByConstituentId = mutableMapOf<String, ConstituentYears>()
        openCsv(constituentsFile).use { parser ->
            for (record in parser) {
                val constituentId = record.get("constituentid")?.trim().orEmpty()
                if (constituentId !in neededConstituentIds) continue
                yearsByConstituentId[constituentId] = ConstituentYears(
                    beginYear = record.getOrNull("beginyear")?.toIntOrNull(),
                    endYear = record.getOrNull("endyear")?.toIntOrNull()
                )
            }
        }

        return candidates.mapNotNull { p ->
            val image = bestImageByObjectId[p.objectId] ?: return@mapNotNull null
            val constituentId = artistConstituentIdByObject[p.objectId]
            val years = constituentId?.let { yearsByConstituentId[it] }

            NgaMapper.map(
                NgaRecord(
                    objectId = p.objectId,
                    title = p.title,
                    displayDate = p.displayDate,
                    beginYear = p.beginYear,
                    endYear = p.endYear,
                    medium = p.medium,
                    attribution = p.attribution,
                    creditLine = p.creditLine,
                    classification = p.classification,
                    dimensions = p.dimensions,
                    accessionNum = p.accessionNum,
                    wikidataId = p.wikidataId,
                    artistBeginYear = years?.beginYear,
                    artistEndYear = years?.endYear,
                    styleTerms = styleTermsByObject[p.objectId].orEmpty(),
                    schoolTerms = schoolTermsByObject[p.objectId].orEmpty(),
                    imageIiifUrl = image.iiifUrl
                )
            )
        }
    }

    private data class OpenImage(val iiifUrl: String, val isPrimary: Boolean)

    /** Solo `openaccess=1`, prefiriendo `viewtype=primary` cuando un objeto tiene más de una imagen. */
    private fun indexOpenAccessImages(file: File): Map<String, OpenImage> {
        val best = mutableMapOf<String, OpenImage>()
        openCsv(file).use { parser ->
            for (record in parser) {
                if (record.getOrNull("openaccess") != "1") continue
                val objectId = record.getOrNull("depictstmsobjectid") ?: continue
                val iiifUrl = record.getOrNull("iiifurl") ?: continue
                val isPrimary = record.getOrNull("viewtype") == "primary"
                val current = best[objectId]
                if (current == null || (isPrimary && !current.isPrimary)) {
                    best[objectId] = OpenImage(iiifUrl, isPrimary)
                }
            }
        }
        return best
    }

    private fun CSVRecord.getOrNull(name: String): String? =
        if (isSet(name)) get(name)?.trim()?.ifBlank { null } else null

    private fun openCsv(file: File): CSVParser {
        val format = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .get()
        return CSVParser.parse(file, StandardCharsets.UTF_8, format)
    }

    /** Descarga solo si no hay una copia local ya — son ~235MB en total entre las 5 tablas,
     * no tiene sentido volver a bajarlas en cada corrida. Sin control de "stale": el dataset
     * de NGA se actualiza a diario, así que si se quiere una versión más nueva hay que borrar
     * `cacheDir` a mano (o correr con un `cacheDir` nuevo). */
    private fun ensureDownloaded(fileName: String): File {
        val target = File(cacheDir, fileName)
        if (target.exists() && target.length() > 0) {
            println("[nga] usando copia local de $fileName (${target.length() / 1_000_000}MB)")
            return target
        }
        println("[nga] descargando $fileName ...")
        URI(BASE_URL + fileName).toURL().openStream().use { input ->
            target.outputStream().use { output -> input.copyTo(output) }
        }
        println("[nga] $fileName descargado (${target.length() / 1_000_000}MB)")
        return target
    }
}
