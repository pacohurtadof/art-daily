package com.artdaily.core.model

/**
 * Combinación de filtros para pedir obras — cualquier campo en `null` significa "no filtrar
 * por esto". Un `WidgetConfigEntity` (en `:app`) se traduce a este objeto antes de consultar.
 */
data class ArtworkFilter(
    val period: String? = null,
    val century: Int? = null,
    val movement: String? = null,
    val artistName: String? = null,
    val museum: String? = null
)
