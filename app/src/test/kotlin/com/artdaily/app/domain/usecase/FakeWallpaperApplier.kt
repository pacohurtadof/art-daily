package com.artdaily.app.domain.usecase

import com.artdaily.app.wallpaper.WallpaperApplier
import com.artdaily.app.wallpaper.WallpaperTarget

/** Test double en memoria — no descarga ni aplica nada de verdad, solo registra qué se le
 * pidió y devuelve [nextResult] (configurable, para simular una falla de red/decodificación
 * puntual sin encadenarla a la lógica real de Coil/WallpaperManager). */
class FakeWallpaperApplier(private var nextResult: Boolean = true) : WallpaperApplier {

    var applyCallCount = 0
        private set
    var lastAppliedImageUrl: String? = null
        private set
    var lastAppliedTarget: WallpaperTarget? = null
        private set

    fun setNextResult(result: Boolean) { nextResult = result }

    override suspend fun apply(imageUrl: String?, target: WallpaperTarget): Boolean {
        applyCallCount++
        lastAppliedImageUrl = imageUrl
        lastAppliedTarget = target
        if (imageUrl.isNullOrBlank()) return false
        return nextResult
    }
}
