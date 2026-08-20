package com.artdaily.harvester.cma

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CmaMapperTest {

    private fun minimalCc0Dto() = CmaArtworkDto(
        id = 1,
        title = "Test Title",
        share_license_status = "CC0",
        images = CmaImagesDto(web = CmaImageVariantDto(url = "https://example.com/web.jpg"))
    )

    @Test
    fun `discards objects that are not explicitly CC0`() {
        val dto = minimalCc0Dto().copy(share_license_status = "")
        assertNull(CmaMapper.map(dto))
    }

    @Test
    fun `discards objects without a web image`() {
        val dto = minimalCc0Dto().copy(images = null)
        assertNull(CmaMapper.map(dto))
    }

    @Test
    fun `movement and period are always null for CMA (no clean field exists)`() {
        // Decisión de proyecto (2026-08-18): CMA no tiene campo dedicado, así que nunca se
        // intenta adivinar desde department/current_location.
        val artwork = CmaMapper.map(minimalCc0Dto())
        assertNull(artwork?.period)
        assertNull(artwork?.movement)
    }

    @Test
    fun `artist name uses the creator description as-is, unparsed`() {
        val dto = minimalCc0Dto().copy(
            creators = listOf(CmaCreatorDto(description = "Louis Hayet (French, 1864–1940)"))
        )
        // A propósito no se separa el nombre del resto — ver nota en CmaArtworkDto.
        assertEquals("Louis Hayet (French, 1864–1940)", CmaMapper.map(dto)?.artistName)
    }

    @Test
    fun `full image prefers the print variant over the web thumbnail`() {
        val dto = minimalCc0Dto().copy(
            images = CmaImagesDto(
                web = CmaImageVariantDto(url = "https://example.com/web.jpg"),
                print = CmaImageVariantDto(url = "https://example.com/print.jpg")
            )
        )
        val artwork = CmaMapper.map(dto)
        assertEquals("https://example.com/print.jpg", artwork?.imageUrlFull)
        assertEquals("https://example.com/web.jpg", artwork?.imageUrlThumbnail)
    }

    @Test
    fun `full image falls back to the web thumbnail when there is no print variant`() {
        val artwork = CmaMapper.map(minimalCc0Dto())
        assertEquals("https://example.com/web.jpg", artwork?.imageUrlFull)
    }
}
