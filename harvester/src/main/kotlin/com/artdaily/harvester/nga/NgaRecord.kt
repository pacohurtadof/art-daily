package com.artdaily.harvester.nga

/**
 * Registro ya unido (join) de las 5 tablas CSV de NGA que hacen falta para mapear una obra:
 * `objects`, `published_images` (filtrado a `openaccess=1`), `objects_terms` (termType
 * "Style"/"School"), `objects_constituents` + `constituents` (artista principal). Ver
 * [NgaCsvIngester] para el armado del join y `docs/bitacora.md` (2026-09-04) para el detalle
 * de por qué se necesitan justo estas 5 tablas.
 *
 * Separado de [NgaMapper] a propósito, mismo motivo que los DTO de Met/AIC/CMA/Rijks: que el
 * mapeo a [com.artdaily.core.model.Artwork] sea una función pura y testeable sin tener que
 * parsear CSVs de verdad en los tests.
 */
data class NgaRecord(
    val objectId: String,
    val title: String?,
    val displayDate: String?,
    val beginYear: Int?,
    val endYear: Int?,
    val medium: String?,
    val attribution: String?,
    val creditLine: String?,
    val classification: String?,
    val dimensions: String?,
    val accessionNum: String?,
    val wikidataId: String?,
    val artistBeginYear: Int?,
    val artistEndYear: Int?,
    /** Valores crudos de `objects_terms` con termType="Style" (ej. "Impressionist", "Baroque"). */
    val styleTerms: List<String>,
    /** Valores crudos de `objects_terms` con termType="School" — en la práctica son
     * nacionalidad/escuela de origen (ej. "Dutch", "American"), no movimiento artístico. */
    val schoolTerms: List<String>,
    /** Base IIIF (`published_images.iiifurl`) de la imagen principal open-access elegida. */
    val imageIiifUrl: String
)
