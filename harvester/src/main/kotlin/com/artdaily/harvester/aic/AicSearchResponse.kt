package com.artdaily.harvester.aic

import kotlinx.serialization.Serializable

/** GET /api/v1/artworks/search — verificado en vivo contra la API real el 2026-08-17. */
@Serializable
data class AicSearchResponse(
    val pagination: AicPagination,
    val data: List<AicArtworkDto> = emptyList()
)

@Serializable
data class AicPagination(
    val total: Int,
    val limit: Int,
    val current_page: Int
)

/** GET /api/v1/artworks/{id} — envuelve un solo objeto en "data", a diferencia del search. */
@Serializable
data class AicArtworkResponse(
    val data: AicArtworkDto
)
