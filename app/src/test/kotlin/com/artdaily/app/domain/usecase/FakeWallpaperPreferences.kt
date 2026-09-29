package com.artdaily.app.domain.usecase

import com.artdaily.app.data.settings.WallpaperPreferences
import com.artdaily.app.wallpaper.WallpaperSource
import com.artdaily.app.wallpaper.WallpaperTarget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Test double en memoria — mismo motivo que los otros Fakes de este paquete. Antes de
 * 2026-09-28, `WallpaperPreferences` era una clase concreta atada a `Context`/
 * `SharedPreferences`, imposible de fakear así — ver el comentario en la interfaz real. */
class FakeWallpaperPreferences(
    autoChangeEnabled: Boolean = true,
    source: WallpaperSource = WallpaperSource.DAILY_ARTWORK,
    target: WallpaperTarget = WallpaperTarget.BOTH
) : WallpaperPreferences {

    private val _autoChangeEnabled = MutableStateFlow(autoChangeEnabled)
    override val autoChangeEnabled: StateFlow<Boolean> = _autoChangeEnabled

    private val _target = MutableStateFlow(target)
    override val target: StateFlow<WallpaperTarget> = _target

    private val _source = MutableStateFlow(source)
    override val source: StateFlow<WallpaperSource> = _source

    override fun setAutoChangeEnabled(enabled: Boolean) { _autoChangeEnabled.value = enabled }
    override fun setTarget(target: WallpaperTarget) { _target.value = target }
    override fun setSource(source: WallpaperSource) { _source.value = source }

    override var lastFavoriteArtworkId: String? = null
    override var lastAutoAppliedEpochDay: Long? = null
}
