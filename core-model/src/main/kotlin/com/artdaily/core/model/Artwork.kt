package com.artdaily.core.model

import kotlinx.serialization.Serializable

/**
 * Modelo común de obra de arte, compartido entre el harvester y la app Android.
 *
 * Definido originalmente en `docs/etapa2-diseno-arquitectura.md` (sección 2).
 * Diferencia respecto al documento original: se añadió [museumFlaggedHighlight],
 * que faltaba en el data class pero era usado por `calculateRankScore` (sección 6).
 *
 * `@Serializable` porque este mismo modelo se usa para escribir/leer `artworks-delta-*.json`
 * directamente (sin un DTO intermedio) — ver `harvester/storage/DeltaJsonWriter.kt`.
 */
@Serializable
data class Artwork(
    val id: String,                    // "met:45734" o "aic:99652" — prefijo de fuente + id nativo
    val title: String,
    val artistName: String?,
    val artistBirthYear: Int?,
    val artistDeathYear: Int?,
    val creationDateText: String?,      // texto original, ej. "c. 1665"
    val creationYearStart: Int?,        // normalizado, para filtrar por siglo/rango
    val creationYearEnd: Int?,
    val period: String?,                // normalizado vía PeriodNormalizer
    val movement: String?,              // normalizado vía MovementNormalizer
    val century: Int?,                  // derivado de creationYearStart; negativo = siglo a.C.
    val culture: String?,
    val country: String?,
    val classification: String,         // "painting" | "sculpture" | "print" | ... (normalizado)
    val museum: String,                 // "The Metropolitan Museum of Art"
    val museumId: String,               // id original en la fuente
    val imageUrlFull: String?,
    val imageUrlThumbnail: String?,
    val sourceUrl: String,              // ficha oficial en la web del museo
    val sourceApi: String,              // "met" | "aic"
    val license: String,                // "CC0" (por ahora siempre CC0, dado el filtro de fuentes)
    val isPublicDomain: Boolean,
    val description: String?,           // reseña curatorial real cuando la fuente la da (Rijksmuseum/CMA/AIC) — no la línea de crédito, ver [creditLine]
    val creditLine: String?,            // ej. "Gift of Leonard C. Hanna Jr." — atribución de donación, no historia
    val descriptionAttribution: String?, // no-null solo si [description] exige mostrar atribución visible (ej. AIC: CC BY 4.0, a diferencia del resto de sus datos que son CC0)
    val dimensions: String?,
    val accessionNumber: String?,
    val museumFlaggedHighlight: Boolean, // ej. Met `isHighlight` — señal fuerte para el ranking
    val rankScore: Float,               // ver RankScoreCalculator
    val harvestedAt: Long,              // timestamp de la última cosecha, para auditar
    // 2026-08-28 (pedido del usuario: "priorizar obras más conocidas para que no se
    // aburra de ver obras que no conoce"). Ninguna fuente expone una señal de fama real
    // (rankScore mide COMPLETITUD de metadatos, no reconocimiento — ver
    // RankScoreCalculator). Se evaluaron dos caminos: heurística automática vía Wikidata/
    // Wikipedia (escala sola, pero el cruce por título/artista es impreciso y puede fallar
    // en silencio) o curaduría manual obra por obra (mismo patrón ya usado para
    // `movement`). El usuario eligió curaduría manual a propósito, por precisión — ver
    // `harvester/IconicOverrides.kt`/`harvester/data/iconic-overrides.txt`. Default
    // `false`: la enorme mayoría del catálogo no está revisada todavía.
    val isIconic: Boolean = false
)
