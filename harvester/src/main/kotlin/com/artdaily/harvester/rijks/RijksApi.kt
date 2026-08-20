package com.artdaily.harvester.rijks

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * https://data.rijksmuseum.nl/ — API nueva (la vieja se apagó el 5 de enero de 2026), ya
 * NO requiere API key. Contratos verificados en vivo el 2026-08-18.
 */
interface RijksApi {
    @GET("search/collection")
    suspend fun search(
        @Query("title") title: String,
        @Query("imageAvailable") imageAvailable: Boolean = true
    ): RijksSearchResponse

    /**
     * Los resultados de [search] son URLs absolutas en otro dominio (`id.rijksmuseum.nl`,
     * no `data.rijksmuseum.nl`) — por eso `@Url` en vez de un `@Path`/base URL fijo.
     */
    @GET
    suspend fun resolve(
        @Url url: String,
        @Query("_profile") profile: String = "edm-framed"
    ): RijksAggregationDto
}
