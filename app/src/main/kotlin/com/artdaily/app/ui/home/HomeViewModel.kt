package com.artdaily.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artdaily.app.data.local.FavoriteDao
import com.artdaily.app.data.local.FavoriteEntity
import com.artdaily.app.domain.usecase.GetArtworkOfTheDayUseCase
import com.artdaily.core.model.Artwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val artwork: Artwork? = null,
    val isFavorite: Boolean = false
)

/**
 * `widgetId = 0` = "obra del día" de la app principal, sin widget asociado — mismo
 * convenio usado en `HistoryEntity`/`GetArtworkOfTheDayUseCase` desde la Etapa 2.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArtworkOfTheDay: GetArtworkOfTheDayUseCase,
    private val favoriteDao: FavoriteDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val artwork = getArtworkOfTheDay(widgetId = 0)
            _uiState.update { it.copy(isLoading = false, artwork = artwork) }
            // Reactivo, no una lectura única — así si el favorito se quita/agrega desde
            // Detalle (u otra pantalla), este estado se corrige solo. Ver `observeIsFavorite`.
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
            // No hace falta actualizar `isFavorite` acá — el `collect` de arriba lo hace
            // solo en cuanto Room emite el cambio.
        }
    }
}
