package com.artdaily.harvester.cma

import kotlinx.serialization.Serializable

/**
 * GET /api/artworks/{id} (y cada elemento de /api/artworks/). Verificado en vivo el
 * 2026-08-18. A diferencia de Met/AIC, CMA no da un nombre de artista "limpio" separado —
 * solo `creators[].description`, que ya viene con nacionalidad/fechas incluidas
 * (ej. "Louis Hayet (French, 1864–1940)"). Se usa tal cual en vez de parsearlo con regex
 * para separar el nombre — mismo criterio que con AIC: mejor no adivinar que adivinar mal.
 */
@Serializable
data class CmaArtworkDto(
    val id: Int,
    val accession_number: String = "",
    val share_license_status: String = "",
    val title: String = "",
    val creation_date: String = "",
    val creation_date_earliest: Int? = null,
    val creation_date_latest: Int? = null,
    val culture: List<String> = emptyList(),
    val type: String = "",
    val creators: List<CmaCreatorDto> = emptyList(),
    val url: String = "",
    val images: CmaImagesDto? = null,
    val creditline: String = "",
    val measurements: String = "",
    val is_highlight: Boolean = false,
    // Reseña curatorial real — a diferencia de AIC, CMA la marca CC0 igual que el resto de
    // sus datos (`share_license_status`/`copyright` por objeto, verificado en vivo el
    // 2026-08-19), sin exigir atribución. No estaba declarada acá antes: kotlinx.serialization
    // ignora en silencio las claves del JSON que no tienen campo correspondiente, así que este
    // texto llegaba y se descartaba sin que nada fallara — bug real, no una ausencia de la API.
    val description: String? = null
)

@Serializable
data class CmaCreatorDto(
    val description: String = "",
    val birth_year: String = "",
    val death_year: String = ""
)

@Serializable
data class CmaImagesDto(
    val web: CmaImageVariantDto? = null,
    val print: CmaImageVariantDto? = null
)

@Serializable
data class CmaImageVariantDto(val url: String = "")
