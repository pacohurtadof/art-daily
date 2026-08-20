package com.artdaily.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtworkDao {

    @Query("SELECT * FROM artworks WHERE id = :id")
    suspend fun getById(id: String): ArtworkEntity?

    @Query("SELECT COUNT(*) FROM artworks")
    suspend fun count(): Int

    /**
     * Filtros opcionales: cualquiera en `null` se ignora (patrón `:param IS NULL OR columna
     * = :param`, estándar en Room para consultas con filtros dinámicos sin armar SQL a mano).
     * Usado por el `SelectionEngine` (sección 9 de `docs/etapa2-diseno-arquitectura.md`).
     */
    @Query(
        """
        SELECT * FROM artworks
        WHERE (:period IS NULL OR period = :period)
        AND (:century IS NULL OR century = :century)
        AND (:movement IS NULL OR movement = :movement)
        AND (:artistName IS NULL OR artistName = :artistName)
        AND (:museum IS NULL OR museum = :museum)
        AND rankScore >= :minRankScore
        """
    )
    suspend fun getFiltered(
        period: String?,
        century: Int?,
        movement: String?,
        artistName: String?,
        museum: String?,
        minRankScore: Float
    ): List<ArtworkEntity>

    /** Mismos filtros que [getFiltered], pero solo cuenta — para la UI de filtros (Etapa 6),
     * que muestra cuántas obras matchean sin tener que traerlas todas. */
    @Query(
        """
        SELECT COUNT(*) FROM artworks
        WHERE (:period IS NULL OR period = :period)
        AND (:century IS NULL OR century = :century)
        AND (:movement IS NULL OR movement = :movement)
        AND (:artistName IS NULL OR artistName = :artistName)
        AND (:museum IS NULL OR museum = :museum)
        AND rankScore >= :minRankScore
        """
    )
    suspend fun countFiltered(
        period: String?,
        century: Int?,
        movement: String?,
        artistName: String?,
        museum: String?,
        minRankScore: Float
    ): Int

    @Query("SELECT DISTINCT period FROM artworks WHERE period IS NOT NULL ORDER BY period")
    suspend fun getDistinctPeriods(): List<String>

    @Query("SELECT DISTINCT movement FROM artworks WHERE movement IS NOT NULL ORDER BY movement")
    suspend fun getDistinctMovements(): List<String>

    @Query("SELECT DISTINCT museum FROM artworks WHERE museum IS NOT NULL ORDER BY museum")
    suspend fun getDistinctMuseums(): List<String>

    @Query("SELECT DISTINCT century FROM artworks WHERE century IS NOT NULL ORDER BY century")
    suspend fun getDistinctCenturies(): List<Int>

    /** Usado por el sync del delta.json (`INSERT OR REPLACE` por id, igual que el harvester). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(artworks: List<ArtworkEntity>)
}
