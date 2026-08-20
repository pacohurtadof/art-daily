package com.artdaily.app.ui.detail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.artdaily.app.R
import com.artdaily.app.wallpaper.WallpaperTarget
import com.artdaily.core.model.Artwork

@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Feedback del resultado de "usar como fondo" — un solo toast, se consume después
    // para que no se repita si la pantalla se recompone por otro motivo.
    // `Toast.makeText` no es @Composable (corre dentro de un LaunchedEffect) — se usa
    // `context.getString()`, el equivalente de `stringResource()` fuera de composición.
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

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.artwork == null -> Text(
                stringResource(R.string.detail_not_found),
                modifier = Modifier.align(Alignment.Center).padding(24.dp)
            )
            else -> DetailContent(
                artwork = state.artwork!!,
                isFavorite = state.isFavorite,
                onToggleFavorite = viewModel::toggleFavorite,
                isApplyingWallpaper = state.isApplyingWallpaper,
                defaultWallpaperTarget = state.defaultWallpaperTarget,
                onApplyWallpaper = viewModel::applyWallpaper,
                translatedDescription = state.translatedDescription,
                isTranslating = state.isTranslating,
                translationMessage = state.translationMessage,
                onTranslate = viewModel::translateDescription,
                onShowOriginal = viewModel::showOriginalDescription
            )
        }
    }
}

@Composable
private fun DetailContent(
    artwork: Artwork,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    isApplyingWallpaper: Boolean,
    defaultWallpaperTarget: WallpaperTarget,
    onApplyWallpaper: (WallpaperTarget) -> Unit,
    translatedDescription: String?,
    isTranslating: Boolean,
    translationMessage: TranslationMessage?,
    onTranslate: () -> Unit,
    onShowOriginal: () -> Unit
) {
    val context = LocalContext.current
    var showWallpaperDialog by remember { mutableStateOf(false) }

    if (showWallpaperDialog) {
        WallpaperTargetDialog(
            initialTarget = defaultWallpaperTarget,
            onConfirm = { target ->
                showWallpaperDialog = false
                onApplyWallpaper(target)
            },
            onDismiss = { showWallpaperDialog = false }
        )
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        val imageUrl = artwork.imageUrlFull ?: artwork.imageUrlThumbnail
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = artwork.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
        }

        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(artwork.title, style = MaterialTheme.typography.headlineSmall)
            Text(artwork.artistName ?: stringResource(R.string.unknown_artist), style = MaterialTheme.typography.titleMedium)
            if (artwork.artistBirthYear != null || artwork.artistDeathYear != null) {
                Text(
                    "${artwork.artistBirthYear ?: "?"} – ${artwork.artistDeathYear ?: "?"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(onClick = onToggleFavorite, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)) {
                Text(
                    stringResource(
                        if (isFavorite) R.string.action_remove_from_favorites
                        else R.string.action_add_to_favorites
                    )
                )
            }

            OutlinedButton(
                onClick = { showWallpaperDialog = true },
                enabled = !isApplyingWallpaper,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    stringResource(
                        if (isApplyingWallpaper) R.string.detail_wallpaper_applying
                        else R.string.detail_wallpaper_button
                    )
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            InfoRow(stringResource(R.string.label_museum), artwork.museum)
            InfoRow(stringResource(R.string.detail_label_date), artwork.creationDateText)
            InfoRow(stringResource(R.string.label_period), artwork.period)
            InfoRow(stringResource(R.string.label_movement), artwork.movement)
            InfoRow(stringResource(R.string.detail_label_classification), artwork.classification)
            InfoRow(stringResource(R.string.detail_label_culture), artwork.culture)
            InfoRow(stringResource(R.string.detail_label_country), artwork.country)
            InfoRow(stringResource(R.string.detail_label_dimensions), artwork.dimensions)
            InfoRow(stringResource(R.string.detail_label_accession_number), artwork.accessionNumber)
            InfoRow(stringResource(R.string.detail_label_credit), artwork.creditLine)
            InfoRow(stringResource(R.string.detail_label_license), artwork.license)

            val description = artwork.description
            if (!description.isNullOrBlank()) {
                Text(
                    stringResource(R.string.detail_about_artwork),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    translatedDescription ?: description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                // Solo AIC exige esto hoy (CC BY 4.0, a diferencia del resto de la app que
                // es CC0) — ver AicMapper. Se muestra igual aunque el texto esté traducido:
                // sigue siendo un derivado del mismo original CC BY. Null para el resto.
                artwork.descriptionAttribution?.let { attribution ->
                    Text(
                        attribution,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Traducción on-device (ML Kit) — opt-in a propósito, no automática (ver
                // TranslationService). "Ver original" vuelve al texto tal cual llegó del museo.
                if (translatedDescription != null) {
                    TextButton(onClick = onShowOriginal, modifier = Modifier.padding(top = 4.dp)) {
                        Text(stringResource(R.string.detail_view_original))
                    }
                } else {
                    TextButton(
                        onClick = onTranslate,
                        enabled = !isTranslating,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(stringResource(if (isTranslating) R.string.detail_translating else R.string.detail_translate))
                    }
                    translationMessage?.let { message ->
                        Text(
                            stringResource(
                                when (message) {
                                    TranslationMessage.ALREADY_IN_DEVICE_LANGUAGE -> R.string.detail_translation_already_in_language
                                    TranslationMessage.FAILED -> R.string.detail_translation_failed
                                }
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(artwork.sourceUrl)))
                },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            ) {
                Text(stringResource(R.string.detail_view_on_museum_site))
            }
        }
    }
}

/** Mismas tres opciones que Ajustes (`WallpaperTarget`) — precargadas en la preferencia
 * guardada, para no obligar a elegir cada vez si el usuario siempre quiere lo mismo. */
@Composable
private fun WallpaperTargetDialog(
    initialTarget: WallpaperTarget,
    onConfirm: (WallpaperTarget) -> Unit,
    onDismiss: () -> Unit
) {
    var selected by remember { mutableStateOf(initialTarget) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.detail_wallpaper_button)) },
        text = {
            Column {
                WallpaperTarget.entries.forEach { target ->
                    RadioOptionRow(
                        selected = selected == target,
                        label = stringResource(target.labelRes),
                        onClick = { selected = target }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(stringResource(R.string.action_apply)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
        }
    )
}

@Composable
private fun RadioOptionRow(selected: Boolean, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
private fun InfoRow(label: String, value: String?) {
    if (value.isNullOrBlank()) return
    Text(
        "$label: $value",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
