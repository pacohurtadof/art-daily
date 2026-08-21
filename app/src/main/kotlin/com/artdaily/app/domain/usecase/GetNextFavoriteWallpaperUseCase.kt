package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.FavoriteDao
import com.artdaily.app.data.local.toArtwork
import com.artdaily.app.data.settings.WallpaperPreferences
import com.artdaily.core.model.Artwork
import com.artdaily.core.rotation.FavoriteRotation
import javax.inject.Inject

/**
 * Calcula la próxima obra en la rotación de fondos de pantalla por Favoritos
 * (`WallpaperSource.FAVORITES_ROTATION`, 2026-08-21, pedido del usuario). Avanza una
 * posición cada vez que se llama (una vez por corrida de `DailyArtworkWorker`, o al tocar
 * el toggle/selector en Ajustes) — no es aleatorio, es un ciclo secuencial sobre el orden
 * de `FavoriteDao` (más reciente guardado primero).
 *
 * El cálculo del ciclo en sí vive en `FavoriteRotation` (core-model, Kotlin puro) — acá
 * solo se conectan las dos piezas con dependencias de Android: leer la lista real de
 * Favoritos y persistir cuál fue la última aplicada.
 */
class GetNextFavoriteWallpaperUseCase @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val wallpaperPreferences: WallpaperPreferences
) {
    suspend operator fun invoke(): Artwork? {
        val favorites = favoriteDao.getAllOnce().map { it.toArtwork() }
        val next = FavoriteRotation.next(favorites, wallpaperPreferences.lastFavoriteArtworkId) ?: return null
        wallpaperPreferences.lastFavoriteArtworkId = next.id
        return next
    }
}
