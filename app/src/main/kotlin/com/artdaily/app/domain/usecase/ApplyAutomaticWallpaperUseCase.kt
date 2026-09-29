package com.artdaily.app.domain.usecase

import com.artdaily.app.data.settings.WallpaperPreferences
import com.artdaily.app.wallpaper.WallpaperApplier
import com.artdaily.app.wallpaper.WallpaperSource
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

/**
 * Aplica el fondo de pantalla automático (Ajustes → "Cambiar automáticamente"), si está
 * activado, con la fuente elegida (obra del día o rotación de Favoritos) — pero SOLO si
 * todavía no se aplicó hoy. Extraído de `DailyArtworkWorker.doWork()` el 2026-09-28 (antes
 * la lógica vivía inline ahí) para que la guarda diaria se pueda probar en un test JVM
 * normal (`ApplyAutomaticWallpaperUseCaseTest`), sin tener que instanciar el `Worker` real
 * (acoplado a `Context`/`GlanceAppWidgetManager`, fuera del alcance de los tests unitarios
 * de este proyecto — ver `docs/bitacora.md`, 2026-08-17, decisión de no usar Robolectric).
 *
 * La guarda existe por un bug real reportado por el usuario (2026-09-28): "el wallpaper...
 * a veces se cambia por la tarde de forma aleatoria". Causa: `ArtWidgetReceiver.onUpdate()`
 * dispara una corrida "extra" de `DailyArtworkWorker` (vía `enqueueOneTime`) en cada
 * `APPWIDGET_UPDATE` que reparte Android — no solo al agregar un widget, también al
 * reiniciar el dispositivo o reinstalar/actualizar la app. Sin esta guarda, cada corrida
 * extra volvía a aplicar el fondo: con `WallpaperSource.FAVORITES_ROTATION`,
 * `GetNextFavoriteWallpaperUseCase` avanza una posición CADA VEZ que se llama (por diseño,
 * no tiene noción de "día" — ver su propio comentario), así que el fondo terminaba
 * cambiando de nuevo a cualquier hora, sin relación con el ciclo de medianoche.
 */
class ApplyAutomaticWallpaperUseCase @Inject constructor(
    private val wallpaperPreferences: WallpaperPreferences,
    private val getArtworkOfTheDay: GetArtworkOfTheDayUseCase,
    private val getNextFavoriteWallpaper: GetNextFavoriteWallpaperUseCase,
    private val wallpaperApplier: WallpaperApplier
) {
    /** Mutable a propósito, mismo patrón que `GetArtworkOfTheDayUseCase.clock`. */
    internal var clock: Clock = Clock.systemDefaultZone()

    /**
     * @param bypassDailyGuard true cuando quien llama es una acción EXPLÍCITA del usuario
     * (tocar el toggle o cambiar fuente/destino en `SettingsScreen`, vía `SettingsViewModel`)
     * — ahí sí debe aplicarse ya mismo aunque ya se haya aplicado hoy, para que el cambio de
     * configuración se note al toque (bug real ya arreglado antes, 2026-08-21: "activé el
     * toggle pero no pasó nada con mi fondo"). `false` (default) es el camino automático del
     * worker, el que sí debe respetar la guarda diaria.
     *
     * @return true si se aplicó (o si no hacía falta: automático desactivado, o ya aplicado
     * hoy y `bypassDailyGuard = false`) — false solo cuando debía aplicarse y
     * `WallpaperApplier.apply` falló.
     */
    suspend operator fun invoke(bypassDailyGuard: Boolean = false): Boolean {
        if (!bypassDailyGuard && !wallpaperPreferences.autoChangeEnabled.value) return true

        val todayEpochDay = LocalDate.now(clock).toEpochDay()
        if (!bypassDailyGuard && alreadyAppliedToday(wallpaperPreferences.lastAutoAppliedEpochDay, todayEpochDay)) {
            return true
        }

        // Fuente elegida en Ajustes (WallpaperPreferences.source, 2026-08-21): la obra del
        // día (widgetId=0, mismo convenio que usa HomeViewModel, no depende de que haya
        // widgets colocados) o la próxima en la rotación de Favoritos.
        val artwork = when (wallpaperPreferences.source.value) {
            WallpaperSource.DAILY_ARTWORK -> getArtworkOfTheDay(widgetId = 0)
            WallpaperSource.FAVORITES_ROTATION -> getNextFavoriteWallpaper()
        }
        val imageUrl = artwork?.imageUrlFull ?: artwork?.imageUrlThumbnail

        val applied = wallpaperApplier.apply(imageUrl, wallpaperPreferences.target.value)
        // Solo se marca "ya aplicado hoy" si de verdad se aplicó — si falló (red,
        // decodificación), se deja sin marcar para que la próxima corrida lo reintente el
        // mismo día en vez de esperar a mañana.
        if (applied) wallpaperPreferences.lastAutoAppliedEpochDay = todayEpochDay
        return applied
    }

    companion object {
        /** `internal`, no `private` — probada directo en `ApplyAutomaticWallpaperUseCaseTest`. */
        internal fun alreadyAppliedToday(lastAppliedEpochDay: Long?, todayEpochDay: Long): Boolean =
            lastAppliedEpochDay == todayEpochDay
    }
}
