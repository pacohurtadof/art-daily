package com.artdaily.core.model

/** Valores realmente presentes en la base de datos hoy — no un catálogo fijo, para no
 * ofrecer un filtro que no matchee ninguna obra. */
data class AvailableFilterOptions(
    val periods: List<String> = emptyList(),
    val movements: List<String> = emptyList(),
    val museums: List<String> = emptyList(),
    val centuries: List<Int> = emptyList()
)
