package com.artdaily.harvester.cma

import kotlinx.serialization.Serializable

/** GET /api/artworks/ — verificado en vivo contra la API real el 2026-08-18. */
@Serializable
data class CmaSearchResponse(
    val info: CmaSearchInfo,
    val data: List<CmaArtworkDto> = emptyList()
)

@Serializable
data class CmaSearchInfo(val total: Int)

/** GET /api/artworks/{id} — envuelve un solo objeto en "data", igual que AIC. */
@Serializable
data class CmaArtworkResponse(val data: CmaArtworkDto)
