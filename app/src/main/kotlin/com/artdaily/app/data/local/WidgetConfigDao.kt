package com.artdaily.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WidgetConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(config: WidgetConfigEntity)

    @Query("SELECT * FROM widget_config WHERE widgetId = :widgetId")
    suspend fun getById(widgetId: Int): WidgetConfigEntity?

    /** El `DailyArtworkWorker` itera sobre todos los widgets configurados (sección 6). */
    @Query("SELECT * FROM widget_config")
    suspend fun getAll(): List<WidgetConfigEntity>

    /** Se llama desde `onDeleted()` del receiver cuando se borra un widget del home screen. */
    @Query("DELETE FROM widget_config WHERE widgetId = :widgetId")
    suspend fun delete(widgetId: Int)
}
