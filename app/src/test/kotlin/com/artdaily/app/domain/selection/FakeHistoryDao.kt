package com.artdaily.app.domain.selection

import com.artdaily.app.data.local.HistoryDao
import com.artdaily.app.data.local.HistoryEntity

/** Test double en memoria de [HistoryDao] — mismo motivo que [FakeArtworkRepository]. */
class FakeHistoryDao : HistoryDao {
    private val entries = mutableListOf<HistoryEntity>()

    override suspend fun record(entry: HistoryEntity) {
        entries.add(entry)
    }

    override suspend fun getRecentArtworkIds(widgetId: Int, sinceEpochMillis: Long): List<String> =
        entries.filter { it.widgetId == widgetId && it.shownAt >= sinceEpochMillis }.map { it.artworkId }

    override suspend fun getMostRecentSince(widgetId: Int, sinceEpochMillis: Long): String? =
        entries.filter { it.widgetId == widgetId && it.shownAt >= sinceEpochMillis }
            .maxByOrNull { it.shownAt }?.artworkId

    override suspend fun clearForWidget(widgetId: Int) {
        entries.removeAll { it.widgetId == widgetId }
    }
}
