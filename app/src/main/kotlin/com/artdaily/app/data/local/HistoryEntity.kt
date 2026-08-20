package com.artdaily.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * `docs/etapa2-diseno-arquitectura.md`, sección 8 — sin cambios respecto al documento.
 * `widgetId = 0` significa "obra del día" de la app principal, sin widget asociado.
 */
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val entryId: Long = 0,
    val widgetId: Int,
    val artworkId: String,
    val shownAt: Long
)
