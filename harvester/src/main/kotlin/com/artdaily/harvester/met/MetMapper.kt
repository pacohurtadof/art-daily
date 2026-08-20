package com.artdaily.harvester.met

import com.artdaily.core.model.Artwork
import com.artdaily.core.normalize.CenturyCalculator
import com.artdaily.core.normalize.ClassificationNormalizer
import com.artdaily.core.normalize.MovementNormalizer
import com.artdaily.core.normalize.PeriodNormalizer
import com.artdaily.core.normalize.blankToNull

/**
 * DTO de Met -> modelo común [Artwork].
 *
 * Respecto a `docs/etapa2-diseno-arquitectura.md` (sección 4):
 * - [map] descarta el objeto si no es dominio público o no tiene imagen — mismo criterio.
 * - `movement` ya NO se deriva de `classification` (ver nota en el mensaje al usuario / commit):
 *   se prueba contra period/culture/objectName vía [MovementNormalizer], que acepta varios
 *   candidatos y devuelve null si ninguno matchea.
 * - `artistName` usa `artistDisplayName` directo en vez de buscar en `constituents` por rol
 *   "Artist" — es el mismo dato pero sin depender de que el rol venga escrito igual siempre;
 *   se cae a `constituents` solo si `artistDisplayName` viene vacío.
 * - `artistBeginDate`/`artistEndDate` llegan como `String` desde la API real (no `Int` como
 *   asumía el documento), así que se parsean con `toIntOrNull()`.
 */
object MetMapper {
    private const val MUSEUM_NAME = "The Metropolitan Museum of Art"

    fun map(dto: MetObjectDto): Artwork? {
        val imageUrl = dto.primaryImage.blankToNull() ?: return null
        if (!dto.isPublicDomain) return null

        val artistName = dto.artistDisplayName.blankToNull()
            ?: dto.constituents?.firstOrNull { it.role == "Artist" }?.name?.blankToNull()

        val period = dto.period.blankToNull()
        val culture = dto.culture.blankToNull()
        val objectName = dto.objectName.blankToNull()

        return Artwork(
            id = "met:${dto.objectID}",
            title = dto.title.blankToNull() ?: "Sin título",
            artistName = artistName,
            artistBirthYear = dto.artistBeginDate.blankToNull()?.toIntOrNull(),
            artistDeathYear = dto.artistEndDate.blankToNull()?.toIntOrNull(),
            creationDateText = dto.objectDate.blankToNull(),
            creationYearStart = dto.objectBeginDate,
            creationYearEnd = dto.objectEndDate,
            period = PeriodNormalizer.normalize(period ?: culture),
            movement = MovementNormalizer.normalize(period, culture, objectName),
            century = CenturyCalculator.fromYear(dto.objectBeginDate),
            culture = culture,
            country = dto.country.blankToNull(),
            classification = ClassificationNormalizer.normalize(dto.classification),
            museum = MUSEUM_NAME,
            museumId = dto.objectID.toString(),
            imageUrlFull = imageUrl,
            imageUrlThumbnail = dto.primaryImageSmall.blankToNull() ?: imageUrl,
            sourceUrl = dto.objectURL.blankToNull()
                ?: "https://www.metmuseum.org/art/collection/search/${dto.objectID}",
            sourceApi = "met",
            license = "CC0",
            isPublicDomain = true,
            // El Met no expone ningún campo de reseña/historia (verificado en vivo el
            // 2026-08-19 contra el objeto completo — solo metadata, sin texto curatorial).
            description = null,
            creditLine = dto.creditLine.blankToNull(),
            descriptionAttribution = null,
            dimensions = dto.dimensions.blankToNull(),
            accessionNumber = dto.accessionNumber.blankToNull(),
            museumFlaggedHighlight = dto.isHighlight,
            rankScore = 0f, // se calcula después con RankScoreCalculator, una vez mapeado
            harvestedAt = System.currentTimeMillis()
        )
    }
}
