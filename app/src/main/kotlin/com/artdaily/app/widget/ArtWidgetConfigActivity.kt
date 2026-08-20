package com.artdaily.app.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.artdaily.app.R
import com.artdaily.app.ui.common.FilterSection
import com.artdaily.app.ui.common.formatCentury
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Pantalla que Android abre automáticamente al agregar el widget (`ACTION_APPWIDGET_CONFIGURE`).
 *
 * Alcance actual: filtro por periodo/movimiento/museo/siglo (chips, valores realmente
 * presentes en la base). Sin filtro por artista todavía — necesita un buscador, no un chip;
 * ver nota en `ArtWidgetConfigViewModel`.
 */
@AndroidEntryPoint
class ArtWidgetConfigActivity : ComponentActivity() {

    private val viewModel: ArtWidgetConfigViewModel by viewModels()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Convención de Android: si el usuario cancela (back), debe contar como cancelado.
        setResult(Activity.RESULT_CANCELED)

        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ConfigScreen(viewModel = viewModel, onConfirm = ::confirmAndFinish)
                }
            }
        }
    }

    private fun confirmAndFinish() {
        lifecycleScope.launch {
            viewModel.saveConfig(appWidgetId)
            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
    }
}

@Composable
private fun ConfigScreen(viewModel: ArtWidgetConfigViewModel, onConfirm: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    // Sin esto, un lag imperceptible entre tocar el botón y que termine de guardar (es una
    // escritura a Room, async) invitaba a tocar de nuevo o presionar "atrás" — y si Android
    // recibe un cancelado antes de que el guardado termine, BORRA el widget que recién se
    // iba a agregar. Deshabilitar + mostrar feedback inmediato evita ambas cosas.
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(stringResource(R.string.widget_config_title), style = MaterialTheme.typography.headlineSmall)
        Text(
            stringResource(R.string.widget_config_subtitle),
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            return@Column
        }

        FilterSection(
            title = stringResource(R.string.label_period),
            options = state.available.periods,
            selected = state.selectedPeriod,
            label = { it },
            onSelect = viewModel::selectPeriod
        )
        FilterSection(
            title = stringResource(R.string.label_movement),
            options = state.available.movements,
            selected = state.selectedMovement,
            label = { it },
            onSelect = viewModel::selectMovement
        )
        FilterSection(
            title = stringResource(R.string.label_museum),
            options = state.available.museums,
            selected = state.selectedMuseum,
            label = { it },
            onSelect = viewModel::selectMuseum
        )
        FilterSection(
            title = stringResource(R.string.label_century),
            options = state.available.centuries,
            selected = state.selectedCentury,
            label = { formatCentury(it) },
            onSelect = viewModel::selectCentury
        )

        Text(
            text = if (state.matchingCount > 0) {
                stringResource(R.string.widget_config_matching_count, state.matchingCount)
            } else {
                stringResource(R.string.widget_config_no_match)
            },
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(
            onClick = {
                isSaving = true
                onConfirm()
            },
            enabled = state.matchingCount > 0 && !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(if (isSaving) R.string.widget_config_saving else R.string.widget_config_add_button))
        }
    }
}
