package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.HistoryDao
import com.artdaily.app.data.local.HistoryEntity
import com.artdaily.app.data.local.WidgetConfigDao
import com.artdaily.app.domain.selection.SelectionEngine
import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.repository.ArtworkRepository
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * Calcula la obra del día para un widget (o `widgetId = 0` = app principal sin widget) y
 * deja constancia en el historial. La llama `DailyArtworkWorker` una vez al día por cada
 * widget configurado, pero TAMBIÉN se llama cada vez que se abre "Hoy" (`HomeViewModel`) —
 * por eso primero revisa si ya se eligió una obra hoy antes de sortear una nueva: sin este
 * chequeo, cada apertura de la app volvía a sortear (bug real, reportado por el usuario:
 * "la obra de hoy cambia cada vez que entro a la app").
 */
class GetArtworkOfTheDayUseCase @Inject constructor(
    private val selectionEngine: SelectionEngine,
    private val widgetConfigDao: WidgetConfigDao,
    private val historyDao: HistoryDao,
    private val artworkRepository: ArtworkRepository
) {
    suspend operator fun invoke(widgetId: Int = 0): Artwork? {
        val config = widgetConfigDao.getById(widgetId)

        // Si este widget NO tiene ningún filtro propio configurado, comparte la misma "obra
        // del día" que "Hoy" (widgetId 0) en vez de sortear la suya por separado. Antes cada
        // widget tenía su propia fila de historial aunque el filtro fuera idéntico al de
        // Hoy, así que hacía su propio sorteo aleatorio independiente — terminaba mostrando
        // una obra distinta a la de la app aunque no hubiera ninguna razón de filtro para
        // que difiriera (bug real reportado por el usuario, 2026-08-25). Un widget CON
        // filtro propio sigue con su propia clave de historial: su pool de candidatas puede
        // ser distinto al de Hoy, así que necesita su propio sorteo y su propio
        // anti-repetición.
        val hasCustomFilter = config != null && (
            config.period != null || config.movement != null || config.artistName != null ||
                config.yearFrom != null || config.yearTo != null
            )
        val historyKey = if (hasCustomFilter) widgetId else HOME_HISTORY_KEY

        val alreadyChosenId = historyDao.getMostRecentSince(historyKey, startOfTodayEpochMillis())
        if (alreadyChosenId != null) {
            // Si la obra ya elegida hoy todavía existe en el catálogo, esa es "la obra del
            // día" — no se sortea una nueva aunque cambien los filtros del widget a mitad
            // del día. Si por algún motivo ya no resuelve (ej. catálogo regenerado), se cae
            // al flujo normal de abajo en vez de devolver null.
            artworkRepository.getById(alreadyChosenId)?.let { return it }
        }

        // `WidgetConfigEntity.period`/`movement` siguen siendo un solo valor (single-select,
        // sin cambios) — se envuelven en una lista de 0 o 1 elemento para `ArtworkFilter`,
        // que ahora pide listas por la multi-selección de Explorar (2026-08-21).
        val filter = ArtworkFilter(
            periods = config?.period?.let { listOf(it) },
            movements = config?.movement?.let { listOf(it) },
            artistName = config?.artistName,
            yearFrom = config?.yearFrom,
            yearTo = config?.yearTo
        )
        val avoidRepeatDays = config?.avoidRepeatDays ?: 30

        val artwork = selectionEngine.pickForWidget(historyKey, filter, avoidRepeatDays) ?: return null

        historyDao.record(
            HistoryEntity(widgetId = historyKey, artworkId = artwork.id, shownAt = System.currentTimeMillis())
        )
        return artwork
    }

    private companion object {
        /** Misma clave que usa "Hoy" (widgetId = 0) — ver comentario arriba. */
        const val HOME_HISTORY_KEY = 0
    }

    /** Medianoche local de hoy, en millis — un día de calendario real (zona horaria del
     * dispositivo), no una ventana móvil de 24h. `LocalDate`/`ZoneId` son nativos desde API
     * 26 (el `minSdk` del proyecto), no hace falta desugaring. */
    private fun startOfTodayEpochMillis(): Long {
        val zone = ZoneId.systemDefault()
        return LocalDate.now(zone).atStartOfDay(zone).toInstant().toEpochMilli()
    }
}
