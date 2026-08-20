package com.artdaily.harvester.cma

import com.artdaily.core.model.Artwork
import com.artdaily.core.normalize.CenturyCalculator
import com.artdaily.core.normalize.ClassificationNormalizer
import com.artdaily.core.normalize.blankToNull

/**
 * DTO de CMA -> modelo común [Artwork]. Mismo patrón que Met/AIC.
 *
 * `period`/`movement` quedan siempre en `null` para esta fuente: CMA no tiene un campo
 * dedicado y limpio para ninguno de los dos — lo más parecido es `current_location` (el
 * nombre de la sala física, ej. "222 Impressionism & Post-Impressionism") o `department`
 * (categoría curatorial), y ya se decidió como regla del proyecto no adivinar clasificación
 * desde ese tipo de texto (mismo motivo por el que se corrigió el mapper de AIC).
 */
object CmaMapper {
    private const val MUSEUM_NAME = "Cleveland Museum of Art"

    fun map(dto: CmaArtworkDto): Artwork? {
        if (dto.share_license_status != "CC0") return null
        val thumbnailUrl = dto.images?.web?.url?.blankToNull() ?: return null

        val primaryCreator = dto.creators.firstOrNull()

        return Artwork(
            id = "cma:${dto.id}",
            title = dto.title.blankToNull() ?: "Sin título",
            artistName = primaryCreator?.description?.blankToNull(),
            artistBirthYear = primaryCreator?.birth_year?.blankToNull()?.toIntOrNull(),
            artistDeathYear = primaryCreator?.death_year?.blankToNull()?.toIntOrNull(),
            creationDateText = dto.creation_date.blankToNull(),
            creationYearStart = dto.creation_date_earliest,
            creationYearEnd = dto.creation_date_latest,
            period = null,
            movement = null,
            century = CenturyCalculator.fromYear(dto.creation_date_earliest),
            culture = dto.culture.firstOrNull()?.blankToNull(),
            country = null, // `culture` ya cubre esto para CMA; no hay campo de país separado
            classification = ClassificationNormalizer.normalize(dto.type),
            museum = MUSEUM_NAME,
            museumId = dto.id.toString(),
            imageUrlFull = dto.images.print?.url?.blankToNull() ?: thumbnailUrl,
            imageUrlThumbnail = thumbnailUrl,
            sourceUrl = dto.url.blankToNull() ?: "https://www.clevelandart.org/art/${dto.id}",
            sourceApi = "cma",
            license = "CC0",
            isPublicDomain = true,
            description = dto.description?.blankToNull(),
            creditLine = dto.creditline.blankToNull(),
            descriptionAttribution = null, // CC0, no exige atribución (ver nota en el DTO)
            dimensions = dto.measurements.blankToNull(),
            accessionNumber = dto.accession_number.blankToNull(),
            museumFlaggedHighlight = dto.is_highlight,
            rankScore = 0f,
            harvestedAt = System.currentTimeMillis()
        )
    }
}
