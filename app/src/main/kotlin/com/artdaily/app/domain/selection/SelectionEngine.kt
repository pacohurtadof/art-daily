package com.artdaily.app.domain.selection

import com.artdaily.app.data.local.HistoryDao
import com.artdaily.core.model.Artwork
import com.artdaily.core.model.ArtworkFilter
import com.artdaily.core.repository.ArtworkRepository
import javax.inject.Inject
import kotlin.random.Random

/**
 * `docs/etapa2-diseno-arquitectura.md`, sección 9 — random ponderado + anti-repetición.
 * Respecto al pseudocódigo original: ahí recibía un `dao: ArtworkDao` y llamaba
 * `dao.getRecentHistory(...)` en el mismo objeto; aquí se separó en `ArtworkRepository`
 * (filtro) + `HistoryDao` (historial), porque son responsabilidades de tablas distintas.
 */
class SelectionEngine @Inject constructor(
    private val artworkRepository: ArtworkRepository,
    private val historyDao: HistoryDao
) {
    /** Mutable a propósito, solo para poder inyectar un `Random` con seed fija en tests
     * (`SelectionEngineTest`) sin que Hilt tenga que resolver un binding de `Random` en el
     * grafo — Dagger/Hilt no respeta valores default de parámetros de constructor Kotlin
     * (llama al constructor primario con todos los argumentos explícitos), así que un
     * default ahí rompería la compilación de Hilt. En producción siempre queda en
     * `Random.Default`. */
    internal var random: Random = Random.Default

    suspend fun pickForWidget(widgetId: Int, filter: ArtworkFilter, avoidRepeatDays: Int): Artwork? {
        val candidates = artworkRepository.getFiltered(filter)
        if (candidates.isEmpty()) return null

        val sinceEpochMillis = System.currentTimeMillis() - avoidRepeatDays * DAY_MILLIS
        val recentlyShown = historyDao.getRecentArtworkIds(widgetId, sinceEpochMillis).toSet()

        // Si el pool de candidatos es más chico que la ventana de "no repetir", el ciclo se
        // reinicia solo (se vuelve a permitir repetir) en vez de devolver null — decisión ya
        // tomada en la Etapa 1, sección "Riesgos de producto".
        val pool = candidates.filterNot { it.id in recentlyShown }.ifEmpty { candidates }

        // 2026-08-28 (pedido del usuario: "priorizar obras más conocidas para que no se
        // aburra de ver obras que no conoce"). `isIconic` viene de una curaduría manual
        // obra por obra (ver `IconicOverrides` en el harvester) — cobertura de cientos de
        // obras, no de las 10.000+ del catálogo entero. Por eso NO se fuerza a elegir
        // siempre de ahí (se agotaría rápido y el "una obra distinta cada día" perdería la
        // gracia de explorar el catálogo real): se sortea con `ICONIC_BIAS` de probabilidad
        // tomar del sub-pool icónico cuando hay alguno disponible hoy, y el resto de las
        // veces del pool completo (que igual puede incluir obras icónicas al azar).
        val iconicPool = pool.filter { it.isIconic }
        val effectivePool = if (iconicPool.isNotEmpty() && random.nextFloat() < ICONIC_BIAS) {
            iconicPool
        } else {
            pool
        }
        return effectivePool.randomOrNull(random)
    }

    private companion object {
        const val DAY_MILLIS = 24L * 60 * 60 * 1000
        const val ICONIC_BIAS = 0.6f
    }
}
