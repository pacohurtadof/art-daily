package com.artdaily.harvester.rijks

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RijksMapperTest {

    private fun langValue(value: String, lang: String = "en") = buildJsonObject {
        put("@language", JsonPrimitive(lang))
        put("@value", JsonPrimitive(value))
    }

    private fun minimalCho(title: String = "Test Title") = buildJsonObject {
        put("id", JsonPrimitive("https://id.rijksmuseum.nl/123"))
        put("title", buildJsonObject {
            put("en", buildJsonArray { add(JsonPrimitive(title)) })
        })
    }

    private fun dto(
        cho: JsonObject = minimalCho(),
        isShownBy: String? = "https://iiif.micr.io/abc123/full/max/0/default.jpg",
        edmRights: String? = "http://creativecommons.org/publicdomain/mark/1.0/"
    ) = RijksAggregationDto(
        id = "https://id.rijksmuseum.nl/123#aggregation",
        edmRights = edmRights,
        isShownBy = isShownBy?.let { RijksWebResourceDto(id = it) },
        aggregatedCHO = cho
    )

    @Test
    fun `discards objects without an image`() {
        assertNull(RijksMapper.map(dto(isShownBy = null)))
    }

    @Test
    fun `discards objects whose rights are not public domain`() {
        assertNull(RijksMapper.map(dto(edmRights = "http://rightsstatements.org/vocab/InC/1.0/")))
    }

    @Test
    fun `discards objects with no rights info at all`() {
        assertNull(RijksMapper.map(dto(edmRights = null)))
    }

    @Test
    fun `maps CC0 rights to the CC0 license label`() {
        val artwork = RijksMapper.map(
            dto(edmRights = "https://creativecommons.org/publicdomain/zero/1.0/")
        )
        assertEquals("CC0", artwork?.license)
    }

    @Test
    fun `maps Public Domain Mark rights to its own label, not CC0`() {
        val artwork = RijksMapper.map(
            dto(edmRights = "http://creativecommons.org/publicdomain/mark/1.0/")
        )
        assertEquals("Public Domain Mark", artwork?.license)
    }

    @Test
    fun `period and movement are always null (no clean field exists)`() {
        val artwork = RijksMapper.map(dto())
        assertNull(artwork?.period)
        assertNull(artwork?.movement)
    }

    @Test
    fun `title prefers the English entry from the language map`() {
        val cho = buildJsonObject {
            put("id", JsonPrimitive("https://id.rijksmuseum.nl/123"))
            put("title", buildJsonObject {
                put("nl", buildJsonArray { add(JsonPrimitive("De stenen brug")) })
                put("en", buildJsonArray { add(JsonPrimitive("The Stone Bridge")) })
            })
        }
        assertEquals("The Stone Bridge", RijksMapper.map(dto(cho = cho))?.title)
    }

    @Test
    fun `creation year is parsed out of the display date text`() {
        val cho = buildJsonObject {
            put("id", JsonPrimitive("https://id.rijksmuseum.nl/123"))
            put("title", buildJsonObject { put("en", buildJsonArray { add(JsonPrimitive("T")) }) })
            put("created", buildJsonArray { add(langValue("c. 1638")) })
        }
        val artwork = RijksMapper.map(dto(cho = cho))
        assertEquals(1638, artwork?.creationYearStart)
        assertEquals(17, artwork?.century)
    }

    @Test
    fun `thumbnail is derived from the micr_io id in the full image url`() {
        val artwork = RijksMapper.map(dto())
        assertEquals(
            "https://iiif.micr.io/abc123/full/400,/0/default.jpg",
            artwork?.imageUrlThumbnail
        )
    }

    @Test
    fun `artist name comes from the creator's preferred label`() {
        val cho = buildJsonObject {
            put("id", JsonPrimitive("https://id.rijksmuseum.nl/123"))
            put("title", buildJsonObject { put("en", buildJsonArray { add(JsonPrimitive("T")) }) })
            put("creator", buildJsonArray {
                add(buildJsonObject {
                    put(
                        "http://www.w3.org/2004/02/skos/core#prefLabel",
                        buildJsonArray {
                            add(langValue("Rembrandt van Rijn", "nl"))
                            add(langValue("Rembrandt van Rijn", "en"))
                        }
                    )
                })
            })
        }
        assertEquals("Rembrandt van Rijn", RijksMapper.map(dto(cho = cho))?.artistName)
    }
}
