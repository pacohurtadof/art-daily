package com.artdaily.app.wallpaper

import android.app.WallpaperManager
import android.content.Context
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Descarga una imagen (vía Coil, igual que `WidgetImageDownloader`, pero acá no hace
 * falta guardarla a archivo — `WallpaperManager.setBitmap` toma el `Bitmap` directo) y la
 * aplica como fondo de pantalla real del sistema.
 */
@Singleton
class WallpaperApplier @Inject constructor(@ApplicationContext private val context: Context) {

    /** true si se aplicó, false si no había imagen o algo falló (red, decodificación). No
     * relanza — quien llama (botón manual o worker diario) decide cómo avisar del error. */
    suspend fun apply(imageUrl: String?, target: WallpaperTarget): Boolean {
        if (imageUrl.isNullOrBlank()) return false

        return try {
            val imageLoader = ImageLoader.Builder(context).build()
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                // Igual que en WidgetImageDownloader: un bitmap "hardware" no se puede
                // pasar a setBitmap() de forma confiable en todas las versiones de Android.
                .allowHardware(false)
                .build()

            val result = imageLoader.execute(request)
            val bitmap = (result as? SuccessResult)?.image?.let { (it as? BitmapImage)?.bitmap }
                ?: return false

            WallpaperManager.getInstance(context).setBitmap(
                bitmap, null, true, target.toFlags()
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}
