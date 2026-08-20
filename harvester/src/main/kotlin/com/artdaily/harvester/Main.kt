package com.artdaily.harvester

import com.artdaily.core.model.Artwork
import com.artdaily.core.ranking.RankScoreCalculator
import com.artdaily.harvester.aic.AicApi
import com.artdaily.harvester.aic.AicMapper
import com.artdaily.harvester.cma.CmaApi
import com.artdaily.harvester.cma.CmaMapper
import com.artdaily.harvester.met.MetApi
import com.artdaily.harvester.met.MetMapper
import com.artdaily.harvester.network.HttpClientFactory
import com.artdaily.harvester.rijks.RijksApi
import com.artdaily.harvester.rijks.RijksMapper
import com.artdaily.harvester.storage.ArtworkSqliteWriter
import com.artdaily.harvester.storage.DeltaJsonWriter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File

private const val MET_BASE_URL = "https://collectionapi.metmuseum.org/"
private const val AIC_BASE_URL = "https://api.artic.edu/"
private const val CMA_BASE_URL = "https://openaccess-api.clevelandart.org/"
private const val RIJKS_BASE_URL = "https://data.rijksmuseum.nl/"
private const val BATCH_SIZE = 100 // por consulta, por fuente — antes 25; sube para cosechas grandes

/**
 * Términos genéricos (en inglés — Met/AIC/CMA/Rijks indexan mayormente en inglés/neerlandés,
 * y estos matchean razonablemente en ambos) usados por el modo "bulk". Sujetos/escenas
 * comunes en pintura occidental, no movimientos ni periodos — evita el mismo error que ya
 * corregimos en AIC (adivinar clasificación desde texto libre).
 */
private val BULK_QUERY_TERMS = listOf(
    "portrait", "landscape", "still life", "flowers", "river", "mountain",
    "self portrait", "woman", "man", "child", "animals", "garden", "sea", "ship",
    "city", "winter", "night", "religious", "mythology", "horse", "dog", "music",
    "dance", "interior", "market", "forest", "sunset", "moon", "family", "house"
)

/**
 * Modo normal: `./gradlew :harvester:run --args="sunflowers"` — una sola búsqueda, rápido,
 * para pruebas puntuales.
 *
 * Modo bulk: `./gradlew :harvester:run --args="bulk 2000"` — itera [BULK_QUERY_TERMS] sobre
 * las 4 fuentes hasta acumular el total pedido (o agotar la lista de términos), pensado para
 * poblar la base de datos con un volumen real de prueba en vez de un puñado de obras.
 */
fun main(args: Array<String>) = runBlocking {
    if (args.getOrNull(0) == "bulk") {
        val target = args.getOrNull(1)?.toIntOrNull() ?: 2000
        val dbPath = args.getOrNull(2) ?: "output/artworks.db"
        runBulkHarvest(target, dbPath)
        return@runBlocking
    }

    val query = args.getOrNull(0) ?: "landscape"
    val dbPath = args.getOrNull(1) ?: "output/artworks.db"

    val artworks = harvestMet(query) + harvestAic(query) + harvestCma(query) + harvestRijks(query)

    println("\nMapeadas ${artworks.size} obras en total (Met + AIC + CMA + Rijks):\n")
    artworks.sortedByDescending { it.rankScore }.forEach { a ->
        println(
            "- [%.1f] (%s) %s — %s (%s) | periodo=%s movimiento=%s siglo=%s | %s".format(
                a.rankScore,
                a.sourceApi,
                a.title,
                a.artistName ?: "Artista desconocido",
                a.creationDateText ?: "fecha desconocida",
                a.period ?: "-",
                a.movement ?: "-",
                a.century?.toString() ?: "-",
                a.id
            )
        )
    }

    saveAndReportDelta(artworks, dbPath)
}

private suspend fun runBulkHarvest(target: Int, dbPath: String) {
    val writer = ArtworkSqliteWriter(dbPath)
    var totalNewOrChanged = 0
    var totalProcessed = 0
    val allNewOrChanged = mutableListOf<Artwork>()

    println("=== Cosecha bulk: objetivo $target obras, hasta ${BULK_QUERY_TERMS.size} términos ===")

    for ((index, term) in BULK_QUERY_TERMS.withIndex()) {
        if (totalNewOrChanged >= target) {
            println("\nObjetivo alcanzado ($totalNewOrChanged >= $target) — deteniendo antes de agotar los términos.")
            break
        }

        println("\n--- [${index + 1}/${BULK_QUERY_TERMS.size}] término: \"$term\" (acumulado: $totalNewOrChanged/$target) ---")
        val artworks = harvestMet(term) + harvestAic(term) + harvestCma(term) + harvestRijks(term)
        val result = writer.write(artworks)

        totalProcessed += result.totalProcessed
        totalNewOrChanged += result.newOrChanged.size
        allNewOrChanged += result.newOrChanged

        println("Ronda \"$term\": ${artworks.size} mapeadas, ${result.newOrChanged.size} nuevas/cambiadas.")
    }

    println("\n=== Cosecha bulk terminada: $totalNewOrChanged obras nuevas/cambiadas (de $totalProcessed procesadas) ===")
    println("Base de datos: ${File(dbPath).absolutePath}")

    val outputDir = File(dbPath).absoluteFile.parentFile?.path ?: "output"
    val deltaFile = DeltaJsonWriter.write(allNewOrChanged, outputDir)
    if (deltaFile != null) {
        println("Delta: ${allNewOrChanged.size} obras -> ${deltaFile.absolutePath}")
    } else {
        println("Delta: nada nuevo/cambiado, no se generó archivo.")
    }
}

private fun saveAndReportDelta(artworks: List<Artwork>, dbPath: String) {
    val result = ArtworkSqliteWriter(dbPath).write(artworks)
    println("\nGuardadas/actualizadas ${result.totalProcessed} obras en: ${File(dbPath).absolutePath}")

    val outputDir = File(dbPath).absoluteFile.parentFile?.path ?: "output"
    val deltaFile = DeltaJsonWriter.write(result.newOrChanged, outputDir)
    if (deltaFile != null) {
        println("Delta: ${result.newOrChanged.size} obras nuevas/cambiadas -> ${deltaFile.absolutePath}")
    } else {
        println("Delta: nada nuevo/cambiado desde la última corrida, no se generó archivo.")
    }
}

private suspend fun harvestMet(query: String): List<Artwork> {
    val metApi = HttpClientFactory.retrofit(MET_BASE_URL).create(MetApi::class.java)

    println("Buscando en el Met: q=\"$query\" hasImages=true ...")
    val searchResult = metApi.search(query = query, hasImages = true)
    println("Total de resultados: ${searchResult.total}")

    val candidateIds = searchResult.objectIDs.orEmpty().take(BATCH_SIZE)
    if (candidateIds.isEmpty()) {
        println("[met] Sin resultados para \"$query\".")
        return emptyList()
    }

    return buildList {
        for (id in candidateIds) {
            val dto = try {
                metApi.getObject(id)
            } catch (e: Exception) {
                System.err.println("  [met] objectID=$id -> error de red/parseo: ${e.message}")
                continue
            }
            val artwork = MetMapper.map(dto)
            if (artwork == null) {
                continue
            }
            add(artwork.copy(rankScore = RankScoreCalculator.calculate(artwork)))
            delay(150) // uso razonable — Met no exige key pero tampoco publica un rate limit
        }
    }
}

private suspend fun harvestAic(query: String): List<Artwork> {
    val aicApi = HttpClientFactory.retrofit(AIC_BASE_URL).create(AicApi::class.java)

    println("Buscando en AIC: q=\"$query\" ...")
    val searchResult = aicApi.search(query = query, limit = BATCH_SIZE)
    println("Total de resultados: ${searchResult.pagination.total}")

    if (searchResult.data.isEmpty()) {
        println("[aic] Sin resultados para \"$query\".")
        return emptyList()
    }

    return buildList {
        for (summary in searchResult.data) {
            val dto = try {
                aicApi.getArtwork(summary.id).data
            } catch (e: Exception) {
                System.err.println("  [aic] id=${summary.id} -> error de red/parseo: ${e.message}")
                continue
            }
            val artwork = AicMapper.map(dto)
            if (artwork == null) {
                continue
            }
            add(artwork.copy(rankScore = RankScoreCalculator.calculate(artwork)))
            delay(150) // mismo criterio de uso razonable que con el Met
        }
    }
}

private suspend fun harvestCma(query: String): List<Artwork> {
    val cmaApi = HttpClientFactory.retrofit(CMA_BASE_URL).create(CmaApi::class.java)

    println("Buscando en CMA (Cleveland): q=\"$query\" ...")
    val searchResult = cmaApi.search(query = query, limit = BATCH_SIZE)
    println("Total de resultados: ${searchResult.info.total}")

    if (searchResult.data.isEmpty()) {
        println("[cma] Sin resultados para \"$query\".")
        return emptyList()
    }

    // A diferencia de Met/AIC, la búsqueda de CMA ya trae el objeto completo — no hace
    // falta una segunda llamada por obra para el detalle.
    return searchResult.data.mapNotNull { dto ->
        CmaMapper.map(dto)?.let { artwork -> artwork.copy(rankScore = RankScoreCalculator.calculate(artwork)) }
    }
}

private suspend fun harvestRijks(query: String): List<Artwork> {
    val rijksApi = HttpClientFactory.retrofit(RIJKS_BASE_URL).create(RijksApi::class.java)

    println("Buscando en Rijksmuseum: title=\"$query\" ...")
    val searchResult = rijksApi.search(title = query)
    println("Total de resultados: ${searchResult.partOf?.totalItems ?: 0}")

    val candidateIds = searchResult.orderedItems.take(BATCH_SIZE)
    if (candidateIds.isEmpty()) {
        println("[rijks] Sin resultados para \"$query\".")
        return emptyList()
    }

    return buildList {
        for (item in candidateIds) {
            val dto = try {
                rijksApi.resolve(item.id)
            } catch (e: Exception) {
                System.err.println("  [rijks] id=${item.id} -> error de red/parseo: ${e.message}")
                continue
            }
            val artwork = RijksMapper.map(dto)
            if (artwork == null) {
                continue
            }
            add(artwork.copy(rankScore = RankScoreCalculator.calculate(artwork)))
            delay(150) // mismo criterio de uso razonable que con las demás fuentes
        }
    }
}
