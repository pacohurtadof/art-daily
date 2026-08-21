package com.artdaily.app.data.settings

import android.content.Context
import androidx.core.content.edit
import com.artdaily.app.wallpaper.WallpaperSource
import com.artdaily.app.wallpaper.WallpaperTarget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Solo dos valores simples (un booleano + un enum) — `SharedPreferences` de toda la vida
 * alcanza, no hace falta traer DataStore para esto. Expuestos como `StateFlow` para que
 * `SettingsScreen` se recomponga sola al cambiarlos, sin re-leer `SharedPreferences` a mano.
 *
 * `target` se sacó de acá el 2026-08-19 (parecía redundante con el diálogo manual de
 * Detalle, que pregunta lo mismo cada vez) y se volvió a agregar el mismo día (feedback
 * real del usuario al probarlo en un dispositivo real): el cambio AUTOMÁTICO no tiene
 * ningún diálogo — corre solo, sin UI — así que si no se guarda acá, no hay forma de
 * elegir destino para ese caso. El diálogo manual de Detalle sigue siendo independiente
 * de esto, sigue preguntando cada vez sin leer esta preferencia.
 */
@Singleton
class WallpaperPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _autoChangeEnabled = MutableStateFlow(prefs.getBoolean(KEY_AUTO_ENABLED, false))
    /** Apagado por defecto — cambiar el fondo de pantalla del usuario sin que lo pida es
     * invasivo, tiene que activarlo a propósito desde Ajustes. */
    val autoChangeEnabled: StateFlow<Boolean> = _autoChangeEnabled.asStateFlow()

    private val _target = MutableStateFlow(
        prefs.getString(KEY_TARGET, null)?.let { runCatching { WallpaperTarget.valueOf(it) }.getOrNull() }
            ?: WallpaperTarget.BOTH
    )
    val target: StateFlow<WallpaperTarget> = _target.asStateFlow()

    /** De dónde sale la imagen del cambio automático — obra del día (default, comportamiento
     * de siempre) o rotación de Favoritos (2026-08-21, pedido del usuario). */
    private val _source = MutableStateFlow(
        prefs.getString(KEY_SOURCE, null)?.let { runCatching { WallpaperSource.valueOf(it) }.getOrNull() }
            ?: WallpaperSource.DAILY_ARTWORK
    )
    val source: StateFlow<WallpaperSource> = _source.asStateFlow()

    fun setAutoChangeEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_AUTO_ENABLED, enabled) }
        _autoChangeEnabled.value = enabled
    }

    fun setTarget(target: WallpaperTarget) {
        prefs.edit { putString(KEY_TARGET, target.name) }
        _target.value = target
    }

    fun setSource(source: WallpaperSource) {
        prefs.edit { putString(KEY_SOURCE, source.name) }
        _source.value = source
    }

    /** Qué favorito se aplicó la última vez, para que `GetNextFavoriteWallpaperUseCase`
     * sepa cuál sigue en la rotación. No es una preferencia que el usuario elija — por eso
     * no es un `StateFlow`, nada en la UI necesita observarlo. */
    var lastFavoriteArtworkId: String?
        get() = prefs.getString(KEY_LAST_FAVORITE_ID, null)
        set(value) { prefs.edit { putString(KEY_LAST_FAVORITE_ID, value) } }

    private companion object {
        const val PREFS_NAME = "wallpaper_prefs"
        const val KEY_AUTO_ENABLED = "auto_change_enabled"
        const val KEY_TARGET = "target"
        const val KEY_SOURCE = "source"
        const val KEY_LAST_FAVORITE_ID = "last_favorite_artwork_id"
    }
}
