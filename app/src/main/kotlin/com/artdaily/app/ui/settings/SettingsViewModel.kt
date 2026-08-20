package com.artdaily.app.ui.settings

import androidx.lifecycle.ViewModel
import com.artdaily.app.data.settings.WallpaperPreferences
import com.artdaily.app.wallpaper.WallpaperTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

/** Sin `_uiState` propio — `WallpaperPreferences` ya expone `StateFlow`, no hay nada más
 * que agregar acá todavía. Si Ajustes crece con más opciones, esto puede combinarlos. */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val wallpaperPreferences: WallpaperPreferences
) : ViewModel() {

    val autoChangeEnabled: StateFlow<Boolean> = wallpaperPreferences.autoChangeEnabled
    val target: StateFlow<WallpaperTarget> = wallpaperPreferences.target

    fun setAutoChangeEnabled(enabled: Boolean) = wallpaperPreferences.setAutoChangeEnabled(enabled)

    fun setTarget(target: WallpaperTarget) = wallpaperPreferences.setTarget(target)
}
