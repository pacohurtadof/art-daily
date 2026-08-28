package com.artdaily.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * `exportSchema = true` (default) escribe un JSON del esquema en `app/schemas/` en cada
 * build — se versiona junto al código para poder escribir migraciones reales si el esquema
 * cambia más adelante, en vez de improvisar.
 */
@Database(
    entities = [
        ArtworkEntity::class,
        FavoriteEntity::class,
        HistoryEntity::class,
        WidgetConfigEntity::class
    ],
    // v2 (2026-08-19): se agregaron `creditLine`/`descriptionAttribution` a `artworks`
    // (ver ArtworkSqliteWriter.kt / ArtworkEntity.kt).
    // v3 (2026-08-19): `widget_config` cambió `museum`/`century` por `yearFrom`/`yearTo`
    // (ver ArtworkFilter.kt / WidgetConfigEntity.kt). Sin migración escrita a propósito en
    // ninguno de los dos casos, ver `fallbackToDestructiveMigration` en DatabaseModule.
    // v4 (2026-08-28): `artworks` agregó `isIconic` (curaduría manual de obras conocidas,
    // ver ArtworkEntity.kt / SelectionEngine.kt). Tampoco tiene migración escrita.
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artworkDao(): ArtworkDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
    abstract fun widgetConfigDao(): WidgetConfigDao

    companion object {
        const val DATABASE_NAME = "artworks.db"
    }
}
