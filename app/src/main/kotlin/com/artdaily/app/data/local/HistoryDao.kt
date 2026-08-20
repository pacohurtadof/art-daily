package com.artdaily.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {

    @Insert
    suspend fun record(entry: HistoryEntity)

    /**
     * Ids mostrados a este widget (o `widgetId = 0` para la app principal) en los últimos
     * [sinceDays] días — el `SelectionEngine` los usa para no repetir obra (sección 9 del
     * documento de diseño).
     */
    @Query(
        """
        SELECT artworkId FROM history
        WHERE widgetId = :widgetId
        AND shownAt >= :sinceEpochMillis
        """
    )
    suspend fun getRecentArtworkIds(widgetId: Int, sinceEpochMillis: Long): List<String>

    /** La obra ya elegida hoy para este widget, si la hay — `GetArtworkOfTheDayUseCase` la
     * usa para NO volver a sortear en cada apertura de la app (bug real: sin esto, la "obra
     * del día" cambiaba cada vez que se abría la app, no solo una vez al día). */
    @Query(
        """
        SELECT artworkId FROM history
        WHERE widgetId = :widgetId
        AND shownAt >= :sinceEpochMillis
        ORDER BY shownAt DESC
        LIMIT 1
        """
    )
    suspend fun getMostRecentSince(widgetId: Int, sinceEpochMillis: Long): String?

    @Query("DELETE FROM history WHERE widgetId = :widgetId")
    suspend fun clearForWidget(widgetId: Int)
}
