package com.artdaily.app.data.repository

import com.artdaily.app.data.local.ArtworkDao
import com.artdaily.app.data.local.toArtwork
import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.model.AvailableFilterOptions
import com.artdaily.core.repository.ArtworkRepository
import java.time.Year
import javax.inject.Inject

class ArtworkRepositoryImpl @Inject constructor(
    private val artworkDao: ArtworkDao
) : ArtworkRepository {

    override suspend fun getFiltered(filter: ArtworkFilter, minRankScore: Float): List<Artwork> =
        artworkDao.getFiltered(
            hasPeriods = filter.periods != null,
            periods = filter.periods ?: emptyList(),
            hasMovements = filter.movements != null,
            movements = filter.movements ?: emptyList(),
            artistName = filter.artistName,
            yearFrom = filter.yearFrom,
            yearTo = filter.yearTo,
            minRankScore = minRankScore
        ).map { it.toArtwork() }

    override suspend fun getById(id: String): Artwork? = artworkDao.getById(id)?.toArtwork()

    override suspend fun countFiltered(filter: ArtworkFilter, minRankScore: Float): Int =
        artworkDao.countFiltered(
            hasPeriods = filter.periods != null,
            periods = filter.periods ?: emptyList(),
            hasMovements = filter.movements != null,
            movements = filter.movements ?: emptyList(),
            artistName = filter.artistName,
            yearFrom = filter.yearFrom,
            yearTo = filter.yearTo,
            minRankScore = minRankScore
        )

    override suspend fun getAvailableFilterOptions(): AvailableFilterOptions = AvailableFilterOptions(
        periods = artworkDao.getDistinctPeriods(),
        movements = artworkDao.getDistinctMovements(),
        minYear = MIN_FILTERABLE_YEAR,
        maxYear = Year.now().value
    )

    private companion object {
        // Antes venía de MIN(creationYearStart) real de toda la base (~3050 a.C., por un
        // puñado de obras "other"/escultura/cerámica/joyería — casi nada de pintura ahí).
        // Feedback real del usuario (2026-08-20): con ese rango completo, el selector de
        // años quedaba difícil de manejar y sin sentido para pinturas, que en este catálogo
        // van de 740 en adelante (verificado: MIN(creationYearStart) WHERE
        // classification='painting' = 740, 813 obras). Se fijó el piso ahí a propósito —
        // deja ~114 obras muy antiguas (no-pintura) fuera del alcance de ESTE filtro
        // específico (siguen viéndose en Favoritos/Hoy si ya están ahí). Si el catálogo
        // suma pinturas más viejas en el futuro, re-evaluar este número.
        const val MIN_FILTERABLE_YEAR = 740
    }
}
