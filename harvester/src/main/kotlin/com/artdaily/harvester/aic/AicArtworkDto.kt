package com.artdaily.harvester.aic

import kotlinx.serialization.Serializable

/**
 * GET /api/v1/artworks/{id} (y cada elemento de /search). A diferencia del Met, AIC usa
 * snake_case — se respeta tal cual para no meter anotaciones `@SerialName` de más.
 *
 * Verificado en vivo el 2026-08-17 (artwork id 64729, "Early Morning, Tarpon Springs").
 * Nota: `style_titles` (plural, lista) es justo lo que le falta al Met — AIC sí trae
 * movimiento/estilo limpio para la mayoría de sus obras occidentales.
 */
@Serializable
data class AicArtworkDto(
    val id: Int,
    val title: String = "",
    val artist_title: String? = null,
    val date_display: String? = null,
    val date_start: Int? = null,
    val date_end: Int? = null,
    val place_of_origin: String? = null,
    val dimensions: String? = null,
    val medium_display: String? = null,
    val credit_line: String? = null,
    val is_public_domain: Boolean = false,
    val department_title: String? = null,
    val style_title: String? = null,
    val style_titles: List<String> = emptyList(),
    val classification_title: String? = null,
    val image_id: String? = null,
    val api_link: String? = null,
    val accession_number: String? = null,
    // Reseña curatorial real (HTML simple, ej. `<p>...</p>`) — licenciada CC BY 4.0, no CC0
    // como el resto de estos campos (ver AicMapper). `description` es la larga; cuando no
    // existe, `short_description` es un resumen más breve, misma licencia.
    val description: String? = null,
    val short_description: String? = null
)
