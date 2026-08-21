package com.artdaily.app.worker

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.artdaily.app.data.settings.WallpaperPreferences
import com.artdaily.app.data.sync.ArtworkSyncService
import com.artdaily.app.domain.usecase.GetArtworkOfTheDayUseCase
import com.artdaily.app.domain.usecase.GetNextFavoriteWallpaperUseCase
import com.artdaily.app.wallpaper.WallpaperApplier
import com.artdaily.app.wallpaper.WallpaperSource
import com.artdaily.app.widget.ArtWidget
import com.artdaily.app.widget.WidgetImageDownloader
import com.artdaily.app.widget.toWidgetState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * `docs/etapa2-diseno-arquitectura.md`, sección 5: `PeriodicWorkRequest` (~24h) en vez de
 * `AlarmManager` o `updatePeriodMillis` nativo — ver esa sección para el porqué.
 *
 * Itera sobre cada widget REALMENTE colocado en el home screen (vía `GlanceAppWidgetManager`,
 * no solo lo que hay en `widget_config` — si hay una fila huérfana sin widget colocado, se
 * ignora), calcula su obra del día, y actualiza el estado de Glance de esa instancia.
 *
 * También, si el usuario activó el cambio automático de fondo de pantalla en Ajustes,
 * aplica la obra del día (widgetId=0, la misma convención que usa "Hoy" en la app) como
 * wallpaper — independiente de si hay algún widget colocado o no.
 *
 * Primer paso de todos: sincroniza obras nuevas/cambiadas desde el último release de
 * GitHub (ver `ArtworkSyncService`) — antes `assets/artworks.db` era la única fuente de
 * datos posible durante toda la vida de la app, sin este paso nunca llegaba nada nuevo
 * después de instalada. Best-effort: si falla (sin red, GitHub caído), el resto del
 * worker sigue con lo que ya hay en Room, no se cae todo por esto.
 */
@HiltWorker
class DailyArtworkWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getArtworkOfTheDay: GetArtworkOfTheDayUseCase,
    private val getNextFavoriteWallpaper: GetNextFavoriteWallpaperUseCase,
    private val wallpaperPreferences: WallpaperPreferences,
    private val wallpaperApplier: WallpaperApplier,
    private val artworkSyncService: ArtworkSyncService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        artworkSyncService.syncIfNeeded()

        val glanceManager = GlanceAppWidgetManager(applicationContext)
        val glanceIds = glanceManager.getGlanceIds(ArtWidget::class.java)

        glanceIds.forEach { glanceId ->
            val widgetId = glanceManager.getAppWidgetId(glanceId)
            val artwork = getArtworkOfTheDay(widgetId) ?: return@forEach

            val imagePath = WidgetImageDownloader.downloadToFile(
                applicationContext, widgetId, artwork.imageUrlThumbnail
            )

            ArtWidget.updateState(
                applicationContext, glanceId, artwork.toWidgetState().copy(imageFilePath = imagePath)
            )
        }

        if (wallpaperPreferences.autoChangeEnabled.value) {
            // Fuente elegida en Ajustes (WallpaperPreferences.source, 2026-08-21): la obra
            // del día (widgetId=0, mismo convenio que usa HomeViewModel, no depende de que
            // haya widgets colocados) o la próxima en la rotación de Favoritos. Destino
            // (home/lock/ambas) también viene de Ajustes — a diferencia del diálogo manual
            // de Detalle, acá no hay a quién preguntarle, el worker corre solo sin UI, por
            // eso sí hace falta guardar ambas preferencias.
            val artwork = when (wallpaperPreferences.source.value) {
                WallpaperSource.DAILY_ARTWORK -> getArtworkOfTheDay(widgetId = 0)
                WallpaperSource.FAVORITES_ROTATION -> getNextFavoriteWallpaper()
            }
            val imageUrl = artwork?.imageUrlFull ?: artwork?.imageUrlThumbnail
            wallpaperApplier.apply(imageUrl, wallpaperPreferences.target.value)
        }

        return Result.success()
    }

    companion object {
        private const val UNIQUE_PERIODIC_NAME = "daily_artwork_worker"
        private const val UNIQUE_ONE_TIME_NAME = "daily_artwork_worker_one_time"

        // Antes no hacía falta — la única red que tocaba el worker era la descarga
        // opcional de imagen del widget. Ahora también sincroniza contra GitHub en cada
        // corrida, así que sí vale la pena que WorkManager espere a haber conexión en vez
        // de intentarlo a ciegas y fallar.
        private val NETWORK_CONSTRAINTS = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        /** Se llama una vez, desde `ArtDailyApplication.onCreate()`. Idempotente (KEEP). */
        fun schedulePeriodic(context: Context) {
            val request = PeriodicWorkRequestBuilder<DailyArtworkWorker>(24, TimeUnit.HOURS)
                .setConstraints(NETWORK_CONSTRAINTS)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_NAME, ExistingPeriodicWorkPolicy.KEEP, request
            )
        }

        /** Se llama al agregar un widget nuevo, para no esperar ~24h a verlo con datos. */
        fun enqueueOneTime(context: Context) {
            val request = OneTimeWorkRequestBuilder<DailyArtworkWorker>()
                .setConstraints(NETWORK_CONSTRAINTS)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_ONE_TIME_NAME, ExistingWorkPolicy.KEEP, request
            )
        }
    }
}
