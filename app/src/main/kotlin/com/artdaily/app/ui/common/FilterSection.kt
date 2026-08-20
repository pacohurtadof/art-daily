package com.artdaily.app.ui.common

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.artdaily.app.R

/**
 * Fila de chips de un solo filtro (periodo, movimiento, museo o siglo). Compartido entre
 * `ArtWidgetConfigActivity` (filtro de un widget) y `ExploreScreen` (explorar el catálogo)
 * — misma UI, dos consumidores distintos.
 *
 * `title` distingue una sección de otra; `T` es String para period/movement/museum, Int
 * para century. `label` es `@Composable` (no una función plana) para que pueda llamar
 * `stringResource` adentro — ver [formatCentury].
 */
@Composable
fun <T> FilterSection(
    title: String,
    options: List<T>,
    selected: T?,
    label: @Composable (T) -> String,
    onSelect: (T?) -> Unit
) {
    if (options.isEmpty()) return

    Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
    FlowRow(modifier = Modifier.padding(bottom = 12.dp)) {
        options.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(label(option)) },
                modifier = Modifier.padding(end = 6.dp, bottom = 6.dp)
            )
        }
    }
}

@Composable
fun formatCentury(century: Int): String =
    if (century > 0) stringResource(R.string.century_ce, century) else stringResource(R.string.century_bce, -century)
