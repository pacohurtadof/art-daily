package com.artdaily.app.data.settings

import com.artdaily.app.wallpaper.WallpaperSource
import com.artdaily.app.wallpaper.WallpaperTarget
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz extraída el 2026-09-28 (mismo día del fix del bug de "el wallpaper cambia por la
 * tarde de forma aleatoria") — antes era una clase concreta atada a `Context`/
 * `SharedPreferences`, imposible de "fakear" en un test JVM sin Robolectric. Eso era, en la
 * práctica, la razón real por la que nadie había escrito un test para
 * `GetNextFavoriteWallpaperUseCase` ni para la guarda diaria del wallpaper automático — no
 * es que se hubiera decidido no probarlos, es que la pieza de la que dependían no se podía
 * probar. Ver `WallpaperPreferencesImpl` para la implementación real (con `SharedPreferences`)
 * y `FakeWallpaperPreferences` (en `app/src/test/.../domain/usecase/`) para el test double.
 *
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
interface WallpaperPreferences {

    /** Apagado por defecto — cambiar el fondo de pantalla del usuario sin que lo pida es
     * invasivo, tiene que activarlo a propósito desde Ajustes. */
    val autoChangeEnabled: StateFlow<Boolean>

    val target: StateFlow<WallpaperTarget>

    /** De dónde sale la imagen del cambio automático — obra del día (default, comportamiento
     * de siempre) o rotación de Favoritos (2026-08-21, pedido del usuario). */
    val source: StateFlow<WallpaperSource>

    fun setAutoChangeEnabled(enabled: Boolean)

    fun setTarget(target: WallpaperTarget)

    fun setSource(source: WallpaperSource)

    /** Qué favorito se aplicó la última vez, para que `GetNextFavoriteWallpaperUseCase`
     * sepa cuál sigue en la rotación. No es una preferencia que el usuario elija — por eso
     * no es un `StateFlow`, nada en la UI necesita observarlo. */
    var lastFavoriteArtworkId: String?

    /** Último día (epoch day, zona local) en que el cambio AUTOMÁTICO de fondo de pantalla
     * se aplicó de verdad — `ApplyAutomaticWallpaperUseCase` lo revisa antes de reaplicar,
     * para no hacerlo dos veces el mismo día de calendario. Sin esto, cada corrida "extra"
     * del worker (no solo la periódica de medianoche — `ArtWidgetReceiver.onUpdate()`
     * también la dispara al reiniciar el dispositivo o reinstalar la app, no solo al
     * agregar un widget) volvía a aplicar el fondo: con fuente Favoritos, avanzaba la
     * rotación una posición extra cada vez, así que el fondo terminaba cambiando otra vez a
     * cualquier hora del día (bug real reportado por el usuario, 2026-09-28: "a veces se
     * cambia por la tarde de forma aleatoria"). No es una preferencia que el usuario elija,
     * mismo motivo que `lastFavoriteArtworkId` de arriba. */
    var lastAutoAppliedEpochDay: Long?
}
