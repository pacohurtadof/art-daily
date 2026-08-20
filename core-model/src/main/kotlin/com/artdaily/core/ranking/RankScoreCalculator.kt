package com.artdaily.core.ranking

import com.artdaily.core.model.Artwork

/**
 * Fórmula simple y explicable (no ML) para priorizar obras con metadatos completos en el
 * pool de selección aleatoria. Definida en `docs/etapa2-diseno-arquitectura.md`, sección 6.
 *
 * Vive en `:core-model` (no en `:harvester`) porque en teoría se podría recalcular en la app
 * si en el futuro se re-normalizan campos localmente; por ahora solo la llama el harvester.
 */
object RankScoreCalculator {
    fun calculate(a: Artwork): Float {
        var score = 0f
        if (!a.artistName.isNullOrBlank()) score += 1f
        if (!a.period.isNullOrBlank()) score += 1f
        if (!a.movement.isNullOrBlank()) score += 1f
        if (!a.description.isNullOrBlank()) score += 1f
        if (a.creationYearStart != null) score += 1f
        if (a.imageUrlFull != null) score += 1f
        if (a.museumFlaggedHighlight) score += 3f
        return score
    }
}
