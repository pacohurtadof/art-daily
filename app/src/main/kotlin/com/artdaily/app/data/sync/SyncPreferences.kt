package com.artdaily.app.data.sync

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Un solo valor — mismo criterio que `WallpaperPreferences`: `SharedPreferences` alcanza,
 * no hace falta DataStore. Guarda el tag del último release ya sincronizado, para no
 * volver a descargar/procesar el mismo `delta.json` en cada corrida del worker. */
@Singleton
class SyncPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var lastSyncedTag: String?
        get() = prefs.getString(KEY_LAST_SYNCED_TAG, null)
        set(value) = prefs.edit { putString(KEY_LAST_SYNCED_TAG, value) }

    private companion object {
        const val PREFS_NAME = "sync_prefs"
        const val KEY_LAST_SYNCED_TAG = "last_synced_tag"
    }
}
