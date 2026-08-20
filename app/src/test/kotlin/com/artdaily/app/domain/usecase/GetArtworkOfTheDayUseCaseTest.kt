package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.HistoryEntity
import com.artdaily.app.domain.selection.FakeArtworkRepository
import com.artdaily.app.domain.selection.FakeHistoryDao
import com.artdaily.app.domain.selection.SelectionEngine
import com.artdaily.core.model.Artwork
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GetArtworkOfTheDayUseCaseTest {

    private fun artwork(id: String) = Artwork(
        id = id, title = "Title $id", artistName = null, artistBirthYear = null,
        artistDeathYear = null, creationDateText = null, creationYearStart = null,
        creationYearEnd = null, period = null, movement = null, century = null,
        culture = null, country = null, classification = "painting", museum = "Test Museum",
        museumId = id, imageUrlFull = null, imageUrlThumbnail = null,
        sourceUrl = "https://example.com/$id", sourceApi = "test", license = "CC0",
        isPublicDomain = true, description = null, creditLine = null, descriptionAttribution = null,
        dimensions = null, accessionNumber = null,
        museumFlaggedHighlight = false, rankScore = 5f, harvestedAt = 0L
    )

    @Test
    fun `picks and records a new artwork when none was chosen today`() = runBlocking {
        val repo = FakeArtworkRepository(listOf(artwork("a")))
        val history = FakeHistoryDao()
        val useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), FakeWidgetConfigDao(), history, repo
        )

        val result = useCase(widgetId = 1)

        assertEquals("a", result?.id)
        assertEquals(listOf("a"), history.getRecentArtworkIds(widgetId = 1, sinceEpochMillis = 0L))
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
        assertEquals(1, history.getRecentArtworkIds(widgetId = 1, sinceEpochMillis = 0L).size)
    }

    @Test
    fun `picks a new artwork when the last one shown was from a previous day`() = runBlocking {
        val repo = FakeArtworkRepository(listOf(artwork("a")))
        val history = FakeHistoryDao()
        val twoDaysAgo = System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000
        history.record(HistoryEntity(widgetId = 1, artworkId = "a", shownAt = twoDaysAgo))

        val useCase = GetArtworkOfTheDayUseCase(
            SelectionEngine(repo, history), FakeWidgetConfigDao(), history, repo
        )

        val result = useCase(widgetId = 1)

        assertNotNull(result)
        // Se agregó un registro nuevo de HOY, además del de hace dos días — no se reusó
        // el viejo sin más.
        assertEquals(2, history.getRecentArtworkIds(widgetId = 1, sinceEpochMillis = 0L).size)
    }
}
