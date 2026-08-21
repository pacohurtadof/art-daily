package com.artdaily.core.rotation

import com.artdaily.core.model.Artwork
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FavoriteRotationTest {

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
    fun `no favorites means nothing to rotate`() {
        assertNull(FavoriteRotation.next(emptyList(), lastId = null))
    }

    @Test
    fun `first call with no previous artwork starts from the beginning of the list`() {
        val favorites = listOf(artwork("a"), artwork("b"))
        assertEquals("a", FavoriteRotation.next(favorites, lastId = null)?.id)
    }

    @Test
    fun `advances to the next artwork after the last one shown`() {
        val favorites = listOf(artwork("a"), artwork("b"), artwork("c"))
        assertEquals("c", FavoriteRotation.next(favorites, lastId = "b")?.id)
    }

    @Test
    fun `wraps around to the beginning after the last artwork in the list`() {
        val favorites = listOf(artwork("a"), artwork("b"))
        assertEquals("a", FavoriteRotation.next(favorites, lastId = "b")?.id)
    }

    @Test
    fun `restarts from the beginning when the last shown artwork was removed from favorites`() {
        // "b" ya no está en favoritos (se sacó) — indexOfFirst da -1, no debería explotar
        // ni saltarse el resto de la lista.
        val favorites = listOf(artwork("a"), artwork("c"))
        assertEquals("a", FavoriteRotation.next(favorites, lastId = "b")?.id)
    }

    @Test
    fun `a single favorite keeps rotating back to itself`() {
        val favorites = listOf(artwork("a"))
        assertEquals("a", FavoriteRotation.next(favorites, lastId = "a")?.id)
    }
}
