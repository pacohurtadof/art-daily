package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.ArtworkEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetNextFavoriteWallpaperUseCaseTest {

    private fun favorite(id: String) = ArtworkEntity(
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
    fun `sin favoritos, no hay nada que rotar`() = runBlocking {
        val useCase = GetNextFavoriteWallpaperUseCase(FakeFavoriteDao(), FakeWallpaperPreferences())

        assertNull(useCase())
    }

    @Test
    fun `primera llamada, sin historial previo, elige el primero de la lista`() = runBlocking {
        val favorites = FakeFavoriteDao(listOf(favorite("a"), favorite("b")))
        val prefs = FakeWallpaperPreferences()
        val useCase = GetNextFavoriteWallpaperUseCase(favorites, prefs)

        val result = useCase()

        assertEquals("a", result?.id)
        assertEquals("a", prefs.lastFavoriteArtworkId)
    }

    @Test
    fun `avanza una posicion cada vez que se llama - por diseno, sin nocion de dia`() = runBlocking {
        // Documenta a propósito el comportamiento real de esta clase (ver su propio
        // comentario: "avanza una posición cada vez que se llama... no es aleatorio"): NO
        // sabe qué día es ni cuántas veces ya se llamó hoy. Por eso la guarda diaria tiene
        // que vivir un nivel arriba (`ApplyAutomaticWallpaperUseCase`) — sin ella, cada
        // corrida extra del worker avanza la rotación de más, que fue el bug real reportado
        // por el usuario (2026-09-28, ver ese test para la regresión completa).
        val favorites = FakeFavoriteDao(listOf(favorite("a"), favorite("b"), favorite("c")))
        val prefs = FakeWallpaperPreferences()
        val useCase = GetNextFavoriteWallpaperUseCase(favorites, prefs)

        val first = useCase()
        val second = useCase()
        val third = useCase()
        val fourth = useCase() // vuelve a "a" — el ciclo es circular

        assertEquals("a", first?.id)
        assertEquals("b", second?.id)
        assertEquals("c", third?.id)
        assertEquals("a", fourth?.id)
    }

    @Test
    fun `si el ultimo aplicado ya no esta en favoritos, arranca de nuevo desde el principio`() = runBlocking {
        val favorites = FakeFavoriteDao(listOf(favorite("a"), favorite("b")))
        val prefs = FakeWallpaperPreferences().apply { lastFavoriteArtworkId = "removed-from-favorites" }
        val useCase = GetNextFavoriteWallpaperUseCase(favorites, prefs)

        val result = useCase()

        assertEquals("a", result?.id)
    }
}
