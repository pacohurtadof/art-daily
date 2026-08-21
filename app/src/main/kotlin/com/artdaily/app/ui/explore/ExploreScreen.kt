package com.artdaily.app.ui.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.artdaily.app.R
import com.artdaily.app.ui.common.ArtworkThumbnail
import com.artdaily.app.ui.common.FilterSection
import com.artdaily.app.ui.common.YearRangeSelector
import com.artdaily.core.model.Artwork

/**
 * Todo — encabezado de filtros y grid de resultados — vive dentro de UN SOLO
 * `LazyVerticalGrid` (el encabezado ocupa el ancho completo vía `GridItemSpan`), no un
 * `Column` fijo + grid separado. Con periodo/movimiento/años, la sección de filtros por
 * sí sola ya no cabe en una pantalla — si no scrollea junto con el resto, empuja los
 * resultados fuera de vista sin forma de bajar a verlos.
 */
@Composable
fun ExploreScreen(viewModel: ExploreViewModel, onArtworkClick: (Artwork) -> Unit) {
    val state by viewModel.uiState.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            FilterHeader(state = state, viewModel = viewModel)
        }

        when {
            state.isLoading -> item(span = { GridItemSpan(maxLineSpan) }) {
                CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            }
            state.results.isEmpty() -> item(span = { GridItemSpan(maxLineSpan) }) {
                Text(stringResource(R.string.explore_empty_results), modifier = Modifier.padding(16.dp))
            }
            else -> items(state.results, key = { it.id }) { artwork ->
                ArtworkThumbnail(artwork = artwork, onClick = { onArtworkClick(artwork) })
            }
        }
    }
}

@Composable
private fun FilterHeader(state: ExploreUiState, viewModel: ExploreViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.nav_explore), style = MaterialTheme.typography.headlineSmall)
        Text(
            stringResource(R.string.explore_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )
        FilterSection(
            title = stringResource(R.string.label_period), options = state.available.periods,
            selected = state.selectedPeriods, label = { it }, onToggle = viewModel::selectPeriod
        )
        FilterSection(
            title = stringResource(R.string.label_movement), options = state.available.movements,
            selected = state.selectedMovements, label = { it }, onToggle = viewModel::selectMovement
        )
        YearRangeSelector(
            minYear = state.available.minYear,
            maxYear = state.available.maxYear,
            selectedFrom = state.yearFrom,
            selectedTo = state.yearTo,
            onRangeSelected = viewModel::selectYearRange
        )
    }
}
