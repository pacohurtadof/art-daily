package com.artdaily.harvester.met

import kotlinx.serialization.Serializable

/** GET /public/collection/v1/search — verificado en vivo contra la API real el 2026-08-17. */
@Serializable
data class MetSearchResponse(
    val total: Int,
    val objectIDs: List<Int>? = null // null cuando total == 0
)
