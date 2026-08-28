package com.artdaily.core.normalize

/**
 * Normaliza la clasificación/tipo de objeto (`classification` en Met, `classification_title`
 * en AIC) a un set canónico en inglés-minúscula, tal como se declaró en el modelo `Artwork`
 * (`docs/etapa2-diseno-arquitectura.md`, sección 2: "painting" | "sculpture" | "print" | ...).
 *
 * A diferencia de [PeriodNormalizer]/[MovementNormalizer], [normalize] no devuelve null:
 * `Artwork.classification` es un campo no-nulo, así que lo no reconocido cae en "other".
 */
object ClassificationNormalizer {
    private val map = mapOf(
        "paintings" to "painting",
        "painting" to "painting",
        "sculpture" to "sculpture",
        "sculptures" to "sculpture",
        "drawings" to "drawing",
        "drawing" to "drawing",
        "prints" to "print",
        "print" to "print",
        "photographs" to "photograph",
        "photography" to "photograph",
        "ceramics" to "ceramic",
        "textiles" to "textile",
        "costumes" to "textile",
        "glass" to "glass",
        "metalwork" to "metalwork",
        "jewelry" to "jewelry",
        "woodwork" to "woodwork",
        "furniture" to "furniture",
        "arms and armor" to "arms_and_armor",
        "books" to "book",
        "manuscripts" to "manuscript",
        "coins" to "coin",
        // 2026-08-27: bug real encontrado — AIC no manda la palabra "painting"/"print" en
        // `classification_title`, manda el MEDIO real (ej. "oil on canvas", "etching"). El
        // match por substring de abajo nunca los reconocía, así que caían en "other" y quedaban
        // afuera del catálogo (`isEligibleForCatalog` solo acepta painting/print) — de hecho
        // así se perdía "The Bedroom" de Van Gogh (AIC, classification_title="oil on canvas").
        // Términos vistos en vivo contra la API real de AIC (ver docs/bitacora.md).
        "oil on canvas" to "painting",
        "oil on panel" to "painting",
        "oil on board" to "painting",
        "tempera on panel" to "painting",
        "tempera on canvas" to "painting",
        "acrylic on canvas" to "painting",
        "etching" to "print",
        "engraving" to "print",
        "drypoint" to "print",
        "lithograph" to "print",
        "mezzotint" to "print",
        "aquatint" to "print",
        "screenprint" to "print",
        "linocut" to "print",
        "monotype" to "print",
        "chromolithograph" to "print",
        "woodcut" to "print"
        // se amplía con cada fuente nueva que se incorpore
    )

    fun normalize(raw: String?): String {
        if (raw.isNullOrBlank()) return "other"
        val key = raw.trim().lowercase()
        return map[key] ?: map.entries.firstOrNull { key.contains(it.key) }?.value ?: "other"
    }
}
