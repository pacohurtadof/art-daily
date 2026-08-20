package com.artdaily.app.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artdaily.core.model.Artwork
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

data class ExploreUiState(
    val isLoading: Boolean = true,
    val available: AvailableFilterOptions = AvailableFilterOptions(),
    val selectedPeriod: String? = null,
    val selectedMovement: String? = null,
    val selectedMuseum: String? = null,
    val selectedCentury: Int? = null,
    val results: List<Artwork> = emptyList()
) {
    val filter: ArtworkFilter
        get() = ArtworkFilter(
            period = selectedPeriod,
            century = selectedCentury,
            movement = selectedMovement,
            museum = selectedMuseum
        )
}

/**
 * Mismo patrón de filtros que `ArtWidgetConfigViewModel` (chips + `ArtworkRepository`), pero
 * en vez de guardar la config de un widget, muestra los resultados en una cuadrícula. Se
 * limita a [MAX_RESULTS] obras — mostrar las 2000+ del catálogo entero de una sola vez no
 * aporta nada a un grid scrolleable y sí es más lento.
 */
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val artworkRepository: ArtworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val available = artworkRepository.getAvailableFilterOptions()
            _uiState.update { it.copy(available = available) }
            search()
        }
    }

    fun selectPeriod(value: String?) = updateSelection { copy(selectedPeriod = toggled(selectedPeriod, value)) }
    fun selectMovement(value: String?) = updateSelection { copy(selectedMovement = toggled(selectedMovement, value)) }
    fun selectMuseum(value: String?) = updateSelection { copy(selectedMuseum = toggled(selectedMuseum, value)) }
    fun selectCentury(value: Int?) = updateSelection { copy(selectedCentury = toggledInt(selectedCentury, value)) }

    private fun updateSelection(block: ExploreUiState.() -> ExploreUiState) {
        _uiState.update(block)
        search()
    }

    private fun search() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val results = artworkRepository.getFiltered(_uiState.value.filter, minRankScore = 0f)
                .take(MAX_RESULTS)
            _uiState.update { it.copy(isLoading = false, results = results) }
        }
    }

    private fun toggled(current: String?, value: String?): String? = if (current == value) null else value
    private fun toggledInt(current: Int?, value: Int?): Int? = if (current == value) null else value

    private companion object {
        const val MAX_RESULTS = 200
    }
}
