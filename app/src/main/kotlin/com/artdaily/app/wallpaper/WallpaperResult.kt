package com.artdaily.app.wallpaper

/** Resultado de aplicar un fondo de pantalla — compartido entre `DetailViewModel` (botón
 * manual) y `SettingsViewModel` (al activar el cambio automático, para dar feedback
 * inmediato en vez de que el usuario espere hasta el próximo ciclo de 24h del worker). */
enum class WallpaperResult { SUCCESS, ERROR }
