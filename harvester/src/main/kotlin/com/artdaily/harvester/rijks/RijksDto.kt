package com.artdaily.harvester.rijks

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/** GET /search/collection — devuelve solo identificadores LOD, sin metadata (verificado
 * en vivo el 2026-08-18). Cada uno se resuelve por separado con [RijksAggregationDto]. */
@Serializable
data class RijksSearchResponse(
    val orderedItems: List<RijksSearchItem> = emptyList(),
    val partOf: RijksPartOf? = null
)

@Serializable
data class RijksSearchItem(val id: String = "")

@Serializable
data class RijksPartOf(val totalItems: Int = 0)

/**
 * GET {id}?_profile=edm-framed — la representación EDM (Europeana Data Model) en
 * JSON-LD "framed", NO el modelo Linked Art por defecto de ese mismo endpoint.
 *
 * Verificado en vivo el 2026-08-18: pedir el default (Linked Art) da un grafo CIDOC-CRM de
 * ~4000 líneas por objeto, y la imagen ni siquiera está ahí — hay que resolver 3 saltos más
 * (objeto -> VisualItem -> DigitalObject -> access_point). EDM framed trae todo en una sola
 * llamada, incluida `isShownBy` (imagen) y `edmRights` (licencia). `aggregatedCHO` se deja
 * como `JsonObject` crudo en vez de data classes estrictas — el mismo campo (título, fecha,
 * etc.) llega en formas distintas según el objeto; ver `RijksJsonHelpers.kt`.
 */
@Serializable
data class RijksAggregationDto(
    val id: String = "",
    val edmRights: String? = null,
    val isShownBy: RijksWebResourceDto? = null,
    val isShownAt: RijksWebResourceDto? = null,
    val aggregatedCHO: JsonObject = JsonObject(emptyMap())
)

@Serializable
data class RijksWebResourceDto(val id: String = "")
