# Etapa 2 — Diseño de arquitectura

Incorpora la decisión de la Etapa 1 (Met + AIC, Kotlin nativo, Glance/WorkManager) y la nueva decisión de **cosecha propia** en lugar de fetch en vivo desde el dispositivo.

## 1. Visión general: dos proyectos, no uno

```
art-daily/
 ├─ harvester/         (proyecto Kotlin/JVM independiente, NO es parte del APK)
 │   └─ corre localmente o en CI, genera el dataset
 └─ android-app/        (la app Android en sí)
     └─ consume el dataset ya generado, nunca llama a Met/AIC directamente
```

El `harvester` es un módulo Kotlin/JVM plano (sin dependencias de Android), ejecutable con `./gradlew run` o como job programado en CI (GitHub Actions, por ejemplo, con una tarea cron mensual). Esto nos da:
- Reutilización de las **mismas clases de modelo y mapeo** (`Artwork`, `MetMapper`, `AicMapper`) entre el harvester y la app, si las ponemos en un módulo `:core-model` compartido por Gradle.
- Separación limpia: si algún día migramos a KMP, el `harvester` ya está desacoplado de Android por diseño.

## 2. Modelo común (Kotlin, módulo `:core-model`)

```kotlin
// core-model/src/main/kotlin/com/artdaily/core/model/Artwork.kt
data class Artwork(
    val id: String,                    // "met:45734" o "aic:99652" — prefijo de fuente + id nativo
    val title: String,
    val artistName: String?,
    val artistBirthYear: Int?,
    val artistDeathYear: Int?,
    val creationDateText: String?,      // texto original, ej. "c. 1665"
    val creationYearStart: Int?,        // normalizado, para filtrar por siglo/rango
    val creationYearEnd: Int?,
    val period: String?,                // normalizado vía diccionario (ver sección 5)
    val movement: String?,              // normalizado vía diccionario
    val century: Int?,                  // derivado de creationYearStart
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
    val description: String?,
    val dimensions: String?,
    val accessionNumber: String?,
    val rankScore: Float,               // ver sección 6
    val harvestedAt: Long               // timestamp de la última cosecha, para auditar
)
```

## 3. Contratos de red (Retrofit, usados solo por el `harvester`, no por la app)

```kotlin
// harvester/src/main/kotlin/com/artdaily/harvester/met/MetApi.kt
interface MetApi {
    @GET("public/collection/v1/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("medium") medium: String? = null,     // ej. "Paintings"
        @Query("hasImages") hasImages: Boolean = true
    ): MetSearchResponse

    @GET("public/collection/v1/objects/{objectID}")
    suspend fun getObject(@Path("objectID") id: Int): MetObjectDto
}

// harvester/src/main/kotlin/com/artdaily/harvester/aic/AicApi.kt
interface AicApi {
    @GET("api/v1/artworks/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("fields") fields: String = "id,title,artist_display,date_start,date_end," +
            "style_title,classification_title,image_id,is_public_domain,place_of_origin",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ): AicSearchResponse

    @GET("api/v1/artworks/{id}")
    suspend fun getArtwork(@Path("id") id: Int): AicArtworkDto
}
```

Notas:
- Met **no requiere key**; para obtener imágenes de calidad se filtra `hasImages=true` y se descarta cualquier objeto sin `isPublicDomain == true`.
- AIC **no requiere key**; las imágenes se construyen con su patrón IIIF: `https://www.artic.edu/iiif/2/{image_id}/full/843,/0/default.jpg` (tamaño ajustable en la URL).
- Ambas llamadas viven **solo en el harvester**, nunca se compilan dentro del APK de la app.

## 4. Los `Mapper` (fuente → modelo común)

```kotlin
// harvester/src/main/kotlin/com/artdaily/harvester/met/MetMapper.kt
object MetMapper {
    fun map(dto: MetObjectDto): Artwork? {
        if (!dto.isPublicDomain || dto.primaryImage.isNullOrBlank()) return null
        return Artwork(
            id = "met:${dto.objectID}",
            title = dto.title,
            artistName = dto.constituents?.firstOrNull { it.role == "Artist" }?.name,
            creationDateText = dto.objectDate,
            creationYearStart = dto.objectBeginDate,
            creationYearEnd = dto.objectEndDate,
            period = PeriodNormalizer.normalize(dto.period ?: dto.culture),
            movement = MovementNormalizer.normalize(dto.classification),
            century = CenturyCalculator.fromYear(dto.objectBeginDate),
            culture = dto.culture,
            classification = ClassificationNormalizer.normalize(dto.classification),
            museum = "The Metropolitan Museum of Art",
            museumId = dto.objectID.toString(),
            imageUrlFull = dto.primaryImage,
            imageUrlThumbnail = dto.primaryImageSmall,
            sourceUrl = "https://www.metmuseum.org/art/collection/search/${dto.objectID}",
            sourceApi = "met",
            license = "CC0",
            isPublicDomain = true,
            description = dto.creditLine,
            dimensions = dto.dimensions,
            accessionNumber = dto.accessionNumber,
            rankScore = 0f,           // se calcula en un paso posterior (sección 6)
            harvestedAt = System.currentTimeMillis()
        )
    }
}
```
`AicMapper` sigue el mismo patrón, adaptado a los nombres de campo de AIC (`artist_display`, `date_start`, `style_title`, etc.). El principio: **cada mapper decide por sí mismo si descarta el objeto** (filtro de licencia/imagen), así el resto del pipeline nunca ve datos dudosos.

## 5. Diccionario de normalización (periodo / movimiento / clasificación)

Este es, como identificamos en la Etapa 1, el trabajo de ingeniería más delicado. Se implementa como un mapeo estático mantenido a mano, no como heurística automática — más predecible y auditable.

```kotlin
// core-model/src/main/kotlin/com/artdaily/core/normalize/PeriodNormalizer.kt
object PeriodNormalizer {
    // claves en minúsculas, tal como aparecen "en bruto" en cada fuente
    private val map = mapOf(
        "renaissance" to "Renacimiento",
        "italian renaissance" to "Renacimiento",
        "northern renaissance" to "Renacimiento",
        "baroque" to "Barroco",
        "dutch golden age" to "Barroco",
        "rococo" to "Rococó",
        "neoclassicism" to "Neoclasicismo",
        "romanticism" to "Romanticismo",
        "realism" to "Realismo",
        "impressionism" to "Impresionismo",
        "post-impressionism" to "Postimpresionismo",
        "art nouveau" to "Art Nouveau",
        "expressionism" to "Expresionismo",
        "cubism" to "Cubismo",
        "surrealism" to "Surrealismo",
        "modern art" to "Arte moderno",
        "contemporary art" to "Arte contemporáneo"
        // se amplía con cada fuente nueva que se incorpore
    )

    fun normalize(raw: String?): String? {
        if (raw.isNullOrBlank()) return null
        val key = raw.trim().lowercase()
        return map[key] ?: map.entries.firstOrNull { key.contains(it.key) }?.value
        // si no hay match, se deja null — mejor no clasificar que clasificar mal
    }
}
```

Igual patrón para `MovementNormalizer` y `ClassificationNormalizer` (Pintura/Escultura/Fotografía/Dibujo/Grabado). Este diccionario **vive en el módulo compartido**, así que se corrige una sola vez y se aplica igual en el harvester y, si algún día hiciera falta, en la app.

## 6. Sistema de ranking (obra destacada)

Se calcula en el harvester, después del mapeo, con una fórmula simple y explicable (no ML por ahora):

```kotlin
fun calculateRankScore(a: Artwork): Float {
    var score = 0f
    if (!a.artistName.isNullOrBlank()) score += 1f
    if (!a.period.isNullOrBlank()) score += 1f
    if (!a.movement.isNullOrBlank()) score += 1f
    if (!a.description.isNullOrBlank()) score += 1f
    if (a.creationYearStart != null) score += 1f
    if (a.imageUrlFull != null) score += 1f
    // señal de "destacada": el propio Met marca isHighlight en algunos objetos
    if (a.museumFlaggedHighlight) score += 3f
    return score
}
```
Se usa como filtro mínimo (descartar obras con score muy bajo del pool de selección aleatoria), no como ranking rígido — mantiene la aleatoriedad que pediste como método principal.

## 7. Salida del harvester → cómo llega a la app

1. El harvester corre (manual o vía CI programado, ej. mensual) y produce dos artefactos:
   - `artworks.db` — base SQLite/Room ya poblada y lista, para el **primer arranque** de la app (se empaqueta en `assets/` de la app, patrón "prepackaged Room database").
   - `artworks-delta-YYYYMMDD.json` — solo lo nuevo/cambiado desde la última publicación, para **sincronizaciones posteriores** sin tener que redistribuir toda la app.
2. Ambos archivos se publican como **Release estático** (GitHub Releases es suficiente y gratuito; alternativa: Cloudflare R2 free tier). No hay servidor con lógica, solo archivos versionados con una URL fija tipo `.../latest/artworks-delta.json`.
3. En la app, `DailyArtworkWorker` (el mismo que ya decidimos para el widget) además de calcular la obra del día, en su ejecución periódica **comprueba si hay un delta más nuevo que el `harvestedAt` local**, lo descarga si aplica, y hace un `INSERT OR REPLACE` en Room. Sin esto, la app sigue funcionando perfectamente con el dataset embebido — la sync es una mejora, no una dependencia dura.

## 8. Esquema de Room (app)

```kotlin
@Entity(tableName = "artworks")
data class ArtworkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artistName: String?,
    val creationYearStart: Int?,
    val creationYearEnd: Int?,
    val period: String?,
    val movement: String?,
    val century: Int?,
    val culture: String?,
    val classification: String,
    val museum: String,
    val imageUrlFull: String?,
    val imageUrlThumbnail: String?,
    val sourceUrl: String,
    val license: String,
    val description: String?,
    val dimensions: String?,
    val rankScore: Float,
    val harvestedAt: Long
)

@Entity(tableName = "favorites")
data class FavoriteEntity(@PrimaryKey val artworkId: String, val savedAt: Long)

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val entryId: Long = 0,
    val widgetId: Int,          // 0 = "obra del día" de la app principal (sin widget)
    val artworkId: String,
    val shownAt: Long
)

@Entity(tableName = "widget_config")
data class WidgetConfigEntity(
    @PrimaryKey val widgetId: Int,
    val period: String? = null,
    val century: Int? = null,
    val movement: String? = null,
    val artistName: String? = null,
    val museum: String? = null,
    val avoidRepeatDays: Int = 30
)
```

## 9. Motor de selección (anti-repetición, por widget)

```kotlin
class SelectionEngine(private val dao: ArtworkDao) {
    suspend fun pickForWidget(config: WidgetConfigEntity): Artwork? {
        val candidates = dao.getFiltered(
            period = config.period, century = config.century,
            movement = config.movement, artistName = config.artistName,
            museum = config.museum, minRankScore = 3f
        )
        val recentlyShown = dao.getRecentHistory(config.widgetId, sinceDays = config.avoidRepeatDays)
        val pool = candidates.filterNot { it.id in recentlyShown }
            .ifEmpty { candidates } // si se agota el pool, reinicia el ciclo (punto 5 del prompt original)
        return pool.randomOrNull()
    }
}
```

## 10. Actualización del árbol de módulos

```
art-daily/
 ├─ core-model/        (Artwork, normalizadores, ranking — Kotlin puro, sin Android)
 ├─ harvester/          (MetApi, AicApi, mappers, genera artworks.db + delta.json)
 ├─ android-app/
 │   ├─ data/           (Room: DAOs/entities, repositorio que lee de Room + aplica sync de deltas)
 │   ├─ domain/          (SelectionEngine, casos de uso)
 │   ├─ ui/              (home, detail, filters, favorites, history, settings)
 │   └─ widget/           (GlanceAppWidget, config activity, receiver)
 └─ worker/              (DailyArtworkWorker — vive en android-app, usa data+domain)
```

---

Con esto quedan cerrados los tres pendientes de la Etapa 2 (esquema Room, contratos de red, diccionario de normalización) más la incorporación del harvester y el mecanismo de sync estático.

**Siguiente paso:** Tercera Etapa — crear el proyecto Android real en Android Studio con esta estructura de módulos y las dependencias (`build.gradle.kts`) de Compose, Room, Hilt, Retrofit, Coil, Glance y WorkManager.
