package com.artdaily.app.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.artdaily.app.R
import com.artdaily.core.model.Artwork

/**
 * `ui/home` — la única pantalla de la app por ahora. Estética minimalista: la obra
 * cubre toda la pantalla, el texto vive sobre una franja oscura degradada abajo (mismo
 * criterio que el widget), sin distraer con chrome adicional.
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel, onShowDetail: (Artwork) -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        when {
            state.isLoading -> Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.artwork == null -> Box(Modifier.fillMaxSize()) {
                Text(
                    stringResource(R.string.home_no_artworks),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )
            }
            else -> ArtworkOfTheDay(
                artwork = state.artwork!!,
                isFavorite = state.isFavorite,
                onToggleFavorite = viewModel::toggleFavorite,
                onShowDetail = { onShowDetail(state.artwork!!) }
            )
        }
    }
}

@Composable
private fun ArtworkOfTheDay(
    artwork: Artwork,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShowDetail: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val imageUrl = artwork.imageUrlFull ?: artwork.imageUrlThumbnail
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = artwork.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
                // Sin esto, el botón queda dentro de la zona de gestos del sistema (barra
                // de navegación) — casi imposible de tocar con gesture nav activado, que es
                // el modo por defecto hoy. El degradado sí llega hasta el borde real.
                .navigationBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(artwork.title, color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Text(artwork.artistName ?: stringResource(R.string.unknown_artist), color = Color.White)
            Text(
                artwork.museum + (artwork.creationDateText?.let { " · $it" } ?: ""),
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall
            )
            Row(modifier = Modifier.padding(top = 12.dp)) {
                Button(onClick = onToggleFavorite) {
                    Text(
                        stringResource(
                            if (isFavorite) R.string.action_remove_from_favorites
                            else R.string.action_add_to_favorites
                        )
                    )
                }
                // `OutlinedButton` con colores por defecto queda casi invisible sobre la
                // foto (borde y texto en el color "primary" del tema, bajo contraste con
                // una imagen oscura) — se fuerza blanco explícito, igual que el resto del
                // texto superpuesto en este degradado.
                OutlinedButton(
                    onClick = onShowDetail,
                    modifier = Modifier.padding(start = 8.dp),
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text(stringResource(R.string.home_view_details))
                }
            }
        }
    }
}
