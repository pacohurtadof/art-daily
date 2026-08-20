package com.artdaily.harvester.aic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AicMapperTest {

    private fun minimalPublicDomainDto() = AicArtworkDto(
        id = 1,
        title = "Test Title",
        is_public_domain = true,
        image_id = "abc123"
    )

    @Test
    fun `discards objects that are not public domain`() {
        val dto = minimalPublicDomainDto().copy(is_public_domain = false)
        assertNull(AicMapper.map(dto))
    }

    @Test
    fun `discards objects without an image_id`() {
        val dto = minimalPublicDomainDto().copy(image_id = null)
        assertNull(AicMapper.map(dto))
    }

    @Test
    fun `maps a valid object and builds IIIF image urls`() {
        val artwork = AicMapper.map(minimalPublicDomainDto())
        requireNotNull(artwork)
        assertEquals("aic:1", artwork.id)
        assertEquals("aic", artwork.sourceApi)
        assertEquals(
            "https://www.artic.edu/iiif/2/abc123/full/843,/0/default.jpg",
            artwork.imageUrlFull
        )
        assertEquals(
            "https://www.artic.edu/iiif/2/abc123/full/200,/0/default.jpg",
            artwork.imageUrlThumbnail
        )
    }

    @Test
    fun `movement comes from style_title when it matches`() {
        val dto = minimalPublicDomainDto().copy(style_title = "Impressionism")
        assertEquals("Impresionismo", AicMapper.map(dto)?.movement)
    }

    @Test
    fun `REGRESSION - department_title alone never produces a movement`() {
        // Bug real encontrado el 2026-08-18: department_title (categoría curatorial, no
        // un movimiento artístico) se colaba como candidato de movimiento y producía
        // asignaciones falsas. Este test existe específicamente para que no vuelva a pasar.
        val dto = minimalPublicDomainDto().copy(
            style_title = null,
            style_titles = emptyList(),
            department_title = "Modern and Contemporary Art"
        )
        assertNull(AicMapper.map(dto)?.movement)
    }

    @Test
    fun `department_title CAN still contribute to period as a last resort`() {
        // A diferencia de movimiento, para periodo sí se acepta como señal débil.
        val dto = minimalPublicDomainDto().copy(
            style_title = null,
            style_titles = emptyList(),
            department_title = "Modern Art"
        )
        assertEquals("Arte moderno", AicMapper.map(dto)?.period)
    }

    @Test
    fun `discards objects with a blank title by falling back to a default`() {
        val dto = minimalPublicDomainDto().copy(title = "")
        assertEquals("Sin título", AicMapper.map(dto)?.title)
    }
}
