package com.artdaily.app.data.settings

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Un solo valor — `SharedPreferences` de toda la vida alcanza, no hace falta traer
 * DataStore para esto. Expuesto como `StateFlow` para que `SettingsScreen` se recomponga
 * sola al cambiarlo, sin re-leer `SharedPreferences` a mano.
 *
 * Antes también guardaba a qué pantalla(s) aplicar el fondo (`target`) — se sacó el
 * 2026-08-19 (feedback real del usuario: era redundante, el diálogo manual de Detalle ya
 * pregunta lo mismo cada vez que se usa). El cambio automático ahora usa siempre
 * `WallpaperTarget.BOTH` fijo (ver `DailyArtworkWorker`) — no hay a quién preguntarle
 * cuando corre solo, así que no tiene sentido una preferencia separada para eso tampoco.
 */
@Singleton
class WallpaperPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _autoChangeEnabled = MutableStateFlow(prefs.getBoolean(KEY_AUTO_ENABLED, false))
    /** Apagado por defecto — cambiar el fondo de pantalla del usuario sin que lo pida es
     * invasivo, tiene que activarlo a propósito desde Ajustes. */
    val autoChangeEnabled: StateFlow<Boolean> = _autoChangeEnabled.asStateFlow()

    fun setAutoChangeEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_AUTO_ENABLED, enabled) }
        _autoChangeEnabled.value = enabled
    }

    private companion object {
        const val PREFS_NAME = "wallpaper_prefs"
        const val KEY_AUTO_ENABLED = "auto_change_enabled"
    }
}
