package com.artdaily.harvester

import com.artdaily.core.model.Artwork
import java.io.File

/**
 * Excepciones manuales de **movimiento artístico**, obra por obra (2026-08-26).
 *
 * `MovementNormalizer` (core-model) solo clasifica automáticamente cuando la fuente trae un
 * campo de estilo limpio — en la práctica, casi solo AIC (`style_title`/`style_titles`).
 * Met/CMA/Rijksmuseum casi nunca lo traen, así que la enorme mayoría del catálogo queda con
 * `movement = null` aunque muchas SÍ pertenezcan a un movimiento real (Impresionismo,
 * Romanticismo, etc.) — reportado en vivo por el usuario ("elijo impresionismo, pero veo muy
 * pocas, 27 pinturas").
 *
 * Se evaluó (y se descartó a propósito, decisión del usuario) un diccionario artista→
 * movimiento automático: un mismo artista puede cambiar de movimiento a mitad de carrera
 * (ej. Matisse pasa de Fauvismo hacia 1905-1908 a un estilo posterior sin movimiento
 * definido en nuestro diccionario hacia 1920). Por eso esto es una excepción **por obra
 * puntual** (`artworkId`), no por artista — mantenida a mano en
 * `harvester/data/movement-overrides.csv`, revisando título+artista+fecha de cada obra.
 *
 * Solo se asigna un valor cuando corresponde a un movimiento que YA existe en
 * `MovementNormalizer` — Renacimiento/Barroco/Gótico/Rococó y el arte tradicional asiático
 * quedan sin movimiento a propósito (ya están cubiertos por `period`, agregarles un
 * "movimiento" los duplicaría). Movimientos reales pero todavía no soportados por el
 * diccionario (Simbolismo, Ukiyo-e, Escuela del río Hudson...) también quedan en `null` por
 * ahora, en vez de inventar una categoría nueva sin acuerdo explícito.
 */
object MovementOverrides {
    private val overridesByArtworkId: Map<String, String> by lazy { load() }

    /** Si `artwork.movement` ya vino de `MovementNormalizer`, se respeta tal cual — esto es
     * solo un complemento para cuando la fuente no trajo nada. */
    fun apply(artwork: Artwork): Artwork {
        if (artwork.movement != null) return artwork
        val override = overridesByArtworkId[artwork.id] ?: return artwork
        return artwork.copy(movement = override)
    }

    private fun load(): Map<String, String> {
        val file = File("data/movement-overrides.csv")
        if (!file.exists()) return emptyMap()
        return file.readLines()
            .drop(1) // encabezado "artworkId,movement"
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split(",", limit = 2)
                if (parts.size != 2) null else parts[0].trim() to parts[1].trim()
            }
            .toMap()
    }
}
