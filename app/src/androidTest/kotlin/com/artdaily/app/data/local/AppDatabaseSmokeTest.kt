package com.artdaily.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Prueba de humo (corre en un emulador/dispositivo real, no en la JVM del desarrollador):
 * confirma que Room realmente puede abrir el `artworks.db` empaquetado en `assets/`
 * (generado por `:harvester`) y leer datos reales de él — el paso que NO se pudo verificar
 * solo compilando.
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseSmokeTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDatabaseFromAsset() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .createFromAsset(AppDatabase.DATABASE_NAME)
            .build()
    }

    @After
    fun closeDatabase() {
        db.close()
    }

    @Test
    fun prepackagedDatabaseHasRealArtworks() = runBlocking {
        val count = db.artworkDao().count()
        assertTrue("Se esperaban obras en la db empaquetada, había $count", count > 0)
    }

    @Test
    fun canReadASpecificArtworkWithAllItsFields() = runBlocking {
        val any = db.artworkDao().getFiltered(
            hasPeriods = false, periods = emptyList(),
            hasMovements = false, movements = emptyList(),
            artistName = null, yearFrom = null, yearTo = null, minRankScore = 0f
        ).firstOrNull()
        assertTrue("Se esperaba poder leer al menos una obra completa", any != null)
        assertTrue("isPublicDomain debería ser siempre true (filtro del mapper)", any?.isPublicDomain == true)
    }

    /**
     * Regresión del crash real encontrado en vivo el 2026-08-21 al seleccionar dos chips de
     * movimiento a la vez en Explorar: `(:hasMovements = 0 OR movement IN (:movements))` con
     * una lista de 2+ elementos generaba `SQLiteException: row value misused`. Esto NO lo
     * detecta `SelectionEngineTest`/`FakeArtworkRepository` (JVM, sin SQL real) — solo un test
     * instrumentado contra Room/SQLite real lo agarra, que es justo lo que pasó: el bug llegó
     * a probarse en vivo en el emulador antes de notarse. Usa dos movimientos reales del
     * catálogo empaquetado en vez de valores inventados, para no depender de qué movimientos
     * existan hoy.
     */
    @Test
    fun filteringByTwoOrMoreMovementsDoesNotThrow() = runBlocking {
        val movements = db.artworkDao().getDistinctMovements()
        if (movements.size < 2) return@runBlocking // catálogo de prueba sin suficiente variedad

        val selected = movements.take(2)
        val result = db.artworkDao().getFiltered(
            hasPeriods = false, periods = emptyList(),
            hasMovements = true, movements = selected,
            artistName = null, yearFrom = null, yearTo = null, minRankScore = 0f
        )
        assertTrue(
            "Todo resultado debería tener uno de los movimientos seleccionados",
            result.all { it.movement in selected }
        )

        val count = db.artworkDao().countFiltered(
            hasPeriods = false, periods = emptyList(),
            hasMovements = true, movements = selected,
            artistName = null, yearFrom = null, yearTo = null, minRankScore = 0f
        )
        assertTrue("countFiltered debería coincidir con el tamaño de getFiltered", count == result.size)
    }
}
