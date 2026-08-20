package com.artdaily.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.artdaily.app.wallpaper.WallpaperTarget

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val autoChangeEnabled by viewModel.autoChangeEnabled.collectAsState()
    val target by viewModel.target.collectAsState()

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

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

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
