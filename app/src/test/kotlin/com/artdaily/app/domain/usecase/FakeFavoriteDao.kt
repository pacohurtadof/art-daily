package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.ArtworkEntity
import com.artdaily.app.data.local.FavoriteDao
import com.artdaily.app.data.local.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** Test double en memoria — solo implementa lo que `GetNextFavoriteWallpaperUseCase` (y sus
 * tests) necesitan de verdad; el resto de [FavoriteDao] queda sin usar en ningún test hoy,
 * pero igual hay que implementarlo para satisfacer la interfaz. */
class FakeFavoriteDao(initial: List<ArtworkEntity> = emptyList()) : FavoriteDao {

    // Más reciente guardado primero, mismo orden que la query real (`ORDER BY savedAt DESC`).
    private val entities = MutableStateFlow(initial)

    override suspend fun add(favorite: FavoriteEntity) {
        error("No usado por los tests de este paquete")
    }

    override suspend fun remove(favorite: FavoriteEntity) {
        error("No usado por los tests de este paquete")
    }

    override suspend fun isFavorite(artworkId: String): Boolean =
        entities.value.any { it.id == artworkId }

    override fun observeIsFavorite(artworkId: String): Flow<Boolean> {
        error("No usado por los tests de este paquete")
    }

    override fun observeAll(): Flow<List<ArtworkEntity>> = entities

    override suspend fun getAllOnce(): List<ArtworkEntity> = entities.value
}
