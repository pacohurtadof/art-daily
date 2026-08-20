package com.artdaily.app.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artdaily.app.data.local.WidgetConfigDao
import com.artdaily.app.data.local.WidgetConfigEntity
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.model.AvailableFilterOptions
import com.artdaily.core.repository.ArtworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * No incluye filtro por artista todavía — necesitaría un buscador (podrían ser cientos de
 * artistas distintos), no un chip seleccionable como period/movement/museum/century.
 * Queda marcado como pendiente explícito, no un olvido.
 */
data class ConfigUiState(
    val isLoading: Boolean = true,
    val available: AvailableFilterOptions = AvailableFilterOptions(),
    val selectedPeriod: String? = null,
    val selectedMovement: String? = null,
    val selectedMuseum: String? = null,
    val selectedCentury: Int? = null,
    val matchingCount: Int = 0
) {
    val filter: ArtworkFilter
        get() = ArtworkFilter(
            period = selectedPeriod,
            century = selectedCentury,
            movement = selectedMovement,
            museum = selectedMuseum
        )
}

@HiltViewModel
class ArtWidgetConfigViewModel @Inject constructor(
    private val artworkRepository: ArtworkRepository,
    private val widgetConfigDao: WidgetConfigDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val available = artworkRepository.getAvailableFilterOptions()
            _uiState.update { it.copy(available = available, isLoading = false) }
            recomputeMatchCount()
        }
    }

    fun selectPeriod(value: String?) = updateSelection { copy(selectedPeriod = toggled(selectedPeriod, value)) }
    fun selectMovement(value: String?) = updateSelection { copy(selectedMovement = toggled(selectedMovement, value)) }
    fun selectMuseum(value: String?) = updateSelection { copy(selectedMuseum = toggled(selectedMuseum, value)) }
    fun selectCentury(value: Int?) = updateSelection { copy(selectedCentury = toggledInt(selectedCentury, value)) }

    /** Guarda la config para este widget. `avoidRepeatDays` se deja en el default (30). */
    suspend fun saveConfig(widgetId: Int) {
        val state = _uiState.value
        widgetConfigDao.upsert(
            WidgetConfigEntity(
                widgetId = widgetId,
                period = state.selectedPeriod,
                century = state.selectedCentury,
                movement = state.selectedMovement,
                artistName = null, // sin UI de filtro por artista todavía
                museum = state.selectedMuseum
            )
        )
    }

    private fun updateSelection(block: ConfigUiState.() -> ConfigUiState) {
        _uiState.update(block)
        recomputeMatchCount()
    }

    private fun recomputeMatchCount() {
        viewModelScope.launch {
            val count = artworkRepository.countFiltered(_uiState.value.filter, minRankScore = 0f)
            _uiState.update { it.copy(matchingCount = count) }
        }
    }

    /** Tocar el chip ya seleccionado lo deselecciona (vuelve a "Cualquiera"). */
    private fun toggled(current: String?, value: String?): String? = if (current == value) null else value
    private fun toggledInt(current: Int?, value: Int?): Int? = if (current == value) null else value
}
