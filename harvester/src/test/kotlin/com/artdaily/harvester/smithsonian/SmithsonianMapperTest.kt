package com.artdaily.harvester.smithsonian

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SmithsonianMapperTest {

    // "Painted Trillium (Trillium undulatum)" de Mary Vaux Walcott — descargado y comprobado
    // en vivo contra la API real de Smithsonian el 2026-09-04 (con key real, no un fixture
    // inventado), ver docs/bitacora.md.
    private fun realRow(mediaAccess: String = "CC0") = SmithsonianRow(
        id = "ld1-1643381040022-1643381056536-0",
        title = "Painted Trillium (Trillium undulatum)",
        unitCode = "SAAM",
        content = SmithsonianContent(
            freetext = SmithsonianFreetext(
                date = listOf(SmithsonianLabelContent("Date", "1924")),
                name = listOf(
                    SmithsonianLabelContent(
                        "Artist",
                        "Mary Vaux Walcott, born Philadelphia, PA 1860-died St. Andrews, New Brunswick, Canada 1940"
                    )
                ),
                creditLine = listOf(SmithsonianLabelContent("Credit Line", "Smithsonian American Art Museum, Gift of the artist")),
                identifier = listOf(SmithsonianLabelContent("Object number", "1970.355.472")),
                objectType = listOf(SmithsonianLabelContent("Type", "Painting")),
                physicalDescription = listOf(
                    SmithsonianLabelContent("Medium", "watercolor on paper"),
                    SmithsonianLabelContent("Dimensions", "sheet: 10 1/8 x 7 in. (25.6 x 17.9 cm)")
                )
            ),
            descriptiveNonRepeating = SmithsonianDescriptiveNonRepeating(
                record_ID = "saam_1970.355.472",
                unit_code = "SAAM",
                data_source = "Smithsonian American Art Museum",
                record_link = "https://americanart.si.edu/collections/search/artwork/?id=26173",
                online_media = SmithsonianOnlineMedia(
                    media = listOf(
                        SmithsonianMedia(
                            type = "Images",
                            usage = SmithsonianUsage(access = mediaAccess),
                            content = "https://ids.si.edu/ids/deliveryService?id=SAAM-1970.355.472_2",
                            thumbnail = "https://ids.si.edu/ids/deliveryService?id=SAAM-1970.355.472_2",
                            resources = listOf(
                                SmithsonianMediaResource("High-resolution JPEG", "https://ids.si.edu/ids/download?id=SAAM-1970.355.472_2.jpg"),
                                SmithsonianMediaResource("Thumbnail Image", "https://ids.si.edu/ids/download?id=SAAM-1970.355.472_2_thumb")
                            )
                        )
                    )
                )
            ),
            indexedStructured = SmithsonianIndexedStructured(date = listOf("1920s"))
        )
    )

    @Test
    fun `maps a real record verified live against the Smithsonian API on 2026-09-04`() {
        val artwork = SmithsonianMapper.map(realRow())!!

        assertEquals("si:saam_1970.355.472", artwork.id)
        assertEquals("Painted Trillium (Trillium undulatum)", artwork.title)
        assertEquals("Mary Vaux Walcott", artwork.artistName)
        assertEquals(1860, artwork.artistBirthYear)
        assertEquals(1940, artwork.artistDeathYear)
        assertEquals(1924, artwork.creationYearStart)
        assertEquals("painting", artwork.classification)
        assertEquals("Smithsonian American Art Museum", artwork.museum)
        assertEquals("https://ids.si.edu/ids/download?id=SAAM-1970.355.472_2.jpg", artwork.imageUrlFull)
        assertEquals("https://ids.si.edu/ids/download?id=SAAM-1970.355.472_2_thumb", artwork.imageUrlThumbnail)
        assertEquals("CC0", artwork.license)
        assertTrue(artwork.isPublicDomain)
    }

    @Test
    fun `movement and period are always null (no equivalent field exists)`() {
        val artwork = SmithsonianMapper.map(realRow())!!
        assertNull(artwork.movement)
        assertNull(artwork.period)
    }

    @Test
    fun `discards records whose only image is not CC0`() {
        assertNull(SmithsonianMapper.map(realRow(mediaAccess = "Usage conditions apply")))
    }

    @Test
    fun `falls back to the decade bucket when the free-text date has no clear year`() {
        val row = realRow().let {
            it.copy(content = it.content!!.copy(freetext = it.content.freetext!!.copy(date = emptyList())))
        }
        assertEquals(1920, SmithsonianMapper.map(row)?.creationYearStart)
    }

    @Test
    fun `keeps the raw artist text when the born-died pattern does not match`() {
        val row = realRow().let {
            it.copy(
                content = it.content!!.copy(
                    freetext = it.content.freetext!!.copy(
                        name = listOf(SmithsonianLabelContent("Artist", "Unidentified"))
                    )
                )
            )
        }
        val artwork = SmithsonianMapper.map(row)!!
        assertEquals("Unidentified", artwork.artistName)
        assertNull(artwork.artistBirthYear)
    }
}
