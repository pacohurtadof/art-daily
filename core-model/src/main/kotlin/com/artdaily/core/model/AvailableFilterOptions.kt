package com.artdaily.core.model

/** Valores realmente presentes en la base de datos hoy — no un catálogo fijo, para no
 * ofrecer un filtro que no matchee ninguna obra.
 *
 * `minYear`/`maxYear` son los bordes reales del selector de rango de años (2026-08-19,
 * reemplazó a `museums`/`centuries` — ver `ArtworkFilter`); `null` si no hay ninguna obra
 * con `creationYearStart` conocido. */
data class AvailableFilterOptions(
    val periods: List<String> = emptyList(),
    val movements: List<String> = emptyList(),
    val minYear: Int? = null,
    val maxYear: Int? = null
)
