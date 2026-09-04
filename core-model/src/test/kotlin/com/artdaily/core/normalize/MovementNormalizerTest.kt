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

    @Test
    fun `matches movements found live in AIC style_title data on 2026-08-19`() {
        // Encontrados con una consulta real a la API de AIC (no una suposición) al
        // investigar por qué el catálogo tenía tan pocas obras con movimiento asignado.
        assertEquals("Manierismo", MovementNormalizer.normalize("Mannerism"))
        assertEquals("Modernismo", MovementNormalizer.normalize("Modernism"))
        assertEquals("Neoclasicismo", MovementNormalizer.normalize("Neoclassicism"))
        assertEquals("Romanticismo", MovementNormalizer.normalize("Romanticism"))
    }

    @Test
    fun `period-only labels still do not match, even after the 2026-08-19 additions`() {
        // Baroque/Renaissance/Gothic se clasifican como `period` (PeriodNormalizer), no
        // `movement` — a propósito no están en este diccionario, agregarlos duplicaría
        // la clasificación.
        assertNull(MovementNormalizer.normalize("Baroque"))
        assertNull(MovementNormalizer.normalize("Renaissance"))
        assertNull(MovementNormalizer.normalize("Gothic (medieval)"))
    }

    @Test
    fun `matches movements added on 2026-08-26 while classifying movement work by work`() {
        assertEquals("Simbolismo", MovementNormalizer.normalize("Symbolism"))
        assertEquals("Ukiyo-e", MovementNormalizer.normalize("Ukiyo-e"))
        assertEquals("Escuela del río Hudson", MovementNormalizer.normalize("Hudson River School"))
        assertEquals("Luminismo", MovementNormalizer.normalize("Luminism"))
        assertEquals("Tonalismo", MovementNormalizer.normalize("Tonalism"))
        assertEquals("Escuela de Barbizon", MovementNormalizer.normalize("Barbizon"))
        assertEquals("Prerrafaelismo", MovementNormalizer.normalize("Pre-Raphaelite"))
        assertEquals("Nabis", MovementNormalizer.normalize("Nabis"))
        assertEquals("Precisionismo", MovementNormalizer.normalize("Precisionism"))
        assertEquals("Orientalismo", MovementNormalizer.normalize("Orientalism"))
        assertEquals("Escuela Ashcan", MovementNormalizer.normalize("Ashcan School"))
    }

    @Test
    fun `a longer more specific key wins over a shorter one it contains`() {
        // Regresión del bug real encontrado el 2026-08-26 al ampliar el diccionario: antes,
        // el primer match del mapa (por orden de inserción) ganaba, no el más específico —
        // con "impressionism" y "post-impressionism" ambos definidos, un texto que no
        // matchea exacto pero contiene "impressionism" como substring de una variante de
        // "post-impressionism" debía resolver al más largo/específico, no al más corto.
        assertEquals("Postimpresionismo", MovementNormalizer.normalize("Post-Impressionism style"))
        assertEquals("Impresionismo", MovementNormalizer.normalize("French Impressionism style"))
    }

    @Test
    fun `matches the adjective form 'romantic', not just 'romanticism'`() {
        // Verificado en vivo contra la API real de AIC (2026-08-26): dos Delacroix traían
        // `style_titles` con "romantic" a secas, nunca "romanticism" — sin este alias
        // quedaban sin movimiento automático (`PeriodNormalizerTest` tiene la otra mitad de
        // esta regresión: antes esto además producía un `period` incorrecto).
        assertEquals("Romanticismo", MovementNormalizer.normalize("romantic"))
        assertEquals("Romanticismo", MovementNormalizer.normalize("nineteenth century, 19th century, romantic"))
    }

    @Test
    fun `matches the adjective -ist forms found live in NGA Style terms on 2026-09-04`() {
        // NGA (`objects_terms.csv`, termType="Style") da casi siempre la forma "-ist"/"-ive",
        // no "-ism" — descargado y contado en vivo, no una suposición. Sin estos alias,
        // ninguna obra de esta fuente hubiera matcheado el diccionario ya existente.
        assertEquals("Impresionismo", MovementNormalizer.normalize("Impressionist"))
        assertEquals("Postimpresionismo", MovementNormalizer.normalize("Post-Impressionist"))
        assertEquals("Realismo", MovementNormalizer.normalize("Realist"))
        assertEquals("Expresionismo", MovementNormalizer.normalize("Expressionist"))
        assertEquals("Expresionismo abstracto", MovementNormalizer.normalize("Abstract Expressionist"))
        assertEquals("Expresionismo", MovementNormalizer.normalize("German Expressionist"))
        assertEquals("Surrealismo", MovementNormalizer.normalize("Surrealist"))
        assertEquals("Cubismo", MovementNormalizer.normalize("Cubist"))
        assertEquals("Simbolismo", MovementNormalizer.normalize("Symbolist"))
        assertEquals("Fauvismo", MovementNormalizer.normalize("Fauve"))
        assertEquals("Futurismo", MovementNormalizer.normalize("Futurist"))
        assertEquals("Modernismo", MovementNormalizer.normalize("Modernist"))
        assertEquals("Tonalismo", MovementNormalizer.normalize("Tonalist"))
        assertEquals("Minimalismo", MovementNormalizer.normalize("Minimalist"))
        assertEquals("Neoclasicismo", MovementNormalizer.normalize("Neoclassic"))
        assertEquals("Pop art", MovementNormalizer.normalize("Pop"))
        assertEquals("Arte naïf", MovementNormalizer.normalize("Naive"))
        assertEquals("Neoimpresionismo", MovementNormalizer.normalize("Neo-Impressionist"))
    }

    @Test
    fun `Neo-Impressionism is kept distinct from Impressionism, not merged into it`() {
        // Movimiento art-históricamente distinto (puntillismo/divisionismo, post-1885,
        // Seurat/Signac) — reusar "Impresionismo" hubiera sido una clasificación incorrecta.
        assertEquals("Neoimpresionismo", MovementNormalizer.normalize("Neo-Impressionism"))
    }

    @Test
    fun `matches School of Paris, added while classifying NGA works by hand on 2026-09-04`() {
        // Verificado en vivo contra el infobox de Wikipedia de Modigliani ("Movement: School
        // of Paris") — término real aunque más amplio que el resto del diccionario.
        assertEquals("Escuela de París", MovementNormalizer.normalize("School of Paris"))
        assertEquals("Escuela de París", MovementNormalizer.normalize("École de Paris"))
    }
}
