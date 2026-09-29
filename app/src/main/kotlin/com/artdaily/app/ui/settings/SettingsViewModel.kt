package com.artdaily.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artdaily.app.data.local.FavoriteDao
import com.artdaily.app.data.settings.WallpaperPreferences
import com.artdaily.app.domain.usecase.ApplyAutomaticWallpaperUseCase
import com.artdaily.app.wallpaper.WallpaperResult
import com.artdaily.app.wallpaper.WallpaperSource
import com.artdaily.app.wallpaper.WallpaperTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isApplyingWallpaper: Boolean = false,
    val wallpaperResult: WallpaperResult? = null
)

/**
 * `autoChangeEnabled`/`target`/`source` vienen directo de `WallpaperPreferences`. Activar
 * el toggle (o cambiar el destino/fuente mientras ya está activo) aplica ya mismo, vía
 * `ApplyAutomaticWallpaperUseCase(bypassDailyGuard = true)` — la misma lógica que usa
 * `DailyArtworkWorker` para el ciclo automático de ~24h, pero saltando la guarda de "ya
 * aplicado hoy" porque acá SÍ hay un usuario esperando ver el resultado ya mismo (bug real
 * ya arreglado antes, 2026-08-21: "activé el toggle pero no pasó nada con mi fondo"). Mismo
 * criterio que `DailyArtworkWorker.enqueueOneTime` ya usaba para no esperar al agregar un
 * widget.
 *
 * `favoritesCount` es solo para la UI (mostrar un aviso si se elige "rotar entre
 * favoritos" sin tener ninguno guardado todavía) — el cálculo real de rotación vive en
 * `GetNextFavoriteWallpaperUseCase`.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val wallpaperPreferences: WallpaperPreferences,
    private val applyAutomaticWallpaper: ApplyAutomaticWallpaperUseCase,
    favoriteDao: FavoriteDao
) : ViewModel() {

    val autoChangeEnabled: StateFlow<Boolean> = wallpaperPreferences.autoChangeEnabled
    val target: StateFlow<WallpaperTarget> = wallpaperPreferences.target
    val source: StateFlow<WallpaperSource> = wallpaperPreferences.source

    val favoritesCount: StateFlow<Int> = favoriteDao.observeAll()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

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

    fun setSource(source: WallpaperSource) {
        wallpaperPreferences.setSource(source)
        // Mismo criterio que setTarget: si el cambio automático ya estaba activo, se
        // re-aplica ya mismo con la fuente nueva.
        if (wallpaperPreferences.autoChangeEnabled.value) applyWallpaperNow()
    }

    fun consumeWallpaperResult() {
        _uiState.update { it.copy(wallpaperResult = null) }
    }

    private fun applyWallpaperNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isApplyingWallpaper = true, wallpaperResult = null) }
            val success = applyAutomaticWallpaper(bypassDailyGuard = true)
            _uiState.update {
                it.copy(
                    isApplyingWallpaper = false,
                    wallpaperResult = if (success) WallpaperResult.SUCCESS else WallpaperResult.ERROR
                )
            }
        }
    }
}
