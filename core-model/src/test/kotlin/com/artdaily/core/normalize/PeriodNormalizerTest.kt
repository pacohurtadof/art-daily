package com.artdaily.core.normalize

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PeriodNormalizerTest {

    @Test
    fun `matches exact known period`() {
        assertEquals("Barroco", PeriodNormalizer.normalize("baroque"))
    }

    @Test
    fun `matches case-insensitively`() {
        assertEquals("Renacimiento", PeriodNormalizer.normalize("RENAISSANCE"))
    }

    @Test
    fun `matches by substring within a longer raw string`() {
        // ej. el `period` real del Met viene como "Edo period (1615-1868)"
        assertEquals("Periodo Edo", PeriodNormalizer.normalize("Edo period (1615-1868)"))
    }

    @Test
    fun `unknown text returns null instead of guessing`() {
        assertNull(PeriodNormalizer.normalize("some random unrelated text"))
    }

    @Test
    fun `null and blank input return null`() {
        assertNull(PeriodNormalizer.normalize(null))
        assertNull(PeriodNormalizer.normalize(""))
        assertNull(PeriodNormalizer.normalize("   "))
    }

    @Test
    fun `tries candidates in order and returns first match`() {
        assertEquals("Barroco", PeriodNormalizer.normalize(null, "unrelated", "baroque"))
    }

    @Test
    fun `returns null when no candidate matches`() {
        assertNull(PeriodNormalizer.normalize("unrelated", "also unrelated"))
    }
}
