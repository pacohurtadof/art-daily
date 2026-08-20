package com.artdaily.app.domain.selection

import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.model.AvailableFilterOptions
import com.artdaily.core.repository.ArtworkRepository

/** Test double en memoria — evita depender de Room/un emulador para probar el dominio. */
class FakeArtworkRepository(private val artworks: List<Artwork>) : ArtworkRepository {

    override suspend fun getFiltered(filter: ArtworkFilter, minRankScore: Float): List<Artwork> =
        artworks.filter { a ->
            val year = a.creationYearStart
            val yearFrom = filter.yearFrom
            val yearTo = filter.yearTo
            (filter.period == null || a.period == filter.period) &&
                (filter.movement == null || a.movement == filter.movement) &&
                (filter.artistName == null || a.artistName == filter.artistName) &&
                (yearFrom == null || (year != null && year >= yearFrom)) &&
                (yearTo == null || (year != null && year <= yearTo)) &&
                a.rankScore >= minRankScore
        }

    override suspend fun getById(id: String): Artwork? = artworks.find { it.id == id }

    override suspend fun countFiltered(filter: ArtworkFilter, minRankScore: Float): Int =
        getFiltered(filter, minRankScore).size

    override suspend fun getAvailableFilterOptions(): AvailableFilterOptions = AvailableFilterOptions(
        periods = artworks.mapNotNull { it.period }.distinct(),
        movements = artworks.mapNotNull { it.movement }.distinct(),
        minYear = artworks.mapNotNull { it.creationYearStart }.minOrNull(),
        maxYear = artworks.mapNotNull { it.creationYearStart }.maxOrNull()
    )
}
