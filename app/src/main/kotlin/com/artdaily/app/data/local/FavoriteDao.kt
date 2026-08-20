package com.artdaily.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favorite: FavoriteEntity)

    @Delete
    suspend fun remove(favorite: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE artworkId = :artworkId)")
    suspend fun isFavorite(artworkId: String): Boolean

    /** Versión reactiva de [isFavorite] — usada por Hoy/Detalle para que el estado de
     * favorito no quede "congelado" en el momento del `init {}` de cada ViewModel. Antes
     * cada pantalla leía el valor una sola vez con [isFavorite]; si el favorito se
     * quitaba/agregaba desde OTRA pantalla, la primera nunca se enteraba (bug real:
     * "Hoy" seguía diciendo "Quitar de favoritos" después de haberlo quitado en Detalle). */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE artworkId = :artworkId)")
    fun observeIsFavorite(artworkId: String): Flow<Boolean>

    /** Join contra `artworks` — la UI de favoritos quiere la obra completa, no solo el id. */
    @Query(
        """
        SELECT artworks.* FROM artworks
        INNER JOIN favorites ON artworks.id = favorites.artworkId
        ORDER BY favorites.savedAt DESC
        """
    )
    fun observeAll(): Flow<List<ArtworkEntity>>
}
