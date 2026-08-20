package com.artdaily.app.data.repository

import com.artdaily.app.data.local.ArtworkDao
import com.artdaily.app.data.local.toArtwork
import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.model.AvailableFilterOptions
import com.artdaily.core.repository.ArtworkRepository
import javax.inject.Inject

class ArtworkRepositoryImpl @Inject constructor(
    private val artworkDao: ArtworkDao
) : ArtworkRepository {

    override suspend fun getFiltered(filter: ArtworkFilter, minRankScore: Float): List<Artwork> =
        artworkDao.getFiltered(
            period = filter.period,
            century = filter.century,
            movement = filter.movement,
            artistName = filter.artistName,
            museum = filter.museum,
            minRankScore = minRankScore
        ).map { it.toArtwork() }

    override suspend fun getById(id: String): Artwork? = artworkDao.getById(id)?.toArtwork()

    override suspend fun countFiltered(filter: ArtworkFilter, minRankScore: Float): Int =
        artworkDao.countFiltered(
            period = filter.period,
            century = filter.century,
            movement = filter.movement,
            artistName = filter.artistName,
            museum = filter.museum,
            minRankScore = minRankScore
        )

    override suspend fun getAvailableFilterOptions(): AvailableFilterOptions = AvailableFilterOptions(
        periods = artworkDao.getDistinctPeriods(),
        movements = artworkDao.getDistinctMovements(),
        museums = artworkDao.getDistinctMuseums(),
        centuries = artworkDao.getDistinctCenturies()
    )
}
