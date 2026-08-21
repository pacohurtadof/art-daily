package com.artdaily.app.wallpaper

import androidx.annotation.StringRes
import com.artdaily.app.R

/**
 * De dónde sale la imagen para el cambio AUTOMÁTICO de fondo de pantalla (2026-08-21,
 * pedido del usuario: "que el fondo rote entre las obras que tengo en favoritos", en vez
 * de repetir siempre la obra del día). El botón manual de Detalle no usa esto — siempre
 * aplica la obra que se está viendo en ese momento, sin importar esta preferencia.
 */
enum class WallpaperSource(@StringRes val labelRes: Int) {
    DAILY_ARTWORK(R.string.wallpaper_source_daily),
    FAVORITES_ROTATION(R.string.wallpaper_source_favorites)
}
