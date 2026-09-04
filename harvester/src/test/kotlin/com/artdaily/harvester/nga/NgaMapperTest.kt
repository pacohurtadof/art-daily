package com.artdaily.harvester.nga

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NgaMapperTest {

    private fun minimalRecord() = NgaRecord(
        objectId = "60",
        title = "Girl with the Red Hat",
        displayDate = "c. 1669",
        beginYear = 1664,
        endYear = 1674,
        medium = "oil on panel",
        attribution = "Johannes Vermeer",
        creditLine = "Andrew W. Mellon Collection",
        classification = "Painting",
        dimensions = "painted surface: 22.8 x 18 cm",
        accessionNum = "1937.1.53",
        wikidataId = "Q614047",
        artistBeginYear = 1632,
        artistEndYear = 1675,
        styleTerms = listOf("Baroque"),
        schoolTerms = listOf("Dutch"),
        imageIiifUrl = "https://api.nga.gov/iiif/b705a403-3496-42e8-abf5-a37e34c32198"
    )

    @Test
    fun `discards records without an image`() {
        val record = minimalRecord().copy(imageIiifUrl = "")
        assertNull(NgaMapper.map(record))
    }

    @Test
    fun `maps a real record verified live against the NGA CSV dataset on 2026-09-04`() {
        // "Girl with the Red Hat" de Vermeer, objectID 60 — descargado y comprobado en vivo
        // contra los CSV reales de github.com/NationalGalleryOfArt/opendata (ver
        // docs/bitacora.md, 2026-09-04), no un fixture inventado.
        val artwork = NgaMapper.map(minimalRecord())!!

        assertEquals("nga:60", artwork.id)
        assertEquals("Girl with the Red Hat", artwork.title)
        assertEquals("Johannes Vermeer", artwork.artistName)
        assertEquals(1632, artwork.artistBirthYear)
        assertEquals(1675, artwork.artistDeathYear)
        assertEquals(1664, artwork.creationYearStart)
        assertEquals("painting", artwork.classification)
        assertEquals("National Gallery of Art", artwork.museum)
        assertEquals("CC0", artwork.license)
        assertTrue(artwork.isPublicDomain)
        assertEquals(
            "https://api.nga.gov/iiif/b705a403-3496-42e8-abf5-a37e34c32198/full/843,/0/default.jpg",
            artwork.imageUrlFull
        )
        assertEquals(
            "https://api.nga.gov/iiif/b705a403-3496-42e8-abf5-a37e34c32198/full/200,/0/default.jpg",
            artwork.imageUrlThumbnail
        )
        assertEquals("https://www.nga.gov/collection/art-object-page.60.html", artwork.sourceUrl)
        // "Baroque" es periodo, no movimiento (ver PeriodNormalizer/MovementNormalizer) — para
        // esta obra en particular, período SÍ debería resolver aunque movimiento quede null.
        assertEquals("Barroco", artwork.period)
        assertNull(artwork.movement)
        assertNull(artwork.description) // NGA no expone reseña curatorial limpia, ver NgaMapper
    }

    @Test
    fun `School terms feed country, never movement or period`() {
        val record = minimalRecord().copy(styleTerms = emptyList(), schoolTerms = listOf("American"))
        val artwork = NgaMapper.map(record)!!
        assertEquals("American", artwork.country)
        assertNull(artwork.movement)
        assertNull(artwork.period)
    }

    @Test
    fun `Style terms in adjective form resolve to a real movement, not just period entries`() {
        val record = minimalRecord().copy(styleTerms = listOf("Impressionist"))
        assertEquals("Impresionismo", NgaMapper.map(record)?.movement)
    }

    @Test
    fun `no highlight signal exists for NGA, always false`() {
        assertFalse(NgaMapper.map(minimalRecord())!!.museumFlaggedHighlight)
    }
}
