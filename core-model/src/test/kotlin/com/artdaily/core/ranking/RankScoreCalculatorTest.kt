package com.artdaily.core.ranking

import com.artdaily.core.model.Artwork
import org.junit.Assert.assertEquals
import org.junit.Test

class RankScoreCalculatorTest {

    private fun emptyArtwork() = Artwork(
        id = "test:1",
        title = "Test",
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
        classification = "other",
        museum = "Test Museum",
        museumId = "1",
        imageUrlFull = null,
        imageUrlThumbnail = null,
        sourceUrl = "https://example.com",
        sourceApi = "test",
        license = "CC0",
        isPublicDomain = true,
        description = null,
        creditLine = null,
        descriptionAttribution = null,
        dimensions = null,
        accessionNumber = null,
        museumFlaggedHighlight = false,
        rankScore = 0f,
        harvestedAt = 0L
    )

    @Test
    fun `artwork with nothing filled in scores zero`() {
        assertEquals(0f, RankScoreCalculator.calculate(emptyArtwork()), 0f)
    }

    @Test
    fun `each populated field adds exactly one point`() {
        val artwork = emptyArtwork().copy(artistName = "Someone")
        assertEquals(1f, RankScoreCalculator.calculate(artwork), 0f)
    }

    @Test
    fun `museum-flagged highlight adds three points, not one`() {
        val artwork = emptyArtwork().copy(museumFlaggedHighlight = true)
        assertEquals(3f, RankScoreCalculator.calculate(artwork), 0f)
    }

    @Test
    fun `fully populated artwork scores the maximum`() {
        val artwork = emptyArtwork().copy(
            artistName = "Someone",
            period = "Barroco",
            movement = "Impresionismo",
            description = "A description",
            creationYearStart = 1650,
            imageUrlFull = "https://example.com/image.jpg",
            museumFlaggedHighlight = true
        )
        // 6 campos x 1pt + highlight x 3pt
        assertEquals(9f, RankScoreCalculator.calculate(artwork), 0f)
    }
}
