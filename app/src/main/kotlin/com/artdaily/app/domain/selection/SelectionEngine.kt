package com.artdaily.app.domain.selection

import com.artdaily.app.data.local.HistoryDao
import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.repository.ArtworkRepository
import javax.inject.Inject

/**
 * `docs/etapa2-diseno-arquitectura.md`, sección 9 — random ponderado + anti-repetición.
 * Respecto al pseudocódigo original: ahí recibía un `dao: ArtworkDao` y llamaba
 * `dao.getRecentHistory(...)` en el mismo objeto; aquí se separó en `ArtworkRepository`
 * (filtro) + `HistoryDao` (historial), porque son responsabilidades de tablas distintas.
 */
class SelectionEngine @Inject constructor(
    private val artworkRepository: ArtworkRepository,
    private val historyDao: HistoryDao
) {
    suspend fun pickForWidget(widgetId: Int, filter: ArtworkFilter, avoidRepeatDays: Int): Artwork? {
        val candidates = artworkRepository.getFiltered(filter)
        if (candidates.isEmpty()) return null

        val sinceEpochMillis = System.currentTimeMillis() - avoidRepeatDays * DAY_MILLIS
        val recentlyShown = historyDao.getRecentArtworkIds(widgetId, sinceEpochMillis).toSet()

        // Si el pool de candidatos es más chico que la ventana de "no repetir", el ciclo se
        // reinicia solo (se vuelve a permitir repetir) en vez de devolver null — decisión ya
        // tomada en la Etapa 1, sección "Riesgos de producto".
        val pool = candidates.filterNot { it.id in recentlyShown }.ifEmpty { candidates }
        return pool.randomOrNull()
    }

    private companion object {
        const val DAY_MILLIS = 24L * 60 * 60 * 1000
    }
}
