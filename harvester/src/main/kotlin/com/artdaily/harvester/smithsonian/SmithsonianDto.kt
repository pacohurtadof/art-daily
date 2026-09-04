package com.artdaily.harvester.smithsonian

import kotlinx.serialization.Serializable

/**
 * DTOs del "Open Access API" de Smithsonian (`api.si.edu/openaccess/api/v1.0/search`).
 * Esquema real (EDAN/IMM) verificado en vivo el 2026-09-04 contra la API real, con key propia
 * del usuario — no es una suposición, ver `docs/bitacora.md`. Estructura mucho más anidada
 * que Met/AIC/CMA (viene de un modelo pensado originalmente para XML), y a propósito solo se
 * mapean los campos que usa [SmithsonianMapper] — `ignoreUnknownKeys` en `HttpClientFactory`
 * se encarga del resto.
 */
@Serializable
data class SmithsonianSearchResponse(
    val response: SmithsonianResponseBody = SmithsonianResponseBody()
)

@Serializable
data class SmithsonianResponseBody(
    val rows: List<SmithsonianRow> = emptyList(),
    val rowCount: Int = 0
)

@Serializable
data class SmithsonianRow(
    val id: String,
    val title: String? = null,
    val unitCode: String? = null,
    val content: SmithsonianContent? = null
)

@Serializable
data class SmithsonianContent(
    val freetext: SmithsonianFreetext? = null,
    val descriptiveNonRepeating: SmithsonianDescriptiveNonRepeating? = null,
    val indexedStructured: SmithsonianIndexedStructured? = null
)

@Serializable
data class SmithsonianFreetext(
    val date: List<SmithsonianLabelContent> = emptyList(),
    val name: List<SmithsonianLabelContent> = emptyList(),
    val creditLine: List<SmithsonianLabelContent> = emptyList(),
    val identifier: List<SmithsonianLabelContent> = emptyList(),
    val objectType: List<SmithsonianLabelContent> = emptyList(),
    val physicalDescription: List<SmithsonianLabelContent> = emptyList(),
    val notes: List<SmithsonianLabelContent> = emptyList()
)

@Serializable
data class SmithsonianLabelContent(
    val label: String? = null,
    val content: String? = null
)

@Serializable
data class SmithsonianDescriptiveNonRepeating(
    val record_ID: String? = null,
    val unit_code: String? = null,
    val data_source: String? = null,
    val record_link: String? = null,
    val online_media: SmithsonianOnlineMedia? = null
)

@Serializable
data class SmithsonianOnlineMedia(
    val media: List<SmithsonianMedia> = emptyList()
)

@Serializable
data class SmithsonianMedia(
    val type: String? = null,
    val usage: SmithsonianUsage? = null,
    val content: String? = null,
    val thumbnail: String? = null,
    val resources: List<SmithsonianMediaResource> = emptyList()
)

@Serializable
data class SmithsonianUsage(
    val access: String? = null
)

@Serializable
data class SmithsonianMediaResource(
    val label: String? = null,
    val url: String? = null
)

@Serializable
data class SmithsonianIndexedStructured(
    val date: List<String> = emptyList()
)
