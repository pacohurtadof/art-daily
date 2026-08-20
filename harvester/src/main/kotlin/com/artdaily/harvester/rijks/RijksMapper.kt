package com.artdaily.harvester.rijks

import com.artdaily.core.model.Artwork
import com.artdaily.core.normalize.CenturyCalculator
import com.artdaily.core.normalize.ClassificationNormalizer
import com.artdaily.core.normalize.blankToNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

/**
 * DTO de Rijksmuseum (EDM framed) -> modelo común [Artwork]. Mismo criterio de descarte que
 * las otras tres fuentes (licencia + imagen), y misma regla de clasificación: `period`/
 * `movement` quedan en `null` — el EDM de Rijksmuseum no tiene un campo de movimiento
 * artístico limpio (lo más cercano, `dcType`, es tipo de objeto — "painting" — no un
 * movimiento; `subject` son términos de iconografía Iconclass, tampoco un movimiento).
 */
object RijksMapper {
    private const val MUSEUM_NAME = "Rijksmuseum"
    private val YEAR_REGEX = Regex("""\d{4}""")

    fun map(dto: RijksAggregationDto): Artwork? {
        val imageUrl = dto.isShownBy?.id?.blankToNull() ?: return null
        val rights = dto.edmRights?.lowercase() ?: return null
        if (!rights.contains("publicdomain")) return null

        val cho = dto.aggregatedCHO
        val museumId = cho["id"]?.jsonPrimitive?.contentOrNull
            ?.substringAfterLast("/")
            ?: dto.id.substringAfterLast("/").substringBefore("#")

        val creator = cho["creator"]?.jsonArray?.firstOrNull() as? JsonObject
        val creationDateText = cho["created"].langText("en", "nl")
        val creationYearStart = creationDateText?.let { YEAR_REGEX.find(it)?.value?.toIntOrNull() }

        val micrioId = imageUrl.substringAfter("iiif.micr.io/").substringBefore("/")

        return Artwork(
            id = "rijks:$museumId",
            title = cho["title"].edmLangMapFirst("en", "nl") ?: "Sin título",
            artistName = creator?.prefLabel("en", "nl"),
            artistBirthYear = creator?.get("http://rdvocab.info/ElementsGr2/dateOfBirth")?.yearFromIsoDate(),
            artistDeathYear = creator?.get("http://rdvocab.info/ElementsGr2/dateOfDeath")?.yearFromIsoDate(),
            creationDateText = creationDateText,
            creationYearStart = creationYearStart,
            creationYearEnd = creationYearStart, // el EDM no da un rango, solo el texto de display
            period = null,
            movement = null,
            century = CenturyCalculator.fromYear(creationYearStart),
            culture = null, // el EDM de Rijksmuseum no da un campo de cultura tan directo
            country = null,
            classification = ClassificationNormalizer.normalize(cho["dcType"]?.prefLabel("en")),
            museum = MUSEUM_NAME,
            museumId = museumId,
            imageUrlFull = imageUrl,
            imageUrlThumbnail = "https://iiif.micr.io/$micrioId/full/400,/0/default.jpg",
            sourceUrl = dto.isShownAt?.id?.blankToNull()
                ?: "https://www.rijksmuseum.nl/en/collection/$museumId",
            sourceApi = "rijks",
            license = if (rights.contains("zero")) "CC0" else "Public Domain Mark",
            isPublicDomain = true,
            description = cho["description"].edmLangMapFirst("en", "nl"),
            creditLine = null, // el EDM de Rijksmuseum no da un campo equivalente a "credit line"
            descriptionAttribution = null, // cubierto por `rights`/`license` del objeto, sin carve-out aparte
            dimensions = cho["extent"].langText("en", "nl"),
            accessionNumber = cho["identifier"]?.firstPlainString(),
            museumFlaggedHighlight = false, // no hay señal equivalente a isHighlight en el EDM
            rankScore = 0f,
            harvestedAt = System.currentTimeMillis()
        )
    }
}
