package com.artdaily.harvester.met

import kotlinx.serialization.Serializable

/**
 * GET /public/collection/v1/objects/{objectID}
 *
 * Solo se listan los campos que usa [MetMapper]. Verificado contra la respuesta real de la
 * API el 2026-08-17 (objectID 45734) — ojo, varios campos que uno esperaría `Int` llegan
 * como texto: [artistBeginDate]/[artistEndDate] son `String`, no `Int` (a diferencia de
 * [objectBeginDate]/[objectEndDate], que sí son `Int`). Campos "ausentes" en la fuente
 * llegan como `""`, no se omiten — de ahí que casi todo sea `String` no-nulo con default,
 * y se limpie después con `blankToNull()` en el mapper.
 */
@Serializable
data class MetObjectDto(
    val objectID: Int,
    val isHighlight: Boolean = false,
    val isPublicDomain: Boolean = false,
    val accessionNumber: String = "",
    val primaryImage: String = "",
    val primaryImageSmall: String = "",
    val title: String = "",
    val culture: String = "",
    val period: String = "",
    val classification: String = "",
    val objectName: String = "",
    val objectDate: String = "",
    val objectBeginDate: Int? = null,
    val objectEndDate: Int? = null,
    val dimensions: String = "",
    val creditLine: String = "",
    val country: String = "",
    val objectURL: String = "",
    val artistDisplayName: String = "",
    val artistBeginDate: String = "",
    val artistEndDate: String = "",
    val constituents: List<MetConstituentDto>? = null
)

@Serializable
data class MetConstituentDto(
    val name: String = "",
    val role: String = ""
)
