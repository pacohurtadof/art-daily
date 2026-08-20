package com.artdaily.app.data.sync

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Primer uso real de Retrofit DENTRO de `:app` (hasta ahora la app nunca llamaba APIs en
 * vivo, solo el harvester) — mismo patrón que `harvester/network/HttpClientFactory.kt`,
 * pero como módulo Hilt en vez de `object` porque acá sí hay inyección de dependencias.
 * Solo para GitHub Releases (sincronizar `delta.json`) — Met/AIC/CMA/Rijksmuseum siguen
 * siendo territorio exclusivo del harvester.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val GITHUB_API_BASE_URL = "https://api.github.com/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides
    @Singleton
    fun provideGitHubApi(okHttpClient: OkHttpClient, json: Json): GitHubApi =
        Retrofit.Builder()
            .baseUrl(GITHUB_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(GitHubApi::class.java)
}
