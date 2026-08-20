package com.artdaily.app.wallpaper

import android.app.WallpaperManager
import androidx.annotation.StringRes
import com.artdaily.app.R

/**
 * A qué pantalla(s) aplicar el fondo — el usuario pidió poder elegir, no fijarlo a una
 * sola opción. Cada valor mapea a las flags reales de [WallpaperManager.setBitmap].
 *
 * `labelRes` en vez de un `String` fijo — se resuelve con `stringResource()` en cada
 * lugar donde se muestra (Ajustes, el diálogo de Detalle), así sigue el idioma del
 * dispositivo igual que el resto del texto de la app.
 */
enum class WallpaperTarget(@StringRes val labelRes: Int) {
    HOME(R.string.wallpaper_target_home),
    LOCK(R.string.wallpaper_target_lock),
    BOTH(R.string.wallpaper_target_both);

    /** `setBitmap` pide las flags como un solo `Int` combinado con OR. */
    fun toFlags(): Int = when (this) {
        HOME -> WallpaperManager.FLAG_SYSTEM
        LOCK -> WallpaperManager.FLAG_LOCK
        BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
    }
}
