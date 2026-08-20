package com.artdaily.harvester.aic

import com.artdaily.core.model.Artwork
import com.artdaily.core.normalize.CenturyCalculator
import com.artdaily.core.normalize.ClassificationNormalizer
import com.artdaily.core.normalize.MovementNormalizer
import com.artdaily.core.normalize.PeriodNormalizer
import com.artdaily.core.normalize.blankToNull
import com.artdaily.core.normalize.stripHtmlTags

/**
 * DTO de AIC -> modelo común [Artwork]. Mismo patrón que [com.artdaily.harvester.met.MetMapper]:
 * cada mapper decide por sí mismo si descarta el objeto (licencia/imagen).
 *
 * A diferencia del Met, AIC sí trae `style_title`/`style_titles` — un movimiento/estilo
 * real dado por el museo (no adivinado). [MovementNormalizer] solo prueba esos dos campos
 * — nunca `department_title` (es una categoría curatorial/de colección, no un movimiento
 * artístico; usarlo como candidato producía asignaciones falsas, ej. "Impresionismo" mal
 * puesto en obras que no lo eran). [PeriodNormalizer] sí puede usar `department_title` como
 * último recurso — para periodo es una señal más débil pero razonable, a diferencia de
 * movimiento donde no aplica en absoluto.
 *
 * No hay campo equivalente al `isHighlight` del Met en los campos que consultamos — AIC no
 * expone una señal de "obra destacada" clara y documentada, así que [Artwork.museumFlaggedHighlight]
 * queda siempre en `false` para esta fuente (mejor no fingir una señal que no existe).
 */
object AicMapper {
    private const val MUSEUM_NAME = "Art Institute of Chicago"

    // Patrón IIIF documentado por AIC. Nota: al probarlo en el entorno de desarrollo (curl,
    // IP de datacenter) Cloudflare devolvió un challenge anti-bot — pendiente confirmar que
    // carga bien desde una red residencial / la app real antes de darlo por sentado.
    private const val IIIF_BASE = "https://www.artic.edu/iiif/2"

    fun map(dto: AicArtworkDto): Artwork? {
        val imageId = dto.image_id.blankToNull() ?: return null
        if (!dto.is_public_domain) return null

        // Movimiento: SOLO campos de estilo reales del museo — nunca el departamento.
        val movementCandidates = buildList {
            dto.style_title?.let { add(it) }
            addAll(dto.style_titles)
        }.toTypedArray()

        // Periodo: los mismos + el departamento como último recurso (señal más débil, pero
        // razonable para periodo — no lo es para movimiento, ver nota de la clase).
        val periodCandidates = movementCandidates + listOfNotNull(dto.department_title)

        // `description` (larga) antes que `short_description` — ambas licenciadas CC BY 4.0
        // (a diferencia del resto de los campos de AIC, que son CC0; ver nota de la clase).
        val rawDescription = dto.description?.blankToNull() ?: dto.short_description?.blankToNull()

        return Artwork(
            id = "aic:${dto.id}",
            title = dto.title.blankToNull() ?: "Sin título",
            artistName = dto.artist_title?.blankToNull(),
            artistBirthYear = null, // no viene en los campos de AIC que consultamos
            artistDeathYear = null,
            creationDateText = dto.date_display?.blankToNull(),
            creationYearStart = dto.date_start,
            creationYearEnd = dto.date_end,
            period = PeriodNormalizer.normalize(*periodCandidates),
            movement = MovementNormalizer.normalize(*movementCandidates),
            century = CenturyCalculator.fromYear(dto.date_start),
            culture = null, // AIC no da un campo de "cultura" tan limpio como el `culture` del Met
            country = dto.place_of_origin?.blankToNull(),
            classification = ClassificationNormalizer.normalize(dto.classification_title),
            museum = MUSEUM_NAME,
            museumId = dto.id.toString(),
            imageUrlFull = "$IIIF_BASE/$imageId/full/843,/0/default.jpg",
            imageUrlThumbnail = "$IIIF_BASE/$imageId/full/200,/0/default.jpg",
            sourceUrl = dto.api_link?.blankToNull()?.replace("/api/v1/", "/")
                ?: "https://www.artic.edu/artworks/${dto.id}",
            sourceApi = "aic",
            license = "CC0",
            isPublicDomain = true,
            description = rawDescription?.stripHtmlTags(),
            creditLine = dto.credit_line?.blankToNull(),
            // Decisión de producto (2026-08-19): mostrar la reseña real de AIC pese a ser
            // CC BY 4.0 (a diferencia del resto de la app, que es CC0), con esta atribución
            // visible en la UI — ver DetailScreen. Null si no hay reseña que mostrar.
            descriptionAttribution = if (rawDescription != null) {
                "Art Institute of Chicago, CC BY 4.0"
            } else {
                null
            },
            dimensions = dto.dimensions?.blankToNull(),
            accessionNumber = dto.accession_number?.blankToNull(),
            museumFlaggedHighlight = false,
            rankScore = 0f,
            harvestedAt = System.currentTimeMillis()
        )
    }
}
