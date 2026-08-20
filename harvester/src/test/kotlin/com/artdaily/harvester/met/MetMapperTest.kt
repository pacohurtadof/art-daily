package com.artdaily.harvester.met

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MetMapperTest {

    private fun minimalPublicDomainDto() = MetObjectDto(
        objectID = 1,
        isPublicDomain = true,
        primaryImage = "https://images.metmuseum.org/full.jpg",
        primaryImageSmall = "https://images.metmuseum.org/small.jpg",
        title = "Test Title",
        objectURL = "https://www.metmuseum.org/art/collection/search/1"
    )

    @Test
    fun `discards objects that are not public domain`() {
        val dto = minimalPublicDomainDto().copy(isPublicDomain = false)
        assertNull(MetMapper.map(dto))
    }

    @Test
    fun `discards objects without a primary image`() {
        val dto = minimalPublicDomainDto().copy(primaryImage = "")
        assertNull(MetMapper.map(dto))
    }

    @Test
    fun `maps a valid public domain object with image`() {
        val artwork = MetMapper.map(minimalPublicDomainDto())
        requireNotNull(artwork)
        assertEquals("met:1", artwork.id)
        assertEquals("Test Title", artwork.title)
        assertEquals("met", artwork.sourceApi)
        assertEquals("CC0", artwork.license)
        assertEquals(true, artwork.isPublicDomain)
    }

    @Test
    fun `artist name prefers artistDisplayName over constituents`() {
        val dto = minimalPublicDomainDto().copy(
            artistDisplayName = "Direct Name",
            constituents = listOf(MetConstituentDto(name = "Constituent Name", role = "Artist"))
        )
        assertEquals("Direct Name", MetMapper.map(dto)?.artistName)
    }

    @Test
    fun `artist name falls back to constituents when artistDisplayName is blank`() {
        val dto = minimalPublicDomainDto().copy(
            artistDisplayName = "",
            constituents = listOf(MetConstituentDto(name = "Constituent Name", role = "Artist"))
        )
        assertEquals("Constituent Name", MetMapper.map(dto)?.artistName)
    }

    @Test
    fun `artistBeginDate and artistEndDate arrive as strings and get parsed to ints`() {
        val dto = minimalPublicDomainDto().copy(artistBeginDate = "1643", artistEndDate = "1682")
        val artwork = MetMapper.map(dto)
        assertEquals(1643, artwork?.artistBirthYear)
        assertEquals(1682, artwork?.artistDeathYear)
    }

    @Test
    fun `non-numeric artistBeginDate does not crash, maps to null`() {
        val dto = minimalPublicDomainDto().copy(artistBeginDate = "active c. 1600")
        assertNull(MetMapper.map(dto)?.artistBirthYear)
    }

    @Test
    fun `century is derived from objectBeginDate`() {
        val dto = minimalPublicDomainDto().copy(objectBeginDate = 1667)
        assertEquals(17, MetMapper.map(dto)?.century)
    }

    @Test
    fun `blank title falls back to a default instead of an empty string`() {
        val dto = minimalPublicDomainDto().copy(title = "")
        assertEquals("Sin título", MetMapper.map(dto)?.title)
    }
}
