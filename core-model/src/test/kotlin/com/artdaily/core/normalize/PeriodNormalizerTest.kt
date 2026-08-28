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

    @Test
    fun `does not match a word that only contains a key as a substring`() {
        // Regresión de un bug real encontrado el 2026-08-26 (verificado en vivo contra la
        // API real de AIC): dos Delacroix con `style_titles` conteniendo literalmente
        // "romantic" quedaban con period="Antigua Roma", porque un `.contains()` ingenuo
        // hacía que "roman" calzara dentro de "roman**tic**" — palabras distintas. Con
        // límites de palabra, "roman" ya no matchea ahí; correctamente queda sin periodo
        // ("romantic" es pista de un *movimiento*, no de un periodo — ver MovementNormalizer).
        assertNull(PeriodNormalizer.normalize("romantic"))
        assertNull(PeriodNormalizer.normalize("nineteenth century, 19th century, romantic"))
    }

    @Test
    fun `still matches roman as its own standalone word`() {
        assertEquals("Antigua Roma", PeriodNormalizer.normalize("Ancient Roman sculpture"))
    }
}
