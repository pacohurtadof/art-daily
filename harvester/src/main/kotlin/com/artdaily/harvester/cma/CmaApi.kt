package com.artdaily.harvester.cma

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * https://openaccess-api.clevelandart.org/ — no requiere API key.
 * Contratos verificados en vivo el 2026-08-18 contra la API real.
 */
interface CmaApi {
    @GET("api/artworks/")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int = 100
    ): CmaSearchResponse

    @GET("api/artworks/{id}")
    suspend fun getArtwork(@Path("id") id: Int): CmaArtworkResponse
}
