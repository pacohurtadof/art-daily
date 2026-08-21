package com.artdaily.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artdaily.app.data.settings.WallpaperPreferences
import com.artdaily.app.domain.usecase.GetArtworkOfTheDayUseCase
import com.artdaily.app.wallpaper.WallpaperApplier
import com.artdaily.app.wallpaper.WallpaperResult
import com.artdaily.app.wallpaper.WallpaperTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isApplyingWallpaper: Boolean = false,
    val wallpaperResult: WallpaperResult? = null
)

/**
 * `autoChangeEnabled`/`target` vienen directo de `WallpaperPreferences`. Activar el
 * toggle (o cambiar el destino mientras ya está activo) aplica ya mismo — el cambio
 * real corre dentro de `DailyArtworkWorker`, que ya se programó una vez al abrir la app
 * (`ExistingPeriodicWorkPolicy.KEEP`) con su propio ciclo de ~24h; sin este fix, tocar
 * el toggle no se nota hasta el próximo ciclo (bug real reportado por el usuario:
 * "activé el toggle pero no pasó nada con mi fondo"). Mismo criterio que
 * `DailyArtworkWorker.enqueueOneTime` ya usaba para no esperar al agregar un widget.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val wallpaperPreferences: WallpaperPreferences,
    private val getArtworkOfTheDay: GetArtworkOfTheDayUseCase,
    private val wallpaperApplier: WallpaperApplier
) : ViewModel() {

    val autoChangeEnabled: StateFlow<Boolean> = wallpaperPreferences.autoChangeEnabled
    val target: StateFlow<WallpaperTarget> = wallpaperPreferences.target

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setAutoChangeEnabled(enabled: Boolean) {
        wallpaperPreferences.setAutoChangeEnabled(enabled)
        if (enabled) applyWallpaperNow()
    }

    fun setTarget(target: WallpaperTarget) {
        wallpaperPreferences.setTarget(target)
        // Si el cambio automático ya estaba activo, se re-aplica con el destino nuevo
        // al toque — si no, el usuario vería el mismo fondo hasta el próximo ciclo aunque
        // acabara de cambiar la configuración.
        if (wallpaperPreferences.autoChangeEnabled.value) applyWallpaperNow()
    }

    fun consumeWallpaperResult() {
        _uiState.update { it.copy(wallpaperResult = null) }
    }

    private fun applyWallpaperNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isApplyingWallpaper = true, wallpaperResult = null) }
            // widgetId=0 = "obra del día" de la app principal — mismo convenio que usa
            // "Hoy" y el propio DailyArtworkWorker.
            val artwork = getArtworkOfTheDay(widgetId = 0)
            val imageUrl = artwork?.imageUrlFull ?: artwork?.imageUrlThumbnail
            val success = wallpaperApplier.apply(imageUrl, wallpaperPreferences.target.value)
            _uiState.update {
                it.copy(
                    isApplyingWallpaper = false,
                    wallpaperResult = if (success) WallpaperResult.SUCCESS else WallpaperResult.ERROR
                )
            }
        }
    }
}
