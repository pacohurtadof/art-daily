package com.artdaily.harvester

import com.artdaily.core.model.Artwork
import java.io.File

/**
 * Curaduría manual de obras **verdaderamente conocidas/icónicas**, obra por obra
 * (2026-08-28, pedido del usuario: "priorizar obras más conocidas para que no se aburra
 * de ver obras que no conoce").
 *
 * Ninguna de las 4 fuentes trae una señal de fama real (`rankScore` mide completitud de
 * metadatos, no reconocimiento — ver `RankScoreCalculator`; `museumFlaggedHighlight` solo
 * lo pone el Met, y significa "destacada en la colección del Met", no "mundialmente
 * famosa"). Se evaluó una heurística automática vía Wikidata/Wikipedia (¿tiene la obra su
 * propio artículo?) pero el cruce por título/artista es impreciso — el usuario eligió a
 * propósito la curaduría manual, mismo patrón que `MovementOverrides`, por precisión: cero
 * falsos positivos, aunque la cobertura sea de cientos de obras y no de miles.
 *
 * Formato del archivo: un `artworkId` por línea (no hace falta un valor asociado, a
 * diferencia de `movement-overrides.csv` — acá la presencia en el archivo YA es el dato).
 * Todo lo que sigue a un `#` se trata como comentario (tanto líneas enteras de comentario
 * como el resto de una línea después del id, ej. `aic:28560   # The Bedroom` — así queda
 * documentado qué es cada obra sin un segundo archivo) y se descarta antes de comparar.
 */
object IconicOverrides {
    private val iconicIds: Set<String> by lazy { load() }

    fun apply(artwork: Artwork): Artwork {
        if (artwork.id !in iconicIds) return artwork
        return artwork.copy(isIconic = true)
    }

    private fun load(): Set<String> {
        val file = File("data/iconic-overrides.txt")
        if (!file.exists()) return emptySet()
        return file.readLines()
            .map { it.substringBefore('#').trim() }
            .filter { it.isNotBlank() }
            .toSet()
    }
}
