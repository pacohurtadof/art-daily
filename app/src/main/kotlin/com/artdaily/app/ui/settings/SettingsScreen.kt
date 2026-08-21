package com.artdaily.app.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.artdaily.app.R
import com.artdaily.app.wallpaper.WallpaperResult
import com.artdaily.app.wallpaper.WallpaperTarget

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val autoChangeEnabled by viewModel.autoChangeEnabled.collectAsState()
    val target by viewModel.target.collectAsState()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Mismo patrón que el botón manual de Detalle: un toast, se consume después para
    // que no se repita si la pantalla se recompone por otro motivo.
    LaunchedEffect(state.wallpaperResult) {
        when (state.wallpaperResult) {
            WallpaperResult.SUCCESS ->
                Toast.makeText(context, context.getString(R.string.detail_wallpaper_toast_success), Toast.LENGTH_SHORT).show()
            WallpaperResult.ERROR ->
                Toast.makeText(context, context.getString(R.string.detail_wallpaper_toast_error), Toast.LENGTH_SHORT).show()
            null -> Unit
        }
        if (state.wallpaperResult != null) viewModel.consumeWallpaperResult()
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(stringResource(R.string.nav_settings), style = MaterialTheme.typography.headlineSmall)

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.settings_auto_wallpaper_title))
                Text(
                    stringResource(R.string.settings_auto_wallpaper_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp, end = 12.dp)
                )
            }
            if (state.isApplyingWallpaper) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 12.dp).size(20.dp), strokeWidth = 2.dp)
            }
            Switch(
                checked = autoChangeEnabled,
                onCheckedChange = viewModel::setAutoChangeEnabled,
                enabled = !state.isApplyingWallpaper
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        // Destino del cambio automático — a diferencia del botón manual de Detalle (que
        // pregunta cada vez), acá no hay ningún diálogo: el worker corre solo, sin UI,
        // así que si no se elige acá, no hay forma de configurarlo para ese caso.
        Text(
            stringResource(R.string.settings_wallpaper_target_title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            stringResource(R.string.settings_wallpaper_target_subtitle),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
        )
        WallpaperTarget.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(selected = target == option, onClick = { viewModel.setTarget(option) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = target == option, onClick = { viewModel.setTarget(option) })
                Text(stringResource(option.labelRes), modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
