package com.artdaily.app.wallpaper

/**
 * Interfaz extraída el 2026-09-28 (junto con `WallpaperPreferences`, mismo motivo: poder
 * "fakearla" en tests JVM sin Robolectric) — ver `WallpaperApplierImpl` para la
 * implementación real (descarga con Coil + `WallpaperManager`).
 */
interface WallpaperApplier {

    /** true si se aplicó, false si no había imagen o algo falló (red, decodificación). No
     * relanza — quien llama (botón manual o worker diario) decide cómo avisar del error. */
    suspend fun apply(imageUrl: String?, target: WallpaperTarget): Boolean
}
