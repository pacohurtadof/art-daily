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
private const val BATCH_SIZE = 150 // por consulta, por fuente — 2026-08-25: subido de 100 a 150 (se probó 250 y disparó un bloqueo temporal de Incapsula/WAF en Met, ver docs/bitacora.md) para la cosecha de ~10.000 obras

// 2026-08-25: bloqueo temporal real del WAF (Incapsula) de Met durante la primera corrida de
// la cosecha grande (250/término disparó cientos de 403 seguidos) — se recuperó solo a los
// pocos minutos, pero insistir contra un bloqueo activo solo lo empeora/alarga. Este circuit
// breaker corta la ronda de ESE término/fuente ante varios fallos seguidos (probable bloqueo
// o caída del servicio) en vez de seguir insistiendo obra por obra hasta agotar el lote.
private const val MAX_CONSECUTIVE_FAILURES = 5
private const val DETAIL_FETCH_DELAY_MS = 300L // subido de 150 — uso más conservador tras el bloqueo

/** Igual que `ArtworkDao.getFiltered` (`classification IN ('painting','print')`, 2026-08-21)
 * — la app nunca muestra otra cosa. */
private val ELIGIBLE_CLASSIFICATIONS = setOf("painting", "print")

/** Igual que `ArtworkRepositoryImpl.MIN_FILTERABLE_YEAR` (2026-08-19/2026-08-25) — el
 * selector de años no baja de 740, y las obras de antes de esa fecha ya se sacaron a mano
 * de la base de datos empaquetada (`DELETE ... WHERE creationYearStart < 740`, 2026-08-25). */
private const val MIN_ELIGIBLE_YEAR = 740

/**
 * Filtro de elegibilidad para el catálogo REAL — no solo para pruebas puntuales del modo
 * normal. Cosechar y guardar esculturas/fotos/dibujos, o pinturas de antes del 740, es peso
 * muerto: nunca se van a mostrar (ver `ELIGIBLE_CLASSIFICATIONS`/`MIN_ELIGIBLE_YEAR` arriba),
 * pero igual inflan `artworks.db`. Se excluye ACÁ, antes de escribir a SQLite, en vez de
 * confiar solo en el filtro en tiempo de consulta de la app.
 *
 * `creationYearStart == null` se ACEPTA a propósito (no se descarta): la regla es "no
 * mostrar antes del 740", no "descartar lo que no tiene año conocido" — mismo criterio que
 * la limpieza manual del 2026-08-25, que en SQL tampoco toca los NULL (`NULL < 740` es NULL,
 * no true).
 */
private fun Artwork.isEligibleForCatalog(): Boolean {
    val year = creationYearStart
    return classification in ELIGIBLE_CLASSIFICATIONS && (year == null || year >= MIN_ELIGIBLE_YEAR)
}

/**
 * Términos genéricos (en inglés — Met/AIC/CMA/Rijks indexan mayormente en inglés/neerlandés,
 * y estos matchean razonablemente en ambos) usados por el modo "bulk". Sujetos/escenas/
 * apellidos de pintores conocidos, NO movimientos ni periodos — evita el mismo error que ya
 * corregimos en AIC (adivinar clasificación desde texto libre); el periodo/movimiento real
 * de cada obra lo sigue derivando `PeriodNormalizer`/`MovementNormalizer` desde los campos
 * propios de cada fuente, nunca desde el término de búsqueda.
 *
 * 2026-08-25: se amplió de 30 a ~190 términos (ver docs/bitacora.md) para poder llegar a
 * ~10.000 obras painting/print sin depender solo de subir `BATCH_SIZE` — muchos términos
 * genéricos ("landscape", "portrait") ya estaban cerca de agotar lo que cada fuente indexa
 * para ellos.
 */
private val BULK_QUERY_TERMS = listOf(
    // --- Sujetos/escenas genéricas (lista original, 2026-08-17) ---
    "portrait", "landscape", "still life", "flowers", "river", "mountain",
    "self portrait", "woman", "man", "child", "animals", "garden", "sea", "ship",
    "city", "winter", "night", "religious", "mythology", "horse", "dog", "music",
    "dance", "interior", "market", "forest", "sunset", "moon", "family", "house",
    // --- Más sujetos/escenas (2026-08-25) ---
    "children", "girl", "boy", "old man", "old woman", "peasant", "soldier", "king",
    "queen", "saint", "angel", "madonna", "nativity", "crucifixion", "battle", "hunting",
    "fishing", "harvest", "wedding", "bath", "sleep", "reading", "writing", "letter",
    "cat", "bird", "cattle", "sheep", "deer", "lion", "elephant", "butterfly", "fish",
    "fruit", "vegetables", "wine", "bread", "table", "kitchen", "chair", "window",
    "mirror", "book", "candle", "skull", "shell", "jewelry", "hat", "shoes",
    "lake", "waterfall", "cliff", "beach", "island", "valley", "field", "meadow",
    "road", "bridge", "village", "castle", "church", "cathedral", "temple", "palace",
    "harbor", "boat", "sailing", "storm", "rain", "snow", "spring", "summer", "autumn",
    "dawn", "dusk", "cloud", "sky", "star", "fire", "rainbow",
    "farmer", "worker", "sailor", "musician", "dancer", "actor", "beggar", "merchant",
    "traveler", "shepherd", "monk", "nun", "widow", "bride", "warrior",
    "love", "death", "sorrow", "joy", "dream", "vanity", "temptation", "sacrifice",
    "allegory", "still water", "orchard", "vineyard", "pond", "cottage", "windmill",
    "tower", "ruins", "column", "arch", "stairs", "courtyard", "balcony", "terrace",
    // --- Pintores muy documentados en estos 4 catálogos (nombres, no movimientos) ---
    "rembrandt", "vermeer", "hals", "van dyck", "rubens", "bruegel", "bosch", "durer",
    "cranach", "holbein", "titian", "tintoretto", "veronese", "raphael", "botticelli",
    "caravaggio", "canaletto", "tiepolo", "goya", "velazquez", "murillo", "el greco",
    "turner", "constable", "gainsborough", "reynolds", "hogarth", "whistler", "sargent",
    "homer", "eakins", "cassatt", "monet", "renoir", "degas", "manet", "cezanne",
    "pissarro", "sisley", "morisot", "seurat", "gauguin", "van gogh", "toulouse-lautrec",
    "matisse", "picasso", "klimt", "schiele", "munch", "kandinsky", "chagall", "klee",
    "hokusai", "hiroshige", "utamaro", "kuniyoshi", "corot", "courbet", "delacroix",
    "ingres", "david", "fragonard", "watteau", "boucher", "chardin", "vigee le brun"
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

    val allMapped = (harvestMet(query) + harvestAic(query) + harvestCma(query) + harvestRijks(query))
        .map { MovementOverrides.apply(it) }
    // El listado en consola muestra TODO lo mapeado (sirve para inspeccionar qué hay,
    // incluidas cosas que no vamos a guardar) — lo que se persiste en `saveAndReportDelta`
    // sí respeta el filtro de elegibilidad (ver `isEligibleForCatalog`), igual que el modo bulk.
    val artworks = allMapped.filter { it.isEligibleForCatalog() }

    println("\nMapeadas ${allMapped.size} obras en total (Met + AIC + CMA + Rijks), ${artworks.size} elegibles para el catálogo:\n")
    allMapped.sortedByDescending { it.rankScore }.forEach { a ->
        println(
            "- [%.1f]%s (%s) %s — %s (%s) | periodo=%s movimiento=%s siglo=%s | %s".format(
                a.rankScore,
                if (a.isEligibleForCatalog()) "" else " [excluida: ${a.classification}/${a.creationYearStart}]",
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
        val allMapped = (harvestMet(term) + harvestAic(term) + harvestCma(term) + harvestRijks(term))
            .map { MovementOverrides.apply(it) }
        // Solo se escribe (y solo cuenta para el objetivo) lo elegible para el catálogo real
        // (painting/print, año >= 740 o desconocido) — ver `isEligibleForCatalog`. El resto se
        // descarta acá mismo, antes de tocar SQLite, para no inflar `artworks.db` con obras
        // que la app nunca va a mostrar.
        val artworks = allMapped.filter { it.isEligibleForCatalog() }
        val result = writer.write(artworks)

        totalProcessed += result.totalProcessed
        totalNewOrChanged += result.newOrChanged.size
        allNewOrChanged += result.newOrChanged

        println(
            "Ronda \"$term\": ${allMapped.size} mapeadas, ${artworks.size} elegibles, " +
                "${result.newOrChanged.size} nuevas/cambiadas."
        )
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
    val searchResult = try {
        metApi.search(query = query, hasImages = true)
    } catch (e: Exception) {
        System.err.println("[met] búsqueda \"$query\" -> error de red/parseo: ${e.message}")
        return emptyList()
    }
    println("Total de resultados: ${searchResult.total}")

    val candidateIds = searchResult.objectIDs.orEmpty().take(BATCH_SIZE)
    if (candidateIds.isEmpty()) {
        println("[met] Sin resultados para \"$query\".")
        return emptyList()
    }

    var consecutiveFailures = 0
    return buildList {
        for (id in candidateIds) {
            val dto = try {
                val result = metApi.getObject(id)
                consecutiveFailures = 0
                result
            } catch (e: Exception) {
                consecutiveFailures++
                System.err.println("  [met] objectID=$id -> error de red/parseo: ${e.message}")
                if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                    System.err.println(
                        "  [met] $MAX_CONSECUTIVE_FAILURES fallos seguidos — probable bloqueo/caída, se corta esta ronda."
                    )
                    break
                }
                continue
            }
            val artwork = MetMapper.map(dto)
            if (artwork == null) {
                continue
            }
            add(artwork.copy(rankScore = RankScoreCalculator.calculate(artwork)))
            delay(DETAIL_FETCH_DELAY_MS)
        }
    }
}

private suspend fun harvestAic(query: String): List<Artwork> {
    val aicApi = HttpClientFactory.retrofit(AIC_BASE_URL).create(AicApi::class.java)

    println("Buscando en AIC: q=\"$query\" ...")
    val searchResult = try {
        aicApi.search(query = query, limit = BATCH_SIZE)
    } catch (e: Exception) {
        System.err.println("[aic] búsqueda \"$query\" -> error de red/parseo: ${e.message}")
        return emptyList()
    }
    println("Total de resultados: ${searchResult.pagination.total}")

    if (searchResult.data.isEmpty()) {
        println("[aic] Sin resultados para \"$query\".")
        return emptyList()
    }

    var consecutiveFailures = 0
    return buildList {
        for (summary in searchResult.data) {
            val dto = try {
                val result = aicApi.getArtwork(summary.id).data
                consecutiveFailures = 0
                result
            } catch (e: Exception) {
                consecutiveFailures++
                System.err.println("  [aic] id=${summary.id} -> error de red/parseo: ${e.message}")
                if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                    System.err.println(
                        "  [aic] $MAX_CONSECUTIVE_FAILURES fallos seguidos — probable bloqueo/caída, se corta esta ronda."
                    )
                    break
                }
                continue
            }
            val artwork = AicMapper.map(dto)
            if (artwork == null) {
                continue
            }
            add(artwork.copy(rankScore = RankScoreCalculator.calculate(artwork)))
            delay(DETAIL_FETCH_DELAY_MS)
        }
    }
}

private suspend fun harvestCma(query: String): List<Artwork> {
    val cmaApi = HttpClientFactory.retrofit(CMA_BASE_URL).create(CmaApi::class.java)

    println("Buscando en CMA (Cleveland): q=\"$query\" ...")
    val searchResult = try {
        cmaApi.search(query = query, limit = BATCH_SIZE)
    } catch (e: Exception) {
        System.err.println("[cma] búsqueda \"$query\" -> error de red/parseo: ${e.message}")
        return emptyList()
    }
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
    val searchResult = try {
        rijksApi.search(title = query)
    } catch (e: Exception) {
        System.err.println("[rijks] búsqueda \"$query\" -> error de red/parseo: ${e.message}")
        return emptyList()
    }
    println("Total de resultados: ${searchResult.partOf?.totalItems ?: 0}")

    val candidateIds = searchResult.orderedItems.take(BATCH_SIZE)
    if (candidateIds.isEmpty()) {
        println("[rijks] Sin resultados para \"$query\".")
        return emptyList()
    }

    var consecutiveFailures = 0
    return buildList {
        for (item in candidateIds) {
            val dto = try {
                val result = rijksApi.resolve(item.id)
                consecutiveFailures = 0
                result
            } catch (e: Exception) {
                consecutiveFailures++
                System.err.println("  [rijks] id=${item.id} -> error de red/parseo: ${e.message}")
                if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                    System.err.println(
                        "  [rijks] $MAX_CONSECUTIVE_FAILURES fallos seguidos — probable bloqueo/caída, se corta esta ronda."
                    )
                    break
                }
                continue
            }
            val artwork = RijksMapper.map(dto)
            if (artwork == null) {
                continue
            }
            add(artwork.copy(rankScore = RankScoreCalculator.calculate(artwork)))
            delay(DETAIL_FETCH_DELAY_MS)
        }
    }
}
