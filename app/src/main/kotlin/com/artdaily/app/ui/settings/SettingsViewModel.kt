package com.artdaily.app.ui.settings

import androidx.lifecycle.ViewModel
import com.artdaily.app.data.settings.WallpaperPreferences
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

    fun setAutoChangeEnabled(enabled: Boolean) = wallpaperPreferences.setAutoChangeEnabled(enabled)
}
