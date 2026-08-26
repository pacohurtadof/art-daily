package com.artdaily.app.domain.usecase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artdaily.app.data.local.AppDatabase
import com.artdaily.app.data.local.ArtworkEntity
import com.artdaily.app.data.local.HistoryEntity
import com.artdaily.app.data.repository.ArtworkRepositoryImpl
import com.artdaily.app.domain.selection.SelectionEngine
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas funcionales (Room real, a diferencia de `GetArtworkOfTheDayUseCaseTest`, que usa
 * fakes en memoria de la JVM) de dos de los tres bugs reportados por el usuario el
 * 2026-08-25:
 *
 *  1. "El fondo no cambia a medianoche" — acá se prueba la mitad que SÍ se puede verificar
 *     sin esperar un día real ni depender del reloj del sistema: que cruzar el límite de un
 *     día de calendario (una obra ya elegida "ayer" en el historial) haga que hoy se sortee
 *     una obra — y por lo tanto una imagen — DISTINTA, en vez de repetir la de ayer. La otra
 *     mitad del bug — que `DailyArtworkWorker` efectivamente CORRA a medianoche y no a
 *     cualquier otra hora — depende de la programación real de `WorkManager` y del reloj del
 *     sistema; no es razonable simularla en un test rápido y determinista, así que esa parte
 *     se verificó a mano leyendo `setInitialDelay` en `DailyArtworkWorker.schedulePeriodic`.
 *
 *  2. "La imagen del widget a veces es distinta a la de Hoy" — acá se prueba que un widget
 *     SIN filtro propio (sin fila en `widget_config`) resuelve exactamente la misma obra
 *     (mismo id, misma imagen) que "Hoy" (`widgetId = 0`). El cambio automático de fondo de
 *     pantalla con fuente "obra del día" (`DailyArtworkWorker`, rama `WallpaperSource
 *     .DAILY_ARTWORK`) llama a `getArtworkOfTheDay(widgetId = 0)` — la MISMA llamada que usa
 *     "Hoy" (ver ese archivo) — así que ese tercer caso queda cubierto por construcción por
 *     el mismo assert. A propósito NO se invoca `WallpaperApplier` real acá: de verdad
 *     cambiaría el fondo de pantalla del dispositivo/emulador donde corra este test.
 *
 * Usa Room EN MEMORIA con datos de prueba propios — a diferencia de `AppDatabaseSmokeTest`,
 * que abre el mismo archivo `artworks.db` que usa la app instalada de verdad (solo para
 * lectura). Estos tests además ESCRIBEN historial (es lo que estamos probando), así que
 * hacerlo contra la base de datos real del dispositivo contaminaría el historial real de
 * quien corra los tests en su propio teléfono — con Room en memoria, cada test arranca de
 * cero y no deja rastro.
 */
@RunWith(AndroidJUnit4::class)
class DailyArtworkFlowTest {

    private lateinit var db: AppDatabase
    private lateinit var useCase: GetArtworkOfTheDayUseCase

    private val artworkA = artworkEntity(id = "test-a", imageUrl = "https://example.com/a-thumb.jpg")
    private val artworkB = artworkEntity(id = "test-b", imageUrl = "https://example.com/b-thumb.jpg")

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        db.artworkDao().upsertAll(listOf(artworkA, artworkB))

        val repository = ArtworkRepositoryImpl(db.artworkDao())
        useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repository, db.historyDao()),
            db.widgetConfigDao(),
            db.historyDao(),
            repository
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun crossingIntoANewDayPicksADifferentArtworkAndImageThanYesterdays() = runBlocking {
        // "Ayer" (dentro de la ventana de `avoidRepeatDays` por defecto, 30 días) ya se
        // mostró `artworkA` en Hoy (widgetId 0).
        val twoDaysAgo = System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000
        db.historyDao().record(HistoryEntity(widgetId = 0, artworkId = artworkA.id, shownAt = twoDaysAgo))

        val today = useCase(widgetId = 0)

        assertNotNull("Se esperaba una obra para hoy", today)
        // Con solo 2 candidatas y `artworkA` recién excluida por anti-repetición, la única
        // que queda en el pool es `artworkB` — determinista, no depende de la suerte del azar.
        assertEquals(artworkB.id, today?.id)
        assertNotEquals("La obra de hoy no debería repetir la de ayer", artworkA.id, today?.id)
        assertNotEquals(
            "La imagen de hoy debería ser distinta a la de ayer",
            artworkA.imageUrlThumbnail, today?.imageUrlThumbnail
        )
    }

    @Test
    fun homeAndAWidgetWithoutItsOwnFilterShowTheExactSameArtworkAndImageToday() = runBlocking {
        val home = useCase(widgetId = 0)
        // Nunca se configuró ningún filtro para este widgetId (no hay fila en widget_config)
        // — el mismo escenario que un widget recién agregado sin tocar su configuración.
        val widgetWithoutOwnFilter = useCase(widgetId = 4242)

        assertNotNull(home)
        assertEquals("Debería ser la misma obra que Hoy", home?.id, widgetWithoutOwnFilter?.id)
        assertEquals(home?.imageUrlThumbnail, widgetWithoutOwnFilter?.imageUrlThumbnail)
        assertEquals(home?.imageUrlFull, widgetWithoutOwnFilter?.imageUrlFull)
    }

    private fun artworkEntity(id: String, imageUrl: String) = ArtworkEntity(
        id = id, title = "Title $id", artistName = null, artistBirthYear = null,
        artistDeathYear = null, creationDateText = null, creationYearStart = 1800,
        creationYearEnd = null, period = null, movement = null, century = null,
        culture = null, country = null, classification = "painting", museum = "Test Museum",
        museumId = id, imageUrlFull = imageUrl, imageUrlThumbnail = imageUrl,
        sourceUrl = "https://example.com/$id", sourceApi = "test", license = "CC0",
        isPublicDomain = true, description = null, creditLine = null, descriptionAttribution = null,
        dimensions = null, accessionNumber = null,
        museumFlaggedHighlight = false, rankScore = 5f, harvestedAt = 0L
    )
}
