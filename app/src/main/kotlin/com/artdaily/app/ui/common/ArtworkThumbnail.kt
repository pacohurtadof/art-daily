package com.artdaily.app.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.artdaily.core.model.Artwork

/** Celda de grid reusada por `ExploreScreen` y `FavoritesScreen`. */
@Composable
fun ArtworkThumbnail(artwork: Artwork, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
        color = Color.DarkGray
    ) {
        val imageUrl = artwork.imageUrlThumbnail ?: artwork.imageUrlFull
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = artwork.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
