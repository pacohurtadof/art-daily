package com.artdaily.app.ui.common

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * Fila de chips de un solo filtro (periodo o movimiento — museo y siglo se sacaron el
 * 2026-08-19, el segundo reemplazado por [YearRangeSelector]). Compartido entre
 * `ArtWidgetConfigActivity` (filtro de un widget) y `ExploreScreen` (explorar el catálogo)
 * — misma UI, dos consumidores distintos.
 *
 * `selected` es un `Set<T>` (no un solo valor) desde el 2026-08-21 — Explorar permite elegir
 * varios chips a la vez (ej. Impresionismo + Expresionismo). `onToggle` solo avisa qué chip
 * se tocó; quién llama decide si eso agrega/saca del set (Explorar, multi-selección) o
 * reemplaza el valor (config de widget, sigue siendo single-select armando un set de 0 o 1
 * elemento en el call site — ver `ArtWidgetConfigActivity`).
 *
 * `title` distingue una sección de otra. `label` es `@Composable` (no una función plana)
 * para que futuros usos puedan llamar `stringResource` adentro si hace falta.
 *
 * `testTag` (2026-08-21) es un identificador ESTABLE para tests de Compose UI —
 * a diferencia de `title`/`label`, que están traducidos y cambian según el idioma del
 * dispositivo, así un test no depende de en qué idioma corre el emulador/teléfono. Cada
 * chip queda tageado como "${testTag}_chip_\$option". `null` (default) no tagea nada —
 * no afecta el layout ni se ve en pantalla.
 */
@Composable
fun <T> FilterSection(
    title: String,
    options: List<T>,
    selected: Set<T>,
    label: @Composable (T) -> String,
    onToggle: (T) -> Unit,
    testTag: String? = null
) {
    if (options.isEmpty()) return

    Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
    FlowRow(modifier = Modifier.padding(bottom = 12.dp)) {
        options.forEach { option ->
            FilterChip(
                selected = option in selected,
                onClick = { onToggle(option) },
                label = { Text(label(option)) },
                modifier = Modifier
                    .padding(end = 6.dp, bottom = 6.dp)
                    .let { if (testTag != null) it.testTag("${testTag}_chip_$option") else it }
            )
        }
    }
}
