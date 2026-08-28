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
        "romanticism" to "Romanticismo",
        // Agregados el 2026-08-26, al clasificar movimiento obra por obra
        // (`harvester/data/movement-overrides.csv`): movimientos reales que aparecen una y
        // otra vez en el catálogo (pintores del siglo XIX/XX de Met/CMA/Rijks) pero que el
        // diccionario original no cubría todavía — pedido explícito del usuario ("los
        // actuales no son todos los que existieron").
        //
        // "romantic" (adjetivo, no "romanticism"): confirmado en vivo contra la API real de
        // AIC — `style_titles` de dos Delacroix traía literalmente ["nineteenth century",
        // "19th century", "romantic"], nunca la forma "-ism". Sin este alias quedaban sin
        // movimiento automático (se salvaron igual por el override manual de esa obra
        // puntual, pero esto cubre cualquier otra obra con el mismo patrón).
        "romantic" to "Romanticismo",
        "symbolism" to "Simbolismo",
        "ukiyo-e" to "Ukiyo-e",
        "ukiyo-e school" to "Ukiyo-e",
        "hudson river school" to "Escuela del río Hudson",
        "luminism" to "Luminismo",
        "tonalism" to "Tonalismo",
        "barbizon" to "Escuela de Barbizon",
        "barbizon school" to "Escuela de Barbizon",
        "pre-raphaelite" to "Prerrafaelismo",
        "pre-raphaelite brotherhood" to "Prerrafaelismo",
        "nabis" to "Nabis",
        "precisionism" to "Precisionismo",
        "orientalism" to "Orientalismo",
        "ashcan school" to "Escuela Ashcan"
        // se amplía con cada fuente nueva que se incorpore
    )

    /**
     * Prueba cada candidato en orden; devuelve el primer match, o null si ninguno matchea.
     *
     * Dentro de cada candidato, si no hay match exacto, se busca por substring, pero
     * respetando límites de PALABRA (`\b...\b`) — y entre varios matches, gana el más largo
     * (más específico). Dos bugs reales encontrados el 2026-08-26 al ampliar el diccionario
     * (mismo fix en `PeriodNormalizer`, ver ahí el detalle completo):
     *  1. Un `.contains()` ingenuo (sin límites de palabra) hacía que "roman" calzara dentro
     *     de "roman**tic**" — palabras totalmente distintas.
     *  2. Con "post-impressionism" e "impressionism" ambos en el mapa, un texto que no
     *     matchea exacto pero contiene "impressionism" podía resolver al más corto en vez
     *     del más específico, según el orden de inserción del mapa.
     */
    fun normalize(vararg candidates: String?): String? {
        for (raw in candidates) {
            if (raw.isNullOrBlank()) continue
            val key = raw.trim().lowercase()
            val match = map[key]
                ?: map.entries
                    .filter { Regex("\\b${Regex.escape(it.key)}\\b").containsMatchIn(key) }
                    .maxByOrNull { it.key.length }
                    ?.value
            if (match != null) return match
        }
        return null
    }
}
