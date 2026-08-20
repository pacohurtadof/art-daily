package com.artdaily.harvester.aic

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * https://api.artic.edu/ — no requiere API key.
 * Contratos verificados en vivo el 2026-08-17 contra la API real.
 */
interface AicApi {
    @GET("api/v1/artworks/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("fields") fields: String = FIELDS,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ): AicSearchResponse

    @GET("api/v1/artworks/{id}")
    suspend fun getArtwork(
        @Path("id") id: Int,
        @Query("fields") fields: String = FIELDS
    ): AicArtworkResponse

    companion object {
        // `description`/`short_description`: reseña curatorial real, a diferencia del resto
        // de estos campos viene licenciada CC BY 4.0, no CC0 (ver nota en AicMapper).
        const val FIELDS = "id,title,artist_title,date_display,date_start,date_end," +
            "place_of_origin,dimensions,medium_display,credit_line,is_public_domain," +
            "department_title,style_title,style_titles,classification_title,image_id,api_link," +
            "accession_number,description,short_description"
    }
}
