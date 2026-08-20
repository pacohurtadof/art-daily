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
}
