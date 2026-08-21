package com.artdaily.core.model

/** Valores realmente presentes en la base de datos hoy — no un catálogo fijo, para no
 * ofrecer un filtro que no matchee ninguna obra.
 *
 * `minYear`/`maxYear` son los bordes del selector de rango de años (2026-08-19, reemplazó
 * a `museums`/`centuries` — ver `ArtworkFilter`). A diferencia de `periods`/`movements`,
 * NO son un `MIN`/`MAX` crudo de la base — `ArtworkRepositoryImpl` los fija a propósito
 * (piso en el año real de la pintura más antigua del catálogo, no el de toda la base —
 * el resto son casi todo esculturas/cerámica/"other"; techo siempre en el año actual, no
 * en la obra más nueva cosechada) — ver el comentario ahí para el porqué exacto. */
data class AvailableFilterOptions(
    val periods: List<String> = emptyList(),
    val movements: List<String> = emptyList(),
    val minYear: Int? = null,
    val maxYear: Int? = null
)
