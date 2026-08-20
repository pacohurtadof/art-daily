package com.artdaily.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * `docs/etapa2-diseno-arquitectura.md`, sección 8 — sin cambios respecto al documento.
 * Cada instancia de widget (`widgetId`/`GlanceId`) tiene su propia fila, con filtros
 * completamente independientes de los demás widgets.
 *
 * `museum`/`century` reemplazados por `yearFrom`/`yearTo` el 2026-08-19 — ver
 * `ArtworkFilter` en core-model para el porqué.
 */
@Entity(tableName = "widget_config")
data class WidgetConfigEntity(
    @PrimaryKey val widgetId: Int,
    val period: String? = null,
    val movement: String? = null,
    val artistName: String? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val avoidRepeatDays: Int = 30
)
