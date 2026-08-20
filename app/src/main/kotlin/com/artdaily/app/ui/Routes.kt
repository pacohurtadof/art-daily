package com.artdaily.app.ui

import java.net.URLEncoder

/** Rutas de navegación de la app — un solo lugar para no repetir strings sueltos. */
object Routes {
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val FAVORITES = "favorites"
    const val SETTINGS = "settings"
    private const val DETAIL_PATTERN = "detail/{artworkId}"
    const val DETAIL = DETAIL_PATTERN

    /** El id real (ej. "met:45734") lleva ":" — se codifica para no romper el parseo de ruta. */
    fun detail(artworkId: String): String = "detail/${URLEncoder.encode(artworkId, "UTF-8")}"
}
