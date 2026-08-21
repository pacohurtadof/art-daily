package com.artdaily.core.rotation

import com.artdaily.core.model.Artwork

/**
 * Calcula la próxima obra en un ciclo secuencial sobre una lista (más reciente guardado
 * primero, mismo orden que `FavoriteDao`) — usado por la rotación de fondos de pantalla
 * por Favoritos (`WallpaperSource.FAVORITES_ROTATION`, 2026-08-21, pedido del usuario).
 *
 * Kotlin puro, sin dependencias de Android — separado de `GetNextFavoriteWallpaperUseCase`
 * (que sí depende de `FavoriteDao`/`WallpaperPreferences`, ambos con dependencias de
 * Android) a propósito, para poder probar el cálculo del ciclo en un test JVM normal, sin
 * necesitar Robolectric ni un Context real.
 */
object FavoriteRotation {

    /** `lastId` es la obra aplicada la vez anterior. Si no está en `favorites` (se sacó de
     * favoritos) o es `null` (primera vez), `indexOfFirst` da -1 y el cálculo arranca desde
     * el principio de la lista sin necesitar un caso especial. `null` si `favorites` está
     * vacía — no hay nada para rotar. */
    fun next(favorites: List<Artwork>, lastId: String?): Artwork? {
        if (favorites.isEmpty()) return null
        val lastIndex = favorites.indexOfFirst { it.id == lastId }
        return favorites[(lastIndex + 1) % favorites.size]
    }
}
