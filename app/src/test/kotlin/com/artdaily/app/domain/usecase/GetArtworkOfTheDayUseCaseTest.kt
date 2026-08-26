package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.HistoryEntity
import com.artdaily.app.data.local.WidgetConfigEntity
import com.artdaily.app.domain.selection.FakeArtworkRepository
import com.artdaily.app.domain.selection.FakeHistoryDao
import com.artdaily.app.domain.selection.SelectionEngine
import com.artdaily.core.model.Artwork
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GetArtworkOfTheDayUseCaseTest {

    private fun artwork(id: String, artistName: String? = null) = Artwork(
        id = id, title = "Title $id", artistName = artistName, artistBirthYear = null,
        artistDeathYear = null, creationDateText = null, creationYearStart = null,
        creationYearEnd = null, period = null, movement = null, century = null,
        culture = null, country = null, classification = "painting", museum = "Test Museum",
        museumId = id, imageUrlFull = null, imageUrlThumbnail = null,
        sourceUrl = "https://example.com/$id", sourceApi = "test", license = "CC0",
        isPublicDomain = true, description = null, creditLine = null, descriptionAttribution = null,
        dimensions = null, accessionNumber = null,
        museumFlaggedHighlight = false, rankScore = 5f, harvestedAt = 0L
    )

    // Un widget SIN filtro propio (`FakeWidgetConfigDao()` vacío -> `getById` devuelve null)
    // comparte la clave de historial con "Hoy" (widgetId 0) — ver el comentario en
    // `GetArtworkOfTheDayUseCase`. Por eso estos tests, aunque llamen `useCase(widgetId = 1)`,
    // verifican el historial bajo `widgetId = 0`.

    @Test
    fun `picks and records a new artwork when none was chosen today`() = runBlocking {
        val repo = FakeArtworkRepository(listOf(artwork("a")))
        val history = FakeHistoryDao()
        val useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), FakeWidgetConfigDao(), history, repo
        )

        val result = useCase(widgetId = 1)

        assertEquals("a", result?.id)
        assertEquals(listOf("a"), history.getRecentArtworkIds(widgetId = 0, sinceEpochMillis = 0L))
    }

    @Test
    fun `returns the same artwork on a second call the same day, without re-rolling`() = runBlocking {
        // Regresión del bug real: "la obra de hoy cambia cada vez que entro a la app". Con
        // dos candidatos, si volviera a sortear en la segunda llamada habría ~50% de chance
        // de fallar este test en cada corrida — con el fix, 0%, porque ni siquiera sortea.
        val repo = FakeArtworkRepository(listOf(artwork("a"), artwork("b")))
        val history = FakeHistoryDao()
        val useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), FakeWidgetConfigDao(), history, repo
        )

        val first = useCase(widgetId = 1)
        val second = useCase(widgetId = 1)

        assertEquals(first?.id, second?.id)
        // Un solo registro en el historial — la segunda llamada no volvió a grabar.
        assertEquals(1, history.getRecentArtworkIds(widgetId = 0, sinceEpochMillis = 0L).size)
    }

    @Test
    fun `picks a new artwork when the last one shown was from a previous day`() = runBlocking {
        val repo = FakeArtworkRepository(listOf(artwork("a")))
        val history = FakeHistoryDao()
        val twoDaysAgo = System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000
        history.record(HistoryEntity(widgetId = 0, artworkId = "a", shownAt = twoDaysAgo))

        val useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), FakeWidgetConfigDao(), history, repo
        )

        val result = useCase(widgetId = 1)

        assertNotNull(result)
        // Se agregó un registro nuevo de HOY, además del de hace dos días — no se reusó
        // el viejo sin más.
        assertEquals(2, history.getRecentArtworkIds(widgetId = 0, sinceEpochMillis = 0L).size)
    }

    @Test
    fun `a widget without its own filter shows the same artwork as Home`() = runBlocking {
        // Bug real reportado por el usuario (2026-08-25): "la imagen del widget a veces es
        // diferente a la de hoy, en la app". Causa: cada widget tenía su propia fila de
        // historial (por widgetId) aunque no tuviera ningún filtro propio configurado, así
        // que sorteaba su obra por separado en vez de compartir la de Home (widgetId 0).
        val repo = FakeArtworkRepository(listOf(artwork("a"), artwork("b"), artwork("c")))
        val history = FakeHistoryDao()
        val useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), FakeWidgetConfigDao(), history, repo
        )

        val home = useCase(widgetId = 0)
        val widgetWithoutFilter = useCase(widgetId = 42)

        assertEquals(home?.id, widgetWithoutFilter?.id)
    }

    @Test
    fun `a widget with its own filter keeps its own independent history`() = runBlocking {
        // Lo contrario también debe seguir siendo cierto: un widget CON filtro propio puede
        // tener un pool de candidatas distinto al de Home, así que necesita su propio sorteo
        // y su propio anti-repetición — no debe compartir la fila de historial de Home.
        val repo = FakeArtworkRepository(listOf(artwork("a", artistName = "Some Artist")))
        val history = FakeHistoryDao()
        val widgetConfigDao = FakeWidgetConfigDao().apply {
            upsert(WidgetConfigEntity(widgetId = 7, artistName = "Some Artist"))
        }
        val useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), widgetConfigDao, history, repo
        )

        useCase(widgetId = 7)

        assertEquals(0, history.getRecentArtworkIds(widgetId = 0, sinceEpochMillis = 0L).size)
        assertEquals(1, history.getRecentArtworkIds(widgetId = 7, sinceEpochMillis = 0L).size)
    }
}
