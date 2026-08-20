package com.artdaily.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** `docs/etapa2-diseno-arquitectura.md`, sección 8 — sin cambios respecto al documento. */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val artworkId: String,
    val savedAt: Long
)
