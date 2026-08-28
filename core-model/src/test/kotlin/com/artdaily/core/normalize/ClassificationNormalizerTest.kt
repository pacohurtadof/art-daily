package com.artdaily.core.normalize

import org.junit.Assert.assertEquals
import org.junit.Test

class ClassificationNormalizerTest {

    @Test
    fun `matches exact known classification`() {
        assertEquals("painting", ClassificationNormalizer.normalize("Paintings"))
    }

    @Test
    fun `unknown classification falls back to other, not null`() {
        // a diferencia de period/movement, classification es un campo no-nulo en Artwork
        assertEquals("other", ClassificationNormalizer.normalize("Some Unknown Type"))
    }

    @Test
    fun `null or blank falls back to other`() {
        assertEquals("other", ClassificationNormalizer.normalize(null))
        assertEquals("other", ClassificationNormalizer.normalize(""))
    }

    // 2026-08-27: regresión de un bug real — AIC manda el MEDIO en classification_title
    // ("oil on canvas", "etching"), no la palabra "painting"/"print". Sin estas entradas,
    // obras reales como "The Bedroom" de Van Gogh (AIC, classification_title="oil on canvas")
    // caían en "other" y quedaban afuera del catálogo. Ver docs/bitacora.md (2026-08-27).
    @Test
    fun `AIC oil-medium classification titles map to painting`() {
        assertEquals("painting", ClassificationNormalizer.normalize("oil on canvas"))
        assertEquals("painting", ClassificationNormalizer.normalize("Oil on panel"))
        assertEquals("painting", ClassificationNormalizer.normalize("oil on board"))
        assertEquals("painting", ClassificationNormalizer.normalize("tempera on panel"))
    }

    @Test
    fun `AIC printmaking technique classification titles map to print`() {
        assertEquals("print", ClassificationNormalizer.normalize("etching"))
        assertEquals("print", ClassificationNormalizer.normalize("engraving"))
        assertEquals("print", ClassificationNormalizer.normalize("drypoint"))
        assertEquals("print", ClassificationNormalizer.normalize("lithograph"))
        assertEquals("print", ClassificationNormalizer.normalize("wood engraving"))
        assertEquals("print", ClassificationNormalizer.normalize("woodblock print"))
    }
}
