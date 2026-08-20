package com.artdaily.app.ui.common

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Fila de chips de un solo filtro (periodo o movimiento — museo y siglo se sacaron el
 * 2026-08-19, el segundo reemplazado por [YearRangeSelector]). Compartido entre
 * `ArtWidgetConfigActivity` (filtro de un widget) y `ExploreScreen` (explorar el catálogo)
 * — misma UI, dos consumidores distintos.
 *
 * `title` distingue una sección de otra. `label` es `@Composable` (no una función plana)
 * para que futuros usos puedan llamar `stringResource` adentro si hace falta.
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
