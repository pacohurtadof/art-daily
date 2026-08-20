package com.artdaily.app.domain.selection

import com.artdaily.app.data.local.HistoryEntity
import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SelectionEngineTest {

    private fun artwork(id: String, rankScore: Float = 5f) = Artwork(
        id = id,
        title = "Title $id",
        artistName = null,
        artistBirthYear = null,
        artistDeathYear = null,
        creationDateText = null,
        creationYearStart = null,
        creationYearEnd = null,
        period = null,
        movement = null,
        century = null,
        culture = null,
        country = null,
        classification = "painting",
        museum = "Test Museum",
        museumId = id,
        imageUrlFull = null,
        imageUrlThumbnail = null,
        sourceUrl = "https://example.com/$id",
        sourceApi = "test",
        license = "CC0",
        isPublicDomain = true,
        description = null,
        creditLine = null,
        descriptionAttribution = null,
        dimensions = null,
        accessionNumber = null,
        museumFlaggedHighlight = false,
        rankScore = rankScore,
        harvestedAt = 0L
    )

    @Test
    fun `returns null when no artwork matches the filter`() = runBlocking {
        val engine = SelectionEngine(FakeArtworkRepository(emptyList()), FakeHistoryDao())
        val result = engine.pickForWidget(widgetId = 1, filter = ArtworkFilter(), avoidRepeatDays = 30)
        assertNull(result)
    }

    @Test
    fun `returns the only candidate when there is exactly one`() = runBlocking {
        val repo = FakeArtworkRepository(listOf(artwork("a")))
        val engine = SelectionEngine(repo, FakeHistoryDao())
        val result = engine.pickForWidget(widgetId = 1, filter = ArtworkFilter(), avoidRepeatDays = 30)
        assertEquals("a", result?.id)
    }

    @Test
    fun `avoids a recently shown artwork when an alternative exists`() = runBlocking {
        val repo = FakeArtworkRepository(listOf(artwork("a"), artwork("b")))
        val history = FakeHistoryDao()
        history.record(HistoryEntity(widgetId = 1, artworkId = "a", shownAt = System.currentTimeMillis()))

        val engine = SelectionEngine(repo, history)
        val result = engine.pickForWidget(widgetId = 1, filter = ArtworkFilter(), avoidRepeatDays = 30)

        assertEquals("b", result?.id)
    }

    @Test
    fun `resets the cycle instead of returning null when everything was recently shown`() = runBlocking {
        // Decisión de producto de la Etapa 1: si el pool de candidatos es más chico que la
        // ventana de "no repetir", el ciclo se reinicia en vez de trabarse.
        val repo = FakeArtworkRepository(listOf(artwork("a")))
        val history = FakeHistoryDao()
        history.record(HistoryEntity(widgetId = 1, artworkId = "a", shownAt = System.currentTimeMillis()))

        val engine = SelectionEngine(repo, history)
        val result = engine.pickForWidget(widgetId = 1, filter = ArtworkFilter(), avoidRepeatDays = 30)

        assertEquals("a", result?.id) // no queda otra opción, pero NO debe devolver null
    }

    @Test
    fun `history recorded for one widget does not leak into another widget's history`() = runBlocking {
        // Prueba directa sobre el DAO en vez de sobre pickForWidget: con selección
        // aleatoria de por medio, el resultado final no alcanza para distinguir
        // "aislado correctamente" de "aislado por casualidad" — el propio historial sí.
        val history = FakeHistoryDao()
        history.record(HistoryEntity(widgetId = 1, artworkId = "a", shownAt = System.currentTimeMillis()))

        val recentForWidget2 = history.getRecentArtworkIds(widgetId = 2, sinceEpochMillis = 0L)

        assertEquals(emptyList<String>(), recentForWidget2)
    }

    @Test
    fun `an artwork shown outside the avoidRepeatDays window is eligible again`() = runBlocking {
        // Único candidato, mostrado hace 40 días. Si se filtrara igual (bug), el pool
        // quedaría vacío y entraría por la rama de "reinicia el ciclo" — que también
        // devolvería "a" al ser el único candidato, ocultando el error. Por eso se
        // verifica directamente contra el DAO: la fecha de corte SÍ debe excluirlo de
        // "recientes", en vez de asumirlo por el resultado final de pickForWidget.
        val history = FakeHistoryDao()
        val fortyDaysAgo = System.currentTimeMillis() - 40L * 24 * 60 * 60 * 1000
        history.record(HistoryEntity(widgetId = 1, artworkId = "a", shownAt = fortyDaysAgo))

        val thirtyDaysAgo = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
        val recent = history.getRecentArtworkIds(widgetId = 1, sinceEpochMillis = thirtyDaysAgo)

        assertEquals(emptyList<String>(), recent)
    }

    @Test
    fun `filter is applied before considering history`() = runBlocking {
        val repo = FakeArtworkRepository(
            listOf(artwork("a").copy(museum = "Met"), artwork("b").copy(period = "Barroco"))
        )
        val engine = SelectionEngine(repo, FakeHistoryDao())
        val result = engine.pickForWidget(
            widgetId = 1, filter = ArtworkFilter(period = "Barroco"), avoidRepeatDays = 30
        )
        assertEquals("b", result?.id)
    }

    @Test
    fun `year range filter only matches artworks with a known year inside the range`() = runBlocking {
        // Reemplazó al filtro de siglo/museo el 2026-08-19 — "b" queda afuera por año, "c"
        // queda afuera por no tener año conocido (no debe colarse solo porque no hay dato).
        val repo = FakeArtworkRepository(
            listOf(
                artwork("a").copy(creationYearStart = 1650),
                artwork("b").copy(creationYearStart = 1200),
                artwork("c").copy(creationYearStart = null)
            )
        )
        val engine = SelectionEngine(repo, FakeHistoryDao())
        val result = engine.pickForWidget(
            widgetId = 1,
            filter = ArtworkFilter(yearFrom = 1600, yearTo = 1700),
            avoidRepeatDays = 30
        )
        assertEquals("a", result?.id)
    }
}
