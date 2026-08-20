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
     *
     * `yearFrom`/`yearTo` reemplazaron a `museum`/`century` el 2026-08-19 — un rango de años
     * en vez de un chip de siglo entero; comparan contra `creationYearStart` solamente (igual
     * que el `century` que reemplazan, que también se derivaba solo de ese campo).
     */
    @Query(
        """
        SELECT * FROM artworks
        WHERE (:period IS NULL OR period = :period)
        AND (:movement IS NULL OR movement = :movement)
        AND (:artistName IS NULL OR artistName = :artistName)
        AND (:yearFrom IS NULL OR creationYearStart >= :yearFrom)
        AND (:yearTo IS NULL OR creationYearStart <= :yearTo)
        AND rankScore >= :minRankScore
        """
    )
    suspend fun getFiltered(
        period: String?,
        movement: String?,
        artistName: String?,
        yearFrom: Int?,
        yearTo: Int?,
        minRankScore: Float
    ): List<ArtworkEntity>

    /** Mismos filtros que [getFiltered], pero solo cuenta — para la UI de filtros (Etapa 6),
     * que muestra cuántas obras matchean sin tener que traerlas todas. */
    @Query(
        """
        SELECT COUNT(*) FROM artworks
        WHERE (:period IS NULL OR period = :period)
        AND (:movement IS NULL OR movement = :movement)
        AND (:artistName IS NULL OR artistName = :artistName)
        AND (:yearFrom IS NULL OR creationYearStart >= :yearFrom)
        AND (:yearTo IS NULL OR creationYearStart <= :yearTo)
        AND rankScore >= :minRankScore
        """
    )
    suspend fun countFiltered(
        period: String?,
        movement: String?,
        artistName: String?,
        yearFrom: Int?,
        yearTo: Int?,
        minRankScore: Float
    ): Int

    @Query("SELECT DISTINCT period FROM artworks WHERE period IS NOT NULL ORDER BY period")
    suspend fun getDistinctPeriods(): List<String>

    @Query("SELECT DISTINCT movement FROM artworks WHERE movement IS NOT NULL ORDER BY movement")
    suspend fun getDistinctMovements(): List<String>

    /** Bordes reales del selector de rango de años — `null` si ninguna obra tiene
     * `creationYearStart` conocido. */
    @Query("SELECT MIN(creationYearStart) FROM artworks WHERE creationYearStart IS NOT NULL")
    suspend fun getMinYear(): Int?

    @Query("SELECT MAX(creationYearStart) FROM artworks WHERE creationYearStart IS NOT NULL")
    suspend fun getMaxYear(): Int?

    /** Usado por el sync del delta.json (`INSERT OR REPLACE` por id, igual que el harvester). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(artworks: List<ArtworkEntity>)
}
