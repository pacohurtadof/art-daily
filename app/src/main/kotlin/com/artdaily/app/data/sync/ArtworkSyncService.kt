package com.artdaily.app.data.sync

import com.artdaily.app.data.local.ArtworkDao
import com.artdaily.app.data.local.toEntity
import javax.inject.Inject
import javax.inject.Singleton

sealed interface SyncResult {
    data class Synced(val count: Int) : SyncResult
    data object AlreadyUpToDate : SyncResult
    data object Failed : SyncResult
}

private const val DELTA_ASSET_NAME = "delta.json"

/**
 * Sincroniza obras nuevas/cambiadas desde el último release de GitHub — el paso que le
 * faltaba al plan original (ver CLAUDE.md, punto 13 pendiente hasta el 2026-08-19):
 * `artworks.db` empaquetado en `assets/` solo alimenta el primer arranque; sin esto, la
 * app nunca se enteraba de obras cosechadas después de instalada.
 *
 * Todo el error handling cae a [SyncResult.Failed] en vez de relanzar — quien llama
 * (`DailyArtworkWorker`) lo trata como best-effort: sin red, o si GitHub no responde, el
 * resto del worker sigue funcionando con lo que ya hay en Room.
 */
@Singleton
class ArtworkSyncService @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val artworkDao: ArtworkDao,
    private val syncPreferences: SyncPreferences
) {
    suspend fun syncIfNeeded(): SyncResult {
        val release = try {
            gitHubApi.getLatestRelease()
        } catch (e: Exception) {
            return SyncResult.Failed
        }

        // Ya se procesó este release — no hay nada nuevo que bajar. `INSERT OR REPLACE`
        // hace que reprocesar el mismo delta sea inofensivo igual, pero evitar la descarga
        // de nuevo ahorra datos y batería en la corrida diaria del worker.
        if (release.tagName == syncPreferences.lastSyncedTag) return SyncResult.AlreadyUpToDate

        val deltaUrl = release.assets.find { it.name == DELTA_ASSET_NAME }?.browserDownloadUrl
            ?: return SyncResult.Failed

        return try {
            val artworks = gitHubApi.downloadDeltaJson(deltaUrl)
            artworkDao.upsertAll(artworks.map { it.toEntity() })
            syncPreferences.lastSyncedTag = release.tagName
            SyncResult.Synced(artworks.size)
        } catch (e: Exception) {
            SyncResult.Failed
        }
    }
}
