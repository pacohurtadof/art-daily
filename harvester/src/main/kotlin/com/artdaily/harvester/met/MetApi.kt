package com.artdaily.harvester.met

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * https://collectionapi.metmuseum.org/ — no requiere API key.
 * Contratos verificados en vivo el 2026-08-17 contra la API real.
 */
interface MetApi {
    @GET("public/collection/v1/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("medium") medium: String? = null,     // ej. "Paintings"
        @Query("hasImages") hasImages: Boolean = true
    ): MetSearchResponse

    @GET("public/collection/v1/objects/{objectID}")
    suspend fun getObject(@Path("objectID") objectID: Int): MetObjectDto
}
