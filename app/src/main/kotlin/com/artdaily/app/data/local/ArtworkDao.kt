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
     *
     * `periods`/`movements` pasaron de un solo valor a listas el 2026-08-21 (multi-selección
     * en Explorar). El patrón `:param IS NULL OR columna IN (:param)` que funciona para un
     * valor escalar NO sirve acá: Room expande una lista a tantos placeholders como elementos
     * tenga en TODAS sus apariciones del SQL, así que con 2+ elementos seleccionados
     * `:movements IS NULL` termina siendo literalmente `?,? IS NULL` — una comparación de
     * tupla que SQLite rechaza ("row value misused"). Bug real, encontrado en vivo probando
     * en un emulador al tocar dos chips de movimiento a la vez (crasheaba la app). Por eso acá
     * se usa un booleano aparte (`hasPeriods`/`hasMovements`) para decidir si se filtra,
     * en vez de comparar la lista misma contra NULL — la lista siempre llega no-nula (vacía
     * cuando no se filtra).
     *
     * `classification IN ('painting', 'print')` es una regla fija (2026-08-21, pedido del
     * usuario: "veo muchas fotos de esculturas") — no un filtro que el usuario elige, por eso
     * no es un parámetro. Excluye esculturas/cerámica/joyería/etc. de Hoy, Explorar y el
     * widget. Favoritos no pasa por acá (lee por id directo vía `getById`), así que una obra
     * no-pintura ya guardada de antes sigue viéndose ahí.
     *
     * `ORDER BY RANDOM()` (2026-08-26): sin esto, SQLite devuelve las filas en su orden físico
     * (básicamente el orden en que se insertaron) — como el catálogo se fue armando en varias
     * cosechas separadas, un `LIMIT` en Kotlin (`ExploreViewModel.MAX_RESULTS`) sobre una lista
     * sin ordenar terminaba mostrando siempre el mismo bloque de obras de las primeras
     * fuentes cosechadas, cero de las agregadas después — bug real reportado por el usuario
     * ("sigo viendo muy pocos", con un catálogo de 10.000+ obras). `SelectionEngine` no se ve
     * afectado por este orden: ya hace su propio `.randomOrNull()` sobre lo que devuelve acá.
     */
    @Query(
        """
        SELECT * FROM artworks
        WHERE (:hasPeriods = 0 OR period IN (:periods))
        AND (:hasMovements = 0 OR movement IN (:movements))
        AND (:artistName IS NULL OR artistName = :artistName)
        AND (:yearFrom IS NULL OR creationYearStart >= :yearFrom)
        AND (:yearTo IS NULL OR creationYearStart <= :yearTo)
        AND classification IN ('painting', 'print')
        AND rankScore >= :minRankScore
        ORDER BY RANDOM()
        """
    )
    suspend fun getFiltered(
        hasPeriods: Boolean,
        periods: List<String>,
        hasMovements: Boolean,
        movements: List<String>,
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
        WHERE (:hasPeriods = 0 OR period IN (:periods))
        AND (:hasMovements = 0 OR movement IN (:movements))
        AND (:artistName IS NULL OR artistName = :artistName)
        AND (:yearFrom IS NULL OR creationYearStart >= :yearFrom)
        AND (:yearTo IS NULL OR creationYearStart <= :yearTo)
        AND classification IN ('painting', 'print')
        AND rankScore >= :minRankScore
        """
    )
    suspend fun countFiltered(
        hasPeriods: Boolean,
        periods: List<String>,
        hasMovements: Boolean,
        movements: List<String>,
        artistName: String?,
        yearFrom: Int?,
        yearTo: Int?,
        minRankScore: Float
    ): Int

    @Query("SELECT DISTINCT period FROM artworks WHERE period IS NOT NULL ORDER BY period")
    suspend fun getDistinctPeriods(): List<String>

    @Query("SELECT DISTINCT movement FROM artworks WHERE movement IS NOT NULL ORDER BY movement")
    suspend fun getDistinctMovements(): List<String>

    /** Usado por el sync del delta.json (`INSERT OR REPLACE` por id, igual que el harvester). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(artworks: List<ArtworkEntity>)
}
