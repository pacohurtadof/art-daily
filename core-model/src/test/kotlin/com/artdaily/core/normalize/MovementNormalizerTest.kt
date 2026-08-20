package com.artdaily.core.normalize

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovementNormalizerTest {

    @Test
    fun `matches exact known movement`() {
        assertEquals("Impresionismo", MovementNormalizer.normalize("Impressionism"))
    }

    @Test
    fun `matches longer museum style label containing the keyword`() {
        // ej. AIC a veces da "American Impressionism" en vez de "Impressionism" pelado
        assertEquals("Impresionismo", MovementNormalizer.normalize("American Impressionism"))
    }

    @Test
    fun `unrelated curatorial text does not produce a false match`() {
        // esta es la regresión del bug real de esta sesión: un departamento/categoría de
        // colección NUNCA debería producir un movimiento — antes del fix, este tipo de
        // texto sí se colaba como candidato en AicMapper.
        assertNull(MovementNormalizer.normalize("Arts of the Americas"))
        assertNull(MovementNormalizer.normalize("Modern and Contemporary Art"))
    }

    @Test
    fun `null and blank candidates are skipped, not matched`() {
        assertNull(MovementNormalizer.normalize(null, "", "   "))
    }

    @Test
    fun `first matching candidate wins over later ones`() {
        assertEquals("Cubismo", MovementNormalizer.normalize("Cubism", "Impressionism"))
    }
}
