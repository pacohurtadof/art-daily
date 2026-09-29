package com.artdaily.app.data.settings

import android.content.Context
import androidx.core.content.edit
import com.artdaily.app.wallpaper.WallpaperSource
import com.artdaily.app.wallpaper.WallpaperTarget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Implementación real de [WallpaperPreferences], con `SharedPreferences`. Ver la interfaz
 * para el porqué de cada campo — separada de ella el 2026-09-28 para poder "fakearla" en
 * tests JVM (`FakeWallpaperPreferences`). */
@Singleton
class WallpaperPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
) : WallpaperPreferences {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _autoChangeEnabled = MutableStateFlow(prefs.getBoolean(KEY_AUTO_ENABLED, false))
    override val autoChangeEnabled: StateFlow<Boolean> = _autoChangeEnabled.asStateFlow()

    private val _target = MutableStateFlow(
        prefs.getString(KEY_TARGET, null)?.let { runCatching { WallpaperTarget.valueOf(it) }.getOrNull() }
            ?: WallpaperTarget.BOTH
    )
    override val target: StateFlow<WallpaperTarget> = _target.asStateFlow()

    private val _source = MutableStateFlow(
        prefs.getString(KEY_SOURCE, null)?.let { runCatching { WallpaperSource.valueOf(it) }.getOrNull() }
            ?: WallpaperSource.DAILY_ARTWORK
    )
    override val source: StateFlow<WallpaperSource> = _source.asStateFlow()

    override fun setAutoChangeEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_AUTO_ENABLED, enabled) }
        _autoChangeEnabled.value = enabled
    }

    override fun setTarget(target: WallpaperTarget) {
        prefs.edit { putString(KEY_TARGET, target.name) }
        _target.value = target
    }

    override fun setSource(source: WallpaperSource) {
        prefs.edit { putString(KEY_SOURCE, source.name) }
        _source.value = source
    }

    override var lastFavoriteArtworkId: String?
        get() = prefs.getString(KEY_LAST_FAVORITE_ID, null)
        set(value) { prefs.edit { putString(KEY_LAST_FAVORITE_ID, value) } }

    override var lastAutoAppliedEpochDay: Long?
        get() = if (prefs.contains(KEY_LAST_AUTO_APPLIED_DAY)) {
            prefs.getLong(KEY_LAST_AUTO_APPLIED_DAY, 0L)
        } else {
            null
        }
        set(value) {
            prefs.edit {
                if (value == null) remove(KEY_LAST_AUTO_APPLIED_DAY) else putLong(KEY_LAST_AUTO_APPLIED_DAY, value)
            }
        }

    private companion object {
        const val PREFS_NAME = "wallpaper_prefs"
        const val KEY_AUTO_ENABLED = "auto_change_enabled"
        const val KEY_TARGET = "target"
        const val KEY_SOURCE = "source"
        const val KEY_LAST_FAVORITE_ID = "last_favorite_artwork_id"
        const val KEY_LAST_AUTO_APPLIED_DAY = "last_auto_applied_epoch_day"
    }
}
