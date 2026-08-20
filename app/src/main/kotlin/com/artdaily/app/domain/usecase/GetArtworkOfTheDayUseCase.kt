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
        val alreadyChosenId = historyDao.getMostRecentSince(widgetId, startOfTodayEpochMillis())
        if (alreadyChosenId != null) {
            // Si la obra ya elegida hoy todavía existe en el catálogo, esa es "la obra del
            // día" — no se sortea una nueva aunque cambien los filtros del widget a mitad
            // del día. Si por algún motivo ya no resuelve (ej. catálogo regenerado), se cae
            // al flujo normal de abajo en vez de devolver null.
            artworkRepository.getById(alreadyChosenId)?.let { return it }
        }

        val config = widgetConfigDao.getById(widgetId)
        val filter = ArtworkFilter(
            period = config?.period,
            century = config?.century,
            movement = config?.movement,
            artistName = config?.artistName,
            museum = config?.museum
        )
        val avoidRepeatDays = config?.avoidRepeatDays ?: 30

        val artwork = selectionEngine.pickForWidget(widgetId, filter, avoidRepeatDays) ?: return null

        historyDao.record(
            HistoryEntity(widgetId = widgetId, artworkId = artwork.id, shownAt = System.currentTimeMillis())
        )
        return artwork
    }

    /** Medianoche local de hoy, en millis — un día de calendario real (zona horaria del
     * dispositivo), no una ventana móvil de 24h. `LocalDate`/`ZoneId` son nativos desde API
     * 26 (el `minSdk` del proyecto), no hace falta desugaring. */
    private fun startOfTodayEpochMillis(): Long {
        val zone = ZoneId.systemDefault()
        return LocalDate.now(zone).atStartOfDay(zone).toInstant().toEpochMilli()
    }
}
