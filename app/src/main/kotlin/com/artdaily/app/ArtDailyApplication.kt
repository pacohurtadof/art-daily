package com.artdaily.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.artdaily.app.worker.DailyArtworkWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Implementa `Configuration.Provider` para que WorkManager use `HiltWorkerFactory` al
 * construir workers `@HiltWorker` (como `DailyArtworkWorker`) — sin esto, WorkManager
 * intentaría instanciarlos con un constructor vacío y fallaría, porque reciben dependencias
 * inyectadas. WorkManager detecta esto automáticamente si la Application implementa esta
 * interfaz; no hace falta tocar el manifest.
 */
@HiltAndroidApp
class ArtDailyApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super.onCreate()
        DailyArtworkWorker.schedulePeriodic(this)
    }
}
