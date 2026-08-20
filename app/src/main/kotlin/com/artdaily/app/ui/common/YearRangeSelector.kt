package com.artdaily.app.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.artdaily.app.R

/**
 * Reemplaza al chip de "Siglo" (2026-08-19) — un rango de años es más preciso que un
 * siglo entero, sin la aspereza de "a.C./d.C." como unidades separadas. Compartido entre
 * `ExploreScreen` y `ArtWidgetConfigActivity`, mismo criterio que `FilterSection`.
 *
 * `minYear`/`maxYear` son los bordes reales del catálogo (`AvailableFilterOptions`) — si
 * no hay ninguna obra con año conocido, no se muestra nada (igual que `FilterSection`
 * con una lista de opciones vacía).
 *
 * Arrastre en vivo: el `RangeSlider` actualiza su posición visual en cada frame
 * (`onValueChange`), pero solo dispara `onRangeSelected` (que relanza la búsqueda/cuenta)
 * al soltar (`onValueChangeFinished`) — evita re-consultar Room en cada píxel arrastrado.
 */
@Composable
fun YearRangeSelector(
    minYear: Int?,
    maxYear: Int?,
    selectedFrom: Int?,
    selectedTo: Int?,
    onRangeSelected: (from: Int, to: Int) -> Unit
) {
    if (minYear == null || maxYear == null || minYear >= maxYear) return

    var liveRange by remember(selectedFrom, selectedTo) {
        mutableStateOf((selectedFrom ?: minYear).toFloat()..(selectedTo ?: maxYear).toFloat())
    }

    Text(stringResource(R.string.label_years), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween) {
        Text(formatYear(liveRange.start.toInt()), style = MaterialTheme.typography.bodySmall)
        Text(formatYear(liveRange.endInclusive.toInt()), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End)
    }
    RangeSlider(
        value = liveRange,
        valueRange = minYear.toFloat()..maxYear.toFloat(),
        onValueChange = { liveRange = it },
        onValueChangeFinished = {
            onRangeSelected(liveRange.start.toInt(), liveRange.endInclusive.toInt())
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    )
}

/** Mismo criterio que el `formatCentury` que reemplaza tenía para siglos negativos. */
@Composable
private fun formatYear(year: Int): String =
    if (year < 0) stringResource(R.string.year_bce, -year) else year.toString()
