package com.artdaily.harvester.smithsonian

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * `api.si.edu/openaccess/` — requiere API key gratuita (`api.data.gov/signup/`), leída de
 * `harvester/.env` vía `EnvConfig` (nunca commiteada). Contratos verificados en vivo el
 * 2026-09-04 contra la API real, con la key del usuario.
 *
 * A diferencia de Met (que necesita una llamada de detalle por objeto), el registro completo
 * ya viene en la respuesta de `search` — mismo patrón que CMA/AIC.
 */
interface SmithsonianApi {
    @GET("openaccess/api/v1.0/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("start") start: Int = 0,
        @Query("rows") rows: Int = 1000, // 1000 es el máximo real, documentado y verificado
        @Query("api_key") apiKey: String
    ): SmithsonianSearchResponse
}
