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
    // null hasta que `available` carga — a partir de ahí, por defecto son los bordes
    // reales del catálogo (equivale a "no filtrar"), el usuario los achica arrastrando.
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val results: List<Artwork> = emptyList()
) {
    val filter: ArtworkFilter
        get() = ArtworkFilter(
            period = selectedPeriod,
            movement = selectedMovement,
            yearFrom = yearFrom,
            yearTo = yearTo
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
            _uiState.update {
                it.copy(available = available, yearFrom = available.minYear, yearTo = available.maxYear)
            }
            search()
        }
    }

    fun selectPeriod(value: String?) = updateSelection { copy(selectedPeriod = toggled(selectedPeriod, value)) }
    fun selectMovement(value: String?) = updateSelection { copy(selectedMovement = toggled(selectedMovement, value)) }
    fun selectYearRange(from: Int, to: Int) = updateSelection { copy(yearFrom = from, yearTo = to) }

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

    private companion object {
        const val MAX_RESULTS = 200
    }
}
