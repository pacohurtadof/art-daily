package com.artdaily.harvester.smithsonian

import com.artdaily.core.model.Artwork
import com.artdaily.core.normalize.CenturyCalculator
import com.artdaily.core.normalize.ClassificationNormalizer
import com.artdaily.core.normalize.blankToNull

/**
 * DTO de Smithsonian -> modelo común [Artwork]. Mismo patrón que Met/AIC/CMA/Rijks/NGA: el
 * mapper decide si descarta el registro (acá, por licencia de la imagen específica).
 *
 * **Licencia**: a diferencia de "está en el dataset", el criterio real es que la imagen tenga
 * `usage.access == "CC0"` — verificado en vivo el 2026-09-04 que records SIN esa condición sí
 * existen (`"Usage conditions apply"`), mismo gotcha que `published_images.openaccess` de NGA.
 *
 * **Sin `movement`/`period` automático**: a diferencia de NGA (que trae `Style` real),
 * Smithsonian no tiene ningún campo equivalente — solo `topic` (tema/sujeto, ej. "Automobiles",
 * "Figure female"), que NO es movimiento artístico. Quedan siempre `null` acá; se completan
 * después a mano vía `MovementOverrides`/`PeriodOverrides` (mismo mecanismo que NGA).
 *
 * **`name` es texto libre biográfico**, no un campo estructurado — ej. "Mary Vaux Walcott,
 * born Philadelphia, PA 1860-died St. Andrews, New Brunswick, Canada 1940". Se parsea con
 * regex: el nombre es todo lo que precede a ", born"/", died"; los años se extraen del resto.
 * Si el patrón no matchea (perfectamente posible con datos tan heterogéneos), se guarda el
 * nombre igual (mejor mostrar el texto completo que perder el dato) pero sin años.
 */
object SmithsonianMapper {
    // "born Philadelphia, PA 1860" / "died 1940" — el año es el último grupo de 4 dígitos
    // después de la palabra clave, tolerando cualquier texto de lugar en el medio.
    private val BORN_YEAR = Regex("""born[^0-9]*(\d{4})""", RegexOption.IGNORE_CASE)
    private val DIED_YEAR = Regex("""died[^0-9]*(\d{4})""", RegexOption.IGNORE_CASE)
    private val FIRST_YEAR = Regex("""\b(1[0-9]{3}|20[0-4][0-9])\b""") // 1000-2049, descarta ruido tipo "3/4 in."
    private val DECADE = Regex("""(\d{4})s""") // ej. "1920s" de indexedStructured.date

    fun map(row: SmithsonianRow): Artwork? {
        val content = row.content ?: return null
        val dnr = content.descriptiveNonRepeating ?: return null
        val freetext = content.freetext

        // Solo cuenta una imagen con usage.access == "CC0" — nunca la primera imagen a secas.
        val media = dnr.online_media?.media.orEmpty().firstOrNull { it.usage?.access == "CC0" }
            ?: return null
        val fullUrl = media.resources.firstOrNull { it.label == "High-resolution JPEG" }?.url
            ?: media.content?.blankToNull()
            ?: return null
        val thumbUrl = media.resources.firstOrNull { it.label == "Thumbnail Image" }?.url
            ?: media.resources.firstOrNull { it.label == "Screen Image" }?.url
            ?: media.thumbnail?.blankToNull()
            ?: fullUrl

        val recordId = dnr.record_ID?.blankToNull() ?: row.id
        val title = row.title?.blankToNull()
            ?: dnr.record_ID // último recurso, mejor que "Sin título" repetido sin contexto
            ?: "Sin título"

        val rawArtist = freetext?.name?.firstOrNull { it.label == "Artist" }?.content?.blankToNull()
        val artistName = rawArtist
            ?.substringBefore(", born")
            ?.substringBefore(", died")
            ?.trim()
            ?.blankToNull()
        val artistBirthYear = rawArtist?.let { BORN_YEAR.find(it)?.groupValues?.get(1)?.toIntOrNull() }
        val artistDeathYear = rawArtist?.let { DIED_YEAR.find(it)?.groupValues?.get(1)?.toIntOrNull() }

        val creationDateText = freetext?.date?.firstOrNull()?.content?.blankToNull()
        val creationYearStart = creationDateText?.let { FIRST_YEAR.find(it)?.value?.toIntOrNull() }
            ?: content.indexedStructured?.date?.firstOrNull()
                ?.let { DECADE.find(it)?.groupValues?.get(1)?.toIntOrNull() }

        val objectType = freetext?.objectType?.firstOrNull()?.content

        return Artwork(
            id = "si:$recordId",
            title = title,
            artistName = artistName,
            artistBirthYear = artistBirthYear,
            artistDeathYear = artistDeathYear,
            creationDateText = creationDateText,
            creationYearStart = creationYearStart,
            creationYearEnd = creationYearStart, // Smithsonian no da un rango, solo una fecha
            period = null, // ver nota de la clase — sin campo equivalente a Style de NGA
            movement = null,
            century = CenturyCalculator.fromYear(creationYearStart),
            culture = null, // sin campo de cultura limpio en este esquema
            country = null,
            classification = ClassificationNormalizer.normalize(objectType),
            museum = dnr.data_source?.blankToNull() ?: "Smithsonian Institution",
            museumId = recordId,
            imageUrlFull = fullUrl,
            imageUrlThumbnail = thumbUrl,
            sourceUrl = dnr.record_link?.blankToNull() ?: "https://www.si.edu/object/$recordId",
            sourceApi = "si",
            license = "CC0",
            isPublicDomain = true,
            description = freetext?.notes?.joinToString(" ") { it.content.orEmpty() }?.blankToNull(),
            creditLine = freetext?.creditLine?.firstOrNull()?.content?.blankToNull(),
            descriptionAttribution = null, // CC0, no exige atribución
            dimensions = freetext?.physicalDescription?.firstOrNull { it.label == "Dimensions" }?.content?.blankToNull(),
            accessionNumber = freetext?.identifier?.firstOrNull()?.content?.blankToNull(),
            museumFlaggedHighlight = false, // sin señal equivalente al isHighlight del Met
            rankScore = 0f,
            harvestedAt = System.currentTimeMillis()
        )
    }
}
