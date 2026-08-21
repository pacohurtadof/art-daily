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
 * artistas distintos), no un chip seleccionable como period/movement. Museo se sacó de la
 * UI y siglo se reemplazó por un rango de años (`yearFrom`/`yearTo`, 2026-08-19).
 */
data class ConfigUiState(
    val isLoading: Boolean = true,
    val available: AvailableFilterOptions = AvailableFilterOptions(),
    val selectedPeriod: String? = null,
    val selectedMovement: String? = null,
    // null hasta que `available` carga — a partir de ahí, por defecto son los bordes
    // reales del catálogo (equivale a "no filtrar"), el usuario los achica arrastrando.
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val matchingCount: Int = 0
) {
    // La config de un widget sigue siendo single-select en su propia UI (decisión explícita
    // del usuario, 2026-08-21: la multi-selección es solo para Explorar) — pero `ArtworkFilter`
    // ahora pide listas, así que se envuelve el único valor elegido en una lista de 0 o 1
    // elemento. `FilterSection` (compartido con Explorar) se adapta del mismo modo en
    // `ArtWidgetConfigActivity`, sin tocar el modelo de estado de este ViewModel.
    val filter: ArtworkFilter
        get() = ArtworkFilter(
            periods = selectedPeriod?.let { listOf(it) },
            movements = selectedMovement?.let { listOf(it) },
            yearFrom = yearFrom,
            yearTo = yearTo
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
            _uiState.update {
                it.copy(
                    available = available,
                    isLoading = false,
                    yearFrom = available.minYear,
                    yearTo = available.maxYear
                )
            }
            recomputeMatchCount()
        }
    }

    fun selectPeriod(value: String?) = updateSelection { copy(selectedPeriod = toggled(selectedPeriod, value)) }
    fun selectMovement(value: String?) = updateSelection { copy(selectedMovement = toggled(selectedMovement, value)) }

    fun selectYearRange(from: Int, to: Int) = updateSelection { copy(yearFrom = from, yearTo = to) }

    /** Guarda la config para este widget. `avoidRepeatDays` se deja en el default (30). */
    suspend fun saveConfig(widgetId: Int) {
        val state = _uiState.value
        widgetConfigDao.upsert(
            WidgetConfigEntity(
                widgetId = widgetId,
                period = state.selectedPeriod,
                movement = state.selectedMovement,
                artistName = null, // sin UI de filtro por artista todavía
                yearFrom = state.yearFrom,
                yearTo = state.yearTo
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
}
