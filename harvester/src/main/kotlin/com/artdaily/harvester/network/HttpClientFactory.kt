package com.artdaily.harvester.network

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

/** Cliente Retrofit compartido por todas las fuentes del harvester (Met, y AIC más adelante). */
object HttpClientFactory {
    private val json = Json {
        ignoreUnknownKeys = true // las APIs de museos devuelven muchos más campos de los que mapeamos
        coerceInputValues = true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            // 2026-08-25: Met (vía su WAF, Incapsula) bloquea con 403 el User-Agent por
            // default de OkHttp ("okhttp/4.12.0") específicamente — confirmado en vivo con
            // curl: mismo request, solo cambiando este header, pasa de 403 a 200. No es un
            // límite de volumen/rate (un curl sin este UA, incluso en ráfaga, pasaba bien).
            // Se identifica el harvester de forma honesta en vez de imitar un browser.
            val request = chain.request().newBuilder()
                .header("User-Agent", "ArtDailyHarvester/1.0 (+https://github.com/pacohurtadof/art-daily)")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        )
        .build()

    fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
