package com.artdaily.app.domain.usecase

import com.artdaily.app.data.local.WidgetConfigDao
import com.artdaily.app.data.local.WidgetConfigEntity

/** Test double en memoria — mismo motivo que los Fakes de `domain.selection`. */
class FakeWidgetConfigDao : WidgetConfigDao {
    private val configs = mutableMapOf<Int, WidgetConfigEntity>()

    override suspend fun upsert(config: WidgetConfigEntity) {
        configs[config.widgetId] = config
    }

    override suspend fun getById(widgetId: Int): WidgetConfigEntity? = configs[widgetId]

    override suspend fun getAll(): List<WidgetConfigEntity> = configs.values.toList()

    override suspend fun delete(widgetId: Int) {
        configs.remove(widgetId)
    }
}
