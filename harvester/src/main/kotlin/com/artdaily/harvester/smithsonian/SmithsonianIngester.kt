package com.artdaily.harvester.smithsonian

import com.artdaily.core.model.Artwork
import kotlinx.coroutines.delay

/**
 * Cosecha Smithsonian — restringida a propósito a las unidades de arte (de las 19 unidades
 * totales del Smithsonian, la mayoría no son de arte — ver `docs/bitacora.md`, 2026-09-04) y
 * a `object_type="Paintings"` (no "Prints": decisión del usuario, mismo criterio que NGA —
 * acá directamente no hay ningún campo de estilo/movimiento en absoluto, así que ampliar el
 * volumen con prints sin clasificar sería aún peor que en NGA).
 *
 * Paginación real (`start`/`rows`, máximo 1000 filas por página, verificado en vivo) — el
 * registro completo ya viene en la respuesta de `search`, no hace falta una llamada de
 * detalle por objeto (a diferencia de Met).
 */
class SmithsonianIngester(
    private val api: SmithsonianApi,
    private val apiKey: String
) {
    companion object {
        // Códigos reales verificados en vivo contra `terms/unit_code` el 2026-09-04:
        // SAAM (Arte Americano), NPG (National Portrait Gallery), NMAA (antes Freer|Sackler,
        // ahora National Museum of Asian Art), CHNDM (Cooper Hewitt, diseño — sobre todo
        // objetos de diseño, poco volumen de paintings reales), HMSG (Hirshhorn).
        private val ART_UNITS = listOf("SAAM", "NPG", "NMAA", "CHNDM", "HMSG")
        private const val PAGE_SIZE = 1000
        private const val REQUEST_DELAY_MS = 300L
    }

    suspend fun ingest(): List<Artwork> {
        val artworks = mutableListOf<Artwork>()
        for (unit in ART_UNITS) {
            val query = "unit_code:$unit AND object_type:\"Paintings\" AND online_media_type:Images"
            var start = 0
            var total = Int.MAX_VALUE
            var unitCount = 0
            while (start < total) {
                val response = try {
                    api.search(query = query, start = start, rows = PAGE_SIZE, apiKey = apiKey)
                } catch (e: Exception) {
                    System.err.println("[si] $unit start=$start -> error de red/parseo: ${e.message}")
                    break
                }
                total = response.response.rowCount
                val rows = response.response.rows
                if (rows.isEmpty()) break

                val mapped = rows.mapNotNull { SmithsonianMapper.map(it) }
                artworks += mapped
                unitCount += mapped.size
                start += rows.size
                delay(REQUEST_DELAY_MS)
            }
            println("[si] $unit: $unitCount obras mapeadas (de hasta $total candidatas con imagen)")
        }
        return artworks
    }
}
