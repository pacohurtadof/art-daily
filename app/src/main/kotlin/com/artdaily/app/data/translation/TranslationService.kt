package com.artdaily.app.data.translation

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier.UNDETERMINED_LANGUAGE_TAG
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

sealed interface TranslationResult {
    data class Success(val text: String) : TranslationResult
    /** El texto ya estaba en el idioma del dispositivo, o no se pudo identificar el
     * idioma de origen (texto muy corto/ambiguo) — en ambos casos no hay nada que traducir. */
    data object NotNeeded : TranslationResult
    /** El idioma del dispositivo no lo soporta ML Kit, o falló la traducción (sin red
     * para bajar el modelo la primera vez, u otro error). */
    data object Unavailable : TranslationResult
}

/**
 * Traducción on-device vía ML Kit — se pidió (2026-08-19) mostrar las reseñas de
 * obras (que llegan en inglés/neerlandés desde AIC/CMA/Rijksmuseum, nunca en el idioma
 * del usuario) traducidas al idioma del dispositivo. Se descartó a propósito una API de
 * traducción en la nube: costaría dinero por uso, necesitaría una API key, y rompería
 * "la app nunca llama a servicios externos en vivo" (ver CLAUDE.md). ML Kit traduce EN
 * el teléfono — el modelo de cada par de idiomas se descarga una sola vez (unos MB) y
 * después funciona offline, igual que el resto de la app.
 *
 * No sabemos de antemano en qué idioma llegó cada reseña (Rijksmuseum puede traer inglés
 * O neerlandés según el objeto — ver `RijksMapper`), así que primero se identifica el
 * idioma real del texto con `LanguageIdentification` en vez de asumirlo por la fuente.
 */
@Singleton
class TranslationService @Inject constructor() {

    suspend fun translate(text: String): TranslationResult {
        val targetLanguage = TranslateLanguage.fromLanguageTag(Locale.getDefault().language)
            ?: return TranslationResult.Unavailable

        val sourceLanguage = try {
            LanguageIdentification.getClient().identifyLanguage(text).await()
        } catch (e: Exception) {
            return TranslationResult.Unavailable
        }

        if (sourceLanguage == UNDETERMINED_LANGUAGE_TAG || sourceLanguage == targetLanguage) {
            return TranslationResult.NotNeeded
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()
        val translator = Translation.getClient(options)

        return try {
            // Sin `DownloadConditions` explícitas: se permite bajar el modelo por datos
            // móviles también — el usuario ya tocó "Traducir" a propósito, no es un
            // fondo de pantalla automático que se dispara solo (ver WallpaperPreferences).
            translator.downloadModelIfNeeded().await()
            val translated = translator.translate(text).await()
            TranslationResult.Success(translated)
        } catch (e: Exception) {
            TranslationResult.Unavailable
        } finally {
            translator.close()
        }
    }
}
