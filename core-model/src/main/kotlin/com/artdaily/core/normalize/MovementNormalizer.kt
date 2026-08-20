package com.artdaily.core.normalize

/**
 * Normaliza el **movimiento artístico** (distinto del periodo/era — ver [PeriodNormalizer]).
 *
 * Met apenas tiene un campo limpio para esto (riesgo ya identificado en
 * `docs/etapa1-investigacion-apis-arte.md`, sección 7: "sin `movement` explícito en muchas
 * obras del Met"). Por eso [normalize] acepta varios textos candidatos (period, culture,
 * classification, objectName...) y prueba cada uno en orden — el primero que matchea gana.
 * Si ninguno matchea, `null`: mejor no clasificar que clasificar mal.
 */
object MovementNormalizer {
    private val map = mapOf(
        "realism" to "Realismo",
        "impressionism" to "Impresionismo",
        "post-impressionism" to "Postimpresionismo",
        "art nouveau" to "Art Nouveau",
        "expressionism" to "Expresionismo",
        "fauvism" to "Fauvismo",
        "cubism" to "Cubismo",
        "futurism" to "Futurismo",
        "dada" to "Dadaísmo",
        "surrealism" to "Surrealismo",
        "abstract expressionism" to "Expresionismo abstracto",
        "abstract art" to "Arte abstracto",
        "pop art" to "Pop art",
        "minimalism" to "Minimalismo",
        // Agregados el 2026-08-19: encontrados en vivo en `style_title`/`style_titles`
        // reales de AIC (curl contra la API, no una suposición) — Baroque/Renaissance/
        // Gothic quedan afuera a propósito, esos ya se clasifican como `period`, no
        // `movement` (ver PeriodNormalizer); agregarlos acá los duplicaría.
        "mannerism" to "Manierismo",
        "modernism" to "Modernismo",
        "neoclassicism" to "Neoclasicismo",
        "romanticism" to "Romanticismo"
        // se amplía con cada fuente nueva que se incorpore
    )

    /** Prueba cada candidato en orden; devuelve el primer match, o null si ninguno matchea. */
    fun normalize(vararg candidates: String?): String? {
        for (raw in candidates) {
            if (raw.isNullOrBlank()) continue
            val key = raw.trim().lowercase()
            val match = map[key] ?: map.entries.firstOrNull { key.contains(it.key) }?.value
            if (match != null) return match
        }
        return null
    }
}
