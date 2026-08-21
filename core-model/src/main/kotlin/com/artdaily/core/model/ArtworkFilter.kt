package com.artdaily.core.model

/**
 * Combinación de filtros para pedir obras — cualquier campo en `null` significa "no filtrar
 * por esto". Un `WidgetConfigEntity` (en `:app`) se traduce a este objeto antes de consultar.
 *
 * `museum`/`century` (chips) se reemplazaron el 2026-08-19 por `yearFrom`/`yearTo` — un
 * selector de rango de años reemplaza al filtro por siglo (más preciso, sin la aspereza de
 * "siglo entero"), y se sacó el filtro por museo de la UI (decisión de producto, ver
 * `docs/bitacora.md`). Coinciden si `yearFrom <= creationYearStart <= yearTo`.
 */
data class ArtworkFilter(
    // Listas en vez de un solo valor — Explorar permite elegir varios periodos/movimientos
    // a la vez (ej. Impresionismo + Expresionismo juntos), no solo uno (2026-08-21). La
    // config de widget sigue siendo single-select en su UI, pero arma una lista de 0 o 1
    // elemento para reusar este mismo filtro.
    val periods: List<String>? = null,
    val movements: List<String>? = null,
    val artistName: String? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null
)
