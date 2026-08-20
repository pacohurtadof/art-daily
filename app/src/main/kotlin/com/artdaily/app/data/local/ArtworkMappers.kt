package com.artdaily.app.data.local

import com.artdaily.core.model.Artwork

/** `ArtworkEntity` (Room, en `:app`) <-> `Artwork` (modelo común, en `:core-model`). */

fun ArtworkEntity.toArtwork(): Artwork = Artwork(
    id = id,
    title = title,
    artistName = artistName,
    artistBirthYear = artistBirthYear,
    artistDeathYear = artistDeathYear,
    creationDateText = creationDateText,
    creationYearStart = creationYearStart,
    creationYearEnd = creationYearEnd,
    period = period,
    movement = movement,
    century = century,
    culture = culture,
    country = country,
    classification = classification,
    museum = museum,
    museumId = museumId,
    imageUrlFull = imageUrlFull,
    imageUrlThumbnail = imageUrlThumbnail,
    sourceUrl = sourceUrl,
    sourceApi = sourceApi,
    license = license,
    isPublicDomain = isPublicDomain,
    description = description,
    creditLine = creditLine,
    descriptionAttribution = descriptionAttribution,
    dimensions = dimensions,
    accessionNumber = accessionNumber,
    museumFlaggedHighlight = museumFlaggedHighlight,
    rankScore = rankScore,
    harvestedAt = harvestedAt
)

/** Usado al sincronizar `artworks-delta-*.json` hacia Room (pendiente: worker de sync). */
fun Artwork.toEntity(): ArtworkEntity = ArtworkEntity(
    id = id,
    title = title,
    artistName = artistName,
    artistBirthYear = artistBirthYear,
    artistDeathYear = artistDeathYear,
    creationDateText = creationDateText,
    creationYearStart = creationYearStart,
    creationYearEnd = creationYearEnd,
    period = period,
    movement = movement,
    century = century,
    culture = culture,
    country = country,
    classification = classification,
    museum = museum,
    museumId = museumId,
    imageUrlFull = imageUrlFull,
    imageUrlThumbnail = imageUrlThumbnail,
    sourceUrl = sourceUrl,
    sourceApi = sourceApi,
    license = license,
    isPublicDomain = isPublicDomain,
    description = description,
    creditLine = creditLine,
    descriptionAttribution = descriptionAttribution,
    dimensions = dimensions,
    accessionNumber = accessionNumber,
    museumFlaggedHighlight = museumFlaggedHighlight,
    rankScore = rankScore,
    harvestedAt = harvestedAt
)
