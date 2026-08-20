package com.artdaily.app.widget

import com.artdaily.core.model.Artwork
import kotlinx.serialization.Serializable

/**
 * Lo mínimo que el widget necesita pintar — se guarda como JSON en el estado de Glance de
 * cada instancia (`PreferencesGlanceStateDefinition`). Sin imagen todavía (ver nota en
 * `DailyArtworkWorker`).
 */
@Serializable
data class WidgetArtworkState(
    val artworkId: String,
    val title: String,
    val artistName: String?,
    val museum: String,
    val dateText: String?,
    // Ruta a un archivo local (PNG) descargado por el Worker — Preferences (el estado de
    // Glance) solo admite tipos primitivos/String, no un Bitmap directo.
    val imageFilePath: String? = null
)

fun Artwork.toWidgetState(): WidgetArtworkState = WidgetArtworkState(
    artworkId = id,
    title = title,
    artistName = artistName,
    museum = museum,
    dateText = creationDateText
)
