package com.artdaily.harvester.storage

import com.artdaily.core.model.Artwork
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Genera `artworks-delta-YYYYMMDD.json` — el segundo artefacto de salida del harvester
 * (`docs/etapa2-diseno-arquitectura.md`, sección 7): solo las obras nuevas o cambiadas desde
 * la última corrida, para que la app sincronice sin tener que redistribuirse entera.
 *
 * Publicación real (subir esto a GitHub Releases / R2, con un alias ".../latest/...") queda
 * fuera del harvester en sí — es un paso de CI, no de este programa.
 */
object DeltaJsonWriter {
    private val json = Json { prettyPrint = true; encodeDefaults = true }
    private val fileDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")

    /** Devuelve el archivo escrito, o null si no había nada nuevo/cambiado que publicar. */
    fun write(newOrChanged: List<Artwork>, outputDir: String, today: LocalDate = LocalDate.now()): File? {
        if (newOrChanged.isEmpty()) return null

        val dir = File(outputDir).apply { mkdirs() }
        val file = File(dir, "artworks-delta-${today.format(fileDateFormat)}.json")
        file.writeText(json.encodeToString(newOrChanged))
        return file
    }
}
