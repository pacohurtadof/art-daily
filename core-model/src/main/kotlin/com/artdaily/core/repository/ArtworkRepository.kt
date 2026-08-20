package com.artdaily.core.repository

import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.model.AvailableFilterOptions

/**
 * Contrato puro Kotlin (sin Room/Android) — la implementación real vive en `:app`
 * (`ArtworkRepositoryImpl`). Vivir aquí, en `:core-model`, es la misma razón por la que el
 * módulo existe: si algún día hay una versión iOS/KMP, esta interfaz se reutiliza tal cual.
 */
interface ArtworkRepository {
    suspend fun getFiltered(filter: ArtworkFilter, minRankScore: Float = 3f): List<Artwork>
    suspend fun getById(id: String): Artwork?
    suspend fun countFiltered(filter: ArtworkFilter, minRankScore: Float = 0f): Int
    suspend fun getAvailableFilterOptions(): AvailableFilterOptions
}
