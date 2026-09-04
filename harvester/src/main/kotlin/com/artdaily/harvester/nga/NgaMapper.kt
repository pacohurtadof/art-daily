package com.artdaily.harvester.nga

import com.artdaily.core.model.Artwork
import com.artdaily.core.normalize.CenturyCalculator
import com.artdaily.core.normalize.ClassificationNormalizer
import com.artdaily.core.normalize.MovementNormalizer
import com.artdaily.core.normalize.PeriodNormalizer
import com.artdaily.core.normalize.blankToNull

/**
 * [NgaRecord] (ya unido desde los CSVs de NGA) -> modelo común [Artwork]. Mismo patrón que
 * Met/AIC/CMA/Rijks: el mapper decide si descarta el registro (acá, solo por título/imagen
 * vacíos — el filtro de licencia real, `openaccess=1`, ya se aplicó al construir el
 * [NgaRecord] en [NgaCsvIngester], no acá).
 *
 * Investigado en vivo el 2026-09-04 (descarga real de los CSV, no de memoria — ver
 * `docs/bitacora.md`):
 * - `objects_terms` con termType="Style" trae movimiento/periodo real, pero casi siempre en
 *   forma adjetiva "-ist"/"-ive" ("Impressionist", no "Impressionism") — de ahí los alias
 *   nuevos agregados a [MovementNormalizer] el mismo día. "Baroque"/"Renaissance"/"Gothic"/
 *   "Rococo" también aparecen ahí pero son periodo, no movimiento (ya cubierto por
 *   [PeriodNormalizer] sin cambios).
 * - termType="School" es nacionalidad/escuela de origen ("Dutch", "American"), no movimiento
 *   — se usa como candidato de [Artwork.country], nunca de movimiento.
 * - No hay ningún campo de reseña curatorial limpia en el dataset (`objects_text_entries` solo
 *   trae bibliografía/historial de exhibición/procedencia, textos de archivo, no editoriales)
 *   — [Artwork.description] queda siempre `null` para esta fuente, a diferencia de
 *   Rijksmuseum/CMA/AIC.
 * - `attribution` de `objects.csv` ya es un nombre de artista listo para mostrar (ej.
 *   "Johannes Vermeer", o "Imitator of Johannes Vermeer" cuando corresponde) — no hace falta
 *   parsear `attributioninverted`.
 * - No se encontró un campo equivalente al `isHighlight` del Met — [Artwork.museumFlaggedHighlight]
 *   queda siempre `false`, mismo criterio que AIC ("mejor no fingir una señal que no existe").
 */
object NgaMapper {
    private const val MUSEUM_NAME = "National Gallery of Art"

    // Mismo patrón IIIF que AIC (`AicMapper.IIIF_BASE`): ancho fijo en vez de la resolución
    // completa (algunas imágenes de NGA superan los 12000px de lado, ver docs/bitacora.md).
    private const val FULL_WIDTH = "843,"
    private const val THUMB_WIDTH = "200,"

    fun map(record: NgaRecord): Artwork? {
        val title = record.title?.blankToNull() ?: "Sin título"
        val iiifUrl = record.imageIiifUrl.blankToNull() ?: return null

        val styleCandidates = record.styleTerms.toTypedArray()

        return Artwork(
            id = "nga:${record.objectId}",
            title = title,
            artistName = record.attribution?.blankToNull(),
            artistBirthYear = record.artistBeginYear,
            artistDeathYear = record.artistEndYear,
            creationDateText = record.displayDate?.blankToNull(),
            creationYearStart = record.beginYear,
            creationYearEnd = record.endYear,
            period = PeriodNormalizer.normalize(*styleCandidates),
            movement = MovementNormalizer.normalize(*styleCandidates),
            century = CenturyCalculator.fromYear(record.beginYear),
            culture = null,
            country = record.schoolTerms.firstOrNull()?.blankToNull(),
            classification = ClassificationNormalizer.normalize(record.classification),
            museum = MUSEUM_NAME,
            museumId = record.objectId,
            imageUrlFull = "$iiifUrl/full/$FULL_WIDTH/0/default.jpg",
            imageUrlThumbnail = "$iiifUrl/full/$THUMB_WIDTH/0/default.jpg",
            sourceUrl = "https://www.nga.gov/collection/art-object-page.${record.objectId}.html",
            sourceApi = "nga",
            license = "CC0",
            isPublicDomain = true,
            description = null, // ver nota de la clase: NGA no expone reseña curatorial limpia
            creditLine = record.creditLine?.blankToNull(),
            descriptionAttribution = null,
            dimensions = record.dimensions?.blankToNull(),
            accessionNumber = record.accessionNum?.blankToNull(),
            museumFlaggedHighlight = false,
            rankScore = 0f,
            harvestedAt = System.currentTimeMillis()
        )
    }
}
