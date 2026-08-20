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
            period = null, century = null, movement = null,
            artistName = null, museum = null, minRankScore = 0f
        ).firstOrNull()
        assertTrue("Se esperaba poder leer al menos una obra completa", any != null)
        assertTrue("isPublicDomain debería ser siempre true (filtro del mapper)", any?.isPublicDomain == true)
    }
}
