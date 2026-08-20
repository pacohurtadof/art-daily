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
        "neoclassicism" to "Neoclasicismo",
        "romanticism" to "Romanticismo",
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
        "ming dynasty" to "Dinastía Ming",
        "qing dynasty" to "Dinastía Qing",
        "modern" to "Arte moderno",
        "contemporary" to "Arte contemporáneo"
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
        // si no hay match, se deja null — mejor no clasificar que clasificar mal
    }
}
