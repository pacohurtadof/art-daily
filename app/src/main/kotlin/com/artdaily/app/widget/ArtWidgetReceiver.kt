package com.artdaily.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.artdaily.app.data.local.HistoryDao
import com.artdaily.app.data.local.WidgetConfigDao
import com.artdaily.app.worker.DailyArtworkWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * `docs/etapa2-diseno-arquitectura.md`, sección 6: cada `appWidgetId` tiene su propia fila
 * de configuración/historial, independiente de los demás widgets.
 */
@AndroidEntryPoint
class ArtWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ArtWidget()

    @Inject lateinit var widgetConfigDao: WidgetConfigDao
    @Inject lateinit var historyDao: HistoryDao

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        // El widget recién agregado no debería esperar ~24h a la próxima corrida periódica.
        DailyArtworkWorker.enqueueOneTime(context)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        scope.launch {
            appWidgetIds.forEach { widgetId ->
                widgetConfigDao.delete(widgetId)
                historyDao.clearForWidget(widgetId)
                WidgetImageDownloader.deleteFor(context, widgetId)
            }
        }
    }
}
