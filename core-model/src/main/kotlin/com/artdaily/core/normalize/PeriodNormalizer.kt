package com.artdaily.core.normalize

/**
 * Normaliza el **periodo/era histórica** (no el movimiento artístico — ver [MovementNormalizer])
 * a partir del texto libre que entrega cada fuente (`period`, `culture`, `dynasty`...).
 *
 * Diccionario mantenido a mano, tal como se acordó en `docs/etapa2-diseno-arquitectura.md`
 * (sección 5): si un valor no matchea, se deja `null` en vez de clasificarlo mal.
 *
 * Nota respecto al documento original: ahí este mismo diccionario incluía entradas que en
 * realidad son movimientos artísticos (Impresionismo, Cubismo, Surrealismo...). Se movieron
 * a [MovementNormalizer] porque "periodo" y "movimiento" son dos filtros distintos en el
 * modelo `Artwork` y mezclarlos en un solo diccionario los hacía redundantes.
 *
 * [normalize] acepta varios candidatos (igual que [MovementNormalizer]) porque AIC no trae
 * un campo "periodo" limpio — se prueba contra `style_title`/`style_titles`/`department_title`.
 */
object PeriodNormalizer {
    // claves en minúsculas, tal como aparecen "en bruto" en cada fuente
    private val map = mapOf(
        "renaissance" to "Renacimiento",
        "italian renaissance" to "Renacimiento",
        "northern renaissance" to "Renacimiento",
        "baroque" to "Barroco",
        "dutch golden age" to "Barroco",
        "rococo" to "Rococó",
        "medieval" to "Medieval",
        "middle ages" to "Medieval",
        "gothic" to "Gótico",
        "byzantine" to "Bizantino",
        "ancient egypt" to "Antiguo Egipto",
        "ancient greece" to "Antigua Grecia",
        "ancient rome" to "Antigua Roma",
        "roman" to "Antigua Roma",
        "edo period" to "Periodo Edo",
        "meiji period" to "Periodo Meiji",
        "azuchi-momoyama period" to "Periodo Azuchi-Momoyama",
        "momoyama period" to "Periodo Azuchi-Momoyama",
        "muromachi period" to "Periodo Muromachi",
        "song dynasty" to "Dinastía Song",
        "yuan dynasty" to "Dinastía Yuan",
        "ming dynasty" to "Dinastía Ming",
        "qing dynasty" to "Dinastía Qing",
        "modern" to "Arte moderno",
        "contemporary" to "Arte contemporáneo"
        // se amplía con cada fuente nueva que se incorpore
    )

    /**
     * Prueba cada candidato en orden; devuelve el primer match, o null si ninguno matchea.
     *
     * Dentro de cada candidato, si no hay match exacto, se busca por substring, pero
     * respetando límites de PALABRA (`\b...\b`) — y entre varios matches, gana el más largo
     * (más específico). Bug real encontrado el 2026-08-26, clasificando movimiento obra por
     * obra: dos Delacroix con `style_titles = ["nineteenth century", "19th century",
     * "romantic"]` (verificado en vivo contra la API real de AIC) quedaban con
     * `period = "Antigua Roma"`, porque el "roman" de esta lista SÍ es substring de
     * "roman**tic**" con un `.contains()` ingenuo, aunque son palabras distintas. Con límites
     * de palabra, "roman" ya no matchea dentro de "romantic" — queda `null` (correcto:
     * "romantic" no es un periodo/era en este diccionario, es pista de un *movimiento*,
     * ver `MovementNormalizer.romantic`).
     *
     * De paso se sacaron `neoclassicism`/`romanticism` de este diccionario (estaban
     * duplicados con `MovementNormalizer` — el comentario de la clase ya decía que periodo y
     * movimiento no debían mezclarse, pero estos dos quedaron en ambos por descuido).
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
        // si no hay match, se deja null — mejor no clasificar que clasificar mal
    }
}
