package com.artdaily.app.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artdaily.app.data.local.FavoriteDao
import com.artdaily.app.data.local.FavoriteEntity
import com.artdaily.app.data.translation.TranslationResult
import com.artdaily.app.data.translation.TranslationService
import com.artdaily.app.wallpaper.WallpaperApplier
import com.artdaily.app.wallpaper.WallpaperResult
import com.artdaily.app.wallpaper.WallpaperTarget
import com.artdaily.core.model.Artwork
import com.artdaily.core.repository.ArtworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

/** Por qué tocar "Traducir" no produjo una traducción nueva — un enum en vez del texto
 * ya armado, porque el ViewModel no es contexto `@Composable` y no puede llamar
 * `stringResource()`; `DetailScreen` resuelve el texto real a partir de esto. */
enum class TranslationMessage { ALREADY_IN_DEVICE_LANGUAGE, FAILED }

data class DetailUiState(
    val isLoading: Boolean = true,
    val artwork: Artwork? = null,
    val isFavorite: Boolean = false,
    val isApplyingWallpaper: Boolean = false,
    val wallpaperResult: WallpaperResult? = null,
    val defaultWallpaperTarget: WallpaperTarget = WallpaperTarget.BOTH,
    val translatedDescription: String? = null, // null = se muestra la reseña original
    val isTranslating: Boolean = false,
    // null si no aplica — ver [TranslationMessage].
    val translationMessage: TranslationMessage? = null
)

/** Pantalla de detalle compartida — se llega desde Hoy, Explorar, Favoritos o tocando el
 * widget. `artworkId` viaja codificado en la ruta de navegación porque los ids reales traen
 * ":" (ej. "met:45734"), y se decodifica acá. */
@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val artworkRepository: ArtworkRepository,
    private val favoriteDao: FavoriteDao,
    private val wallpaperApplier: WallpaperApplier,
    private val translationService: TranslationService
) : ViewModel() {

    private val artworkId: String = URLDecoder.decode(
        savedStateHandle.get<String>("artworkId").orEmpty(), "UTF-8"
    )

    // Sin preferencia guardada de destino (se sacó de Ajustes el 2026-08-19, era
    // redundante con este mismo diálogo) — arranca siempre en BOTH, el default del
    // data class; el usuario lo cambia acá mismo si quiere otra cosa.
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val artwork = artworkRepository.getById(artworkId)
            _uiState.update { it.copy(isLoading = false, artwork = artwork) }
            // Reactivo, no una lectura única — mismo fix que en HomeViewModel: si el
            // favorito cambia desde otra pantalla (ej. Hoy), este estado se corrige solo.
            if (artwork != null) {
                favoriteDao.observeIsFavorite(artwork.id).collect { isFavorite ->
                    _uiState.update { it.copy(isFavorite = isFavorite) }
                }
            }
        }
    }

    fun toggleFavorite() {
        val artwork = _uiState.value.artwork ?: return
        val wasFavorite = _uiState.value.isFavorite
        viewModelScope.launch {
            if (wasFavorite) {
                favoriteDao.remove(FavoriteEntity(artworkId = artwork.id, savedAt = 0))
            } else {
                favoriteDao.add(FavoriteEntity(artworkId = artwork.id, savedAt = System.currentTimeMillis()))
            }
            // El `collect` de arriba actualiza `isFavorite` en cuanto Room emite el cambio.
        }
    }

    fun applyWallpaper(target: WallpaperTarget) {
        val artwork = _uiState.value.artwork ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isApplyingWallpaper = true, wallpaperResult = null) }
            val imageUrl = artwork.imageUrlFull ?: artwork.imageUrlThumbnail
            val success = wallpaperApplier.apply(imageUrl, target)
            _uiState.update {
                it.copy(
                    isApplyingWallpaper = false,
                    wallpaperResult = if (success) WallpaperResult.SUCCESS else WallpaperResult.ERROR
                )
            }
        }
    }

    fun consumeWallpaperResult() {
        _uiState.update { it.copy(wallpaperResult = null) }
    }

    /** Traducción on-device (ML Kit) — opt-in, se dispara solo al tocar "Traducir", no
     * automático al abrir Detalle (evita bajar un modelo de idioma sin que el usuario lo
     * haya pedido). Ver `TranslationService` para el porqué de este enfoque. */
    fun translateDescription() {
        val description = _uiState.value.artwork?.description ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true, translationMessage = null) }
            when (val result = translationService.translate(description)) {
                is TranslationResult.Success -> _uiState.update {
                    it.copy(isTranslating = false, translatedDescription = result.text)
                }
                TranslationResult.NotNeeded -> _uiState.update {
                    it.copy(isTranslating = false, translationMessage = TranslationMessage.ALREADY_IN_DEVICE_LANGUAGE)
                }
                TranslationResult.Unavailable -> _uiState.update {
                    it.copy(isTranslating = false, translationMessage = TranslationMessage.FAILED)
                }
            }
        }
    }

    fun showOriginalDescription() {
        _uiState.update { it.copy(translatedDescription = null, translationMessage = null) }
    }
}
