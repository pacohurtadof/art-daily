package com.artdaily.app.data.sync

import com.artdaily.core.model.Artwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Solo lo que usa [ArtworkSyncService]: la API de GitHub Releases (`api.github.com`, sin
 * key, límite de tasa generoso para uso no autenticado) para saber cuál es el último
 * release, y el asset `delta.json` de ese release para sincronizar obras nuevas/cambiadas
 * sin necesitar un reinstall con un `artworks.db` regenerado a mano — ver
 * `harvester/storage/DeltaJsonWriter.kt` (que ya anticipaba este paso) y
 * `docs/bitacora.md` (2026-08-19).
 */
interface GitHubApi {

    @GET("repos/pacohurtadof/art-daily/releases/latest")
    suspend fun getLatestRelease(): GitHubReleaseDto

    /** El asset real vive en otro dominio (GitHub redirige a una URL firmada en
     * `release-assets.githubusercontent.com`) — mismo patrón que `RijksApi.resolve`
     * en el harvester: `@Url` con la URL absoluta, ignora el `baseUrl` del Retrofit. */
    @GET
    suspend fun downloadDeltaJson(@Url url: String): List<Artwork>
}

@Serializable
data class GitHubReleaseDto(
    @SerialName("tag_name") val tagName: String,
    val assets: List<GitHubAssetDto> = emptyList()
)

@Serializable
data class GitHubAssetDto(
    val name: String,
    @SerialName("browser_download_url") val browserDownloadUrl: String
)
