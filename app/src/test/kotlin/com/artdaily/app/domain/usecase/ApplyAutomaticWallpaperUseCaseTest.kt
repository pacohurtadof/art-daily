package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.ArtworkEntity
import com.artdaily.app.domain.selection.FakeArtworkRepository
import com.artdaily.app.domain.selection.FakeHistoryDao
import com.artdaily.app.domain.selection.SelectionEngine
import com.artdaily.app.wallpaper.WallpaperSource
import com.artdaily.core.model.Artwork
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Regresión directa del bug real reportado por el usuario (2026-09-28): "el wallpaper... a
 * veces se cambia por la tarde de forma aleatoria". Antes de extraer esta clase, la guarda
 * diaria vivía inline en `DailyArtworkWorker.doWork()` — un `CoroutineWorker` acoplado a
 * `Context`/`GlanceAppWidgetManager`, fuera del alcance de los tests unitarios de este
 * proyecto (no usa Robolectric). Por eso nadie pudo escribir un test que reprodujera "el
 * worker corre dos veces el mismo día" antes de que pasara de verdad en un dispositivo real.
 */
class ApplyAutomaticWallpaperUseCaseTest {

    private val zone = ZoneId.of("America/Argentina/Buenos_Aires")

    private fun artworkEntity(id: String) = ArtworkEntity(
        id = id, title = "Title $id", artistName = null, artistBirthYear = null,
        artistDeathYear = null, creationDateText = null, creationYearStart = null,
        creationYearEnd = null, period = null, movement = null, century = null,
        culture = null, country = null, classification = "painting", museum = "Test Museum",
        museumId = id, imageUrlFull = "https://example.com/$id.jpg", imageUrlThumbnail = null,
        sourceUrl = "https://example.com/$id", sourceApi = "test", license = "CC0",
        isPublicDomain = true, description = null, creditLine = null, descriptionAttribution = null,
        dimensions = null, accessionNumber = null,
        museumFlaggedHighlight = false, rankScore = 5f, harvestedAt = 0L
    )

    private fun artwork(id: String) = Artwork(
        id = id, title = "Title $id", artistName = null, artistBirthYear = null,
        artistDeathYear = null, creationDateText = null, creationYearStart = null,
        creationYearEnd = null, period = null, movement = null, century = null,
        culture = null, country = null, classification = "painting", museum = "Test Museum",
        museumId = id, imageUrlFull = "https://example.com/$id.jpg", imageUrlThumbnail = null,
        sourceUrl = "https://example.com/$id", sourceApi = "test", license = "CC0",
        isPublicDomain = true, description = null, creditLine = null, descriptionAttribution = null,
        dimensions = null, accessionNumber = null,
        museumFlaggedHighlight = false, rankScore = 5f, harvestedAt = 0L
    )

    private fun buildUseCase(
        prefs: FakeWallpaperPreferences,
        applier: FakeWallpaperApplier,
        favorites: FakeFavoriteDao = FakeFavoriteDao(listOf(artworkEntity("fav-a"), artworkEntity("fav-b")))
    ): ApplyAutomaticWallpaperUseCase {
        val repo = FakeArtworkRepository(listOf(artwork("daily-a")))
        val history = FakeHistoryDao()
        val getArtworkOfTheDay = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), FakeWidgetConfigDao(), history, repo
        )
        val getNextFavorite = GetNextFavoriteWallpaperUseCase(favorites, prefs)
        return ApplyAutomaticWallpaperUseCase(prefs, getArtworkOfTheDay, getNextFavorite, applier)
    }

    @Test
    fun `apagado en Ajustes - no hace nada y no cuenta como fallo`() = runBlocking {
        val prefs = FakeWallpaperPreferences(autoChangeEnabled = false)
        val applier = FakeWallpaperApplier()
        val useCase = buildUseCase(prefs, applier)

        val result = useCase()

        assertTrue(result)
        assertEquals(0, applier.applyCallCount)
    }

    @Test
    fun `corrida extra el mismo dia NO vuelve a avanzar la rotacion de Favoritos`() = runBlocking {
        // Este es el bug real: dos corridas del worker el mismo día (ej. la periódica de
        // medianoche + una extra por reinicio/reinstalación) con fuente Favoritos.
        val prefs = FakeWallpaperPreferences(source = WallpaperSource.FAVORITES_ROTATION)
        val applier = FakeWallpaperApplier()
        val useCase = buildUseCase(prefs, applier)
        useCase.clock = Clock.fixed(
            LocalDate.of(2026, 9, 28).atTime(0, 5).atZone(zone).toInstant(), zone
        )

        useCase() // corrida "normal" de medianoche
        useCase() // corrida "extra" la misma tarde, mismo día de calendario

        // Sin la guarda, esta segunda llamada hubiera avanzado la rotación a "fav-b" y
        // vuelto a aplicar — con la guarda, ni siquiera llama al applier la segunda vez.
        assertEquals(1, applier.applyCallCount)
        assertEquals("fav-a", applier.lastAppliedImageUrl?.substringAfterLast("/")?.removeSuffix(".jpg"))
    }

    @Test
    fun `al dia siguiente si vuelve a aplicar y avanza la rotacion`() = runBlocking {
        val prefs = FakeWallpaperPreferences(source = WallpaperSource.FAVORITES_ROTATION)
        val applier = FakeWallpaperApplier()
        val useCase = buildUseCase(prefs, applier)

        useCase.clock = Clock.fixed(
            LocalDate.of(2026, 9, 28).atTime(0, 5).atZone(zone).toInstant(), zone
        )
        useCase() // día 1: aplica "fav-a"

        useCase.clock = Clock.fixed(
            LocalDate.of(2026, 9, 29).atTime(0, 5).atZone(zone).toInstant(), zone
        )
        useCase() // día 2: SÍ debe volver a aplicar, ahora "fav-b"

        assertEquals(2, applier.applyCallCount)
    }

    @Test
    fun `bypassDailyGuard = true (accion manual desde Ajustes) aplica igual aunque ya se haya aplicado hoy`() = runBlocking {
        val prefs = FakeWallpaperPreferences(source = WallpaperSource.FAVORITES_ROTATION)
        val applier = FakeWallpaperApplier()
        val useCase = buildUseCase(prefs, applier)
        useCase.clock = Clock.fixed(
            LocalDate.of(2026, 9, 28).atTime(12, 0).atZone(zone).toInstant(), zone
        )

        useCase() // corrida automática, aplica "fav-a"
        val result = useCase(bypassDailyGuard = true) // el usuario tocó el selector en Ajustes

        assertTrue(result)
        assertEquals(2, applier.applyCallCount) // la acción manual SÍ se nota, a diferencia de una corrida extra del worker
    }

    @Test
    fun `si la aplicacion falla, no se marca como aplicado hoy - se reintenta`() = runBlocking {
        val prefs = FakeWallpaperPreferences(source = WallpaperSource.FAVORITES_ROTATION)
        val applier = FakeWallpaperApplier(nextResult = false)
        val useCase = buildUseCase(prefs, applier)
        useCase.clock = Clock.fixed(
            LocalDate.of(2026, 9, 28).atTime(0, 5).atZone(zone).toInstant(), zone
        )

        val first = useCase()
        val second = useCase()

        assertFalse(first)
        assertFalse(second)
        assertEquals(2, applier.applyCallCount) // ambas corridas lo intentaron de verdad
        assertNull(prefs.lastAutoAppliedEpochDay)
    }

    @Test
    fun `fuente Obra del dia - corrida extra el mismo dia reaplica la MISMA obra, no una distinta`() = runBlocking {
        // Contraste con el caso de Favoritos: acá GetArtworkOfTheDayUseCase ya era estable
        // por sí solo, así que aunque la guarda evite la segunda llamada, tampoco hubiera
        // habido un cambio visible sin ella — confirma por qué este bug en particular era
        // más sutil con esta fuente (la que el usuario tenía configurada).
        val prefs = FakeWallpaperPreferences(source = WallpaperSource.DAILY_ARTWORK)
        val applier = FakeWallpaperApplier()
        val useCase = buildUseCase(prefs, applier)
        useCase.clock = Clock.fixed(
            LocalDate.of(2026, 9, 28).atTime(0, 5).atZone(zone).toInstant(), zone
        )

        useCase()
        useCase()

        assertEquals(1, applier.applyCallCount) // la guarda igual evita la llamada redundante
    }

    @Test
    fun `alreadyAppliedToday - funcion pura`() {
        assertFalse(ApplyAutomaticWallpaperUseCase.alreadyAppliedToday(null, 19_000L))
        assertTrue(ApplyAutomaticWallpaperUseCase.alreadyAppliedToday(19_000L, 19_000L))
        assertFalse(ApplyAutomaticWallpaperUseCase.alreadyAppliedToday(18_999L, 19_000L))
    }
}
