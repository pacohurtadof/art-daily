package com.artdaily.app.widget

import android.content.Context
import android.graphics.Bitmap
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import java.io.File
import java.io.FileOutputStream

/**
 * Descarga la miniatura de una obra a un archivo local en `filesDir`, para que
 * [ArtWidget] la pueda mostrar — Glance no soporta `AsyncImage` de Coil directo como
 * Compose normal, así que el bitmap tiene que existir como archivo antes de renderizarlo.
 *
 * `allowHardware(false)` es necesario: los bitmaps "hardware" (`Bitmap.Config.HARDWARE`)
 * no se pueden leer pixel a pixel para guardarlos a archivo con `compress()`.
 */
object WidgetImageDownloader {

    /** Devuelve la ruta del archivo guardado, o null si no había imagen o falló la descarga. */
    suspend fun downloadToFile(context: Context, widgetId: Int, imageUrl: String?): String? {
        if (imageUrl.isNullOrBlank()) return null

        return try {
            val imageLoader = ImageLoader.Builder(context).build()
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = imageLoader.execute(request)
            val bitmap = (result as? SuccessResult)?.image?.let { (it as? BitmapImage)?.bitmap }
                ?: return null

            val dir = File(context.filesDir, "widget_images").apply { mkdirs() }
            val file = File(dir, "$widgetId.png")
            FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 90, out) }
            file.absolutePath
        } catch (e: Exception) {
            // Una imagen que no cargó no debería tumbar todo el worker — el widget cae a
            // texto-solo (ArtWidget ya maneja imageFilePath == null).
            null
        }
    }

    fun deleteFor(context: Context, widgetId: Int) {
        File(context.filesDir, "widget_images/$widgetId.png").delete()
    }
}
