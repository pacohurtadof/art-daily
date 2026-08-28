package com.artdaily.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla `artworks`. Debe tener EXACTAMENTE las mismas columnas (mismo nombre, mismo orden
 * no importa pero el nombre sí) que crea `ArtworkSqliteWriter` en `:harvester` — Room valida
 * el esquema de la base de datos "prepackaged" (`assets/artworks.db`) contra este `@Entity`
 * al abrirla, y si no calzan, la app truena al arrancar.
 *
 * Respecto al `ArtworkEntity` de ejemplo en `docs/etapa2-diseno-arquitectura.md` (sección 8):
 * ese tenía menos columnas (le faltaban `sourceApi`, `isPublicDomain`, `accessionNumber`,
 * `museumId`, `artistBirthYear`, `artistDeathYear`, `creationDateText`, `country`,
 * `museumFlaggedHighlight`). Se completó con el set entero de `core-model.Artwork` — ver
 * la nota ya dejada en `ArtworkSqliteWriter.kt` sobre por qué (auditoría legal, Etapa 1 sec 3).
 */
@Entity(tableName = "artworks")
data class ArtworkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artistName: String?,
    val artistBirthYear: Int?,
    val artistDeathYear: Int?,
    val creationDateText: String?,
    val creationYearStart: Int?,
    val creationYearEnd: Int?,
    val period: String?,
    val movement: String?,
    val century: Int?,
    val culture: String?,
    val country: String?,
    val classification: String,
    val museum: String,
    val museumId: String,
    val imageUrlFull: String?,
    val imageUrlThumbnail: String?,
    val sourceUrl: String,
    val sourceApi: String,
    val license: String,
    val isPublicDomain: Boolean,
    val description: String?,
    val creditLine: String?,
    val descriptionAttribution: String?,
    val dimensions: String?,
    val accessionNumber: String?,
    val museumFlaggedHighlight: Boolean,
    val rankScore: Float,
    val harvestedAt: Long,
    val isIconic: Boolean = false
)
