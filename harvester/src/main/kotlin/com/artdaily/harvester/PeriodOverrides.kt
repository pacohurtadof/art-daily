package com.artdaily.harvester

import com.artdaily.core.model.Artwork
import java.io.File

/**
 * Excepciones manuales de **periodo/era histórica**, obra por obra (2026-09-04) — mismo
 * patrón que [MovementOverrides], mantenido en un archivo separado (`period-overrides.csv`)
 * porque son dos filtros distintos en el modelo `Artwork` (ver `PeriodNormalizer`).
 *
 * Surgió al revisar a mano las obras de National Gallery of Art sin `movement` NI `period`
 * (2026-09-04): varias SÍ tienen periodo real y bien documentado en Wikipedia (ej. Thomas
 * Gainsborough — infobox "Movement: Rococo", que en este proyecto es periodo, no movimiento)
 * pero NGA no trajo el término `Style` correspondiente para esa obra puntual — no hay forma
 * automática de completarlo, de ahí la excepción manual.
 *
 * Igual que `MovementOverrides`: por obra puntual (`artworkId`), no por artista — un artista
 * puede abarcar más de un periodo a lo largo de su carrera.
 */
object PeriodOverrides {
    private val overridesByArtworkId: Map<String, String> by lazy { load() }

    /** Si `artwork.period` ya vino de `PeriodNormalizer`, se respeta tal cual — esto es solo
     * un complemento para cuando la fuente no trajo nada. */
    fun apply(artwork: Artwork): Artwork {
        if (artwork.period != null) return artwork
        val override = overridesByArtworkId[artwork.id] ?: return artwork
        return artwork.copy(period = override)
    }

    private fun load(): Map<String, String> {
        val file = File("data/period-overrides.csv")
        if (!file.exists()) return emptyMap()
        return file.readLines()
            .drop(1) // encabezado "artworkId,period"
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split(",", limit = 2)
                if (parts.size != 2) null else parts[0].trim() to parts[1].trim()
            }
            .toMap()
    }
}
