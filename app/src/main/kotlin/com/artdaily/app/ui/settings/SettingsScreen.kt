package com.artdaily.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.artdaily.app.R

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val autoChangeEnabled by viewModel.autoChangeEnabled.collectAsState()

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
            Switch(checked = autoChangeEnabled, onCheckedChange = viewModel::setAutoChangeEnabled)
        }
        // El destino (inicio/bloqueo/ambas) se sacó de acá el 2026-08-19 — era
        // redundante con el diálogo de "Usar como fondo de pantalla" en Detalle, que
        // ya pregunta lo mismo cada vez que se usa (ver WallpaperPreferences). El
        // cambio automático usa siempre "ambas pantallas" fijo.
    }
}
