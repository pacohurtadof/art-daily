package com.artdaily.harvester.storage

import com.artdaily.core.model.Artwork
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

/**
 * Escribe la lista de [Artwork] cosechadas en un archivo SQLite real (`artworks.db`),
 * el artefacto que describe `docs/etapa2-diseno-arquitectura.md` (sección 7) para
 * empaquetar en `assets/` de la app y arrancar sin red la primera vez.
 *
 * Nota de diseño (respecto al `ArtworkEntity` de la sección 8 del mismo documento):
 * aquí se persisten TODOS los campos de [Artwork], no solo el subconjunto reducido que
 * mostraba el `ArtworkEntity` de ejemplo — a ese le faltaban `sourceApi`, `isPublicDomain`,
 * `accessionNumber` y `museumId`, que la Etapa 1 (sección 3) exige guardar para poder
 * auditar el origen legal de cada obra. Cuando se cree el `ArtworkEntity` real de Room en
 * `:app`, debe usar este mismo set de columnas (mismos nombres, en camelCase — Room, sin
 * `@ColumnInfo`, espera el nombre de columna igual al nombre de la propiedad Kotlin).
 *
 * Usa `INSERT OR REPLACE`, así que correr el harvester varias veces (con distintas
 * búsquedas) va acumulando obras en el mismo archivo en vez de sobreescribirlo entero.
 */
class ArtworkSqliteWriter(private val dbPath: String) {

    /** Resultado de [write]: cuántas se procesaron en total, y cuáles son nuevas/cambiadas. */
    data class WriteResult(val totalProcessed: Int, val newOrChanged: List<Artwork>)

    fun write(artworks: List<Artwork>): WriteResult {
        File(dbPath).parentFile?.mkdirs()
        DriverManager.getConnection("jdbc:sqlite:$dbPath").use { conn ->
            conn.autoCommit = false
            createTable(conn)
            addIconicColumnIfMissing(conn)
            setSchemaVersion(conn)

            // Se lee el estado PREVIO antes de insertar, para poder comparar y saber qué
            // realmente cambió (si se leyera después, ya estaría pisado por el INSERT OR REPLACE).
            val existingById = readExisting(conn, artworks.map { it.id })
            val newOrChanged = artworks.filter { a ->
                val previous = existingById[a.id]
                // harvestedAt se ignora en la comparación: cambia en cada corrida aunque el
                // dato real de la obra sea idéntico, y no queremos que eso "invente" cambios.
                previous == null || previous.copy(harvestedAt = 0L) != a.copy(harvestedAt = 0L)
            }

            insertAll(conn, artworks)
            conn.commit()
            return WriteResult(artworks.size, newOrChanged)
        }
    }

    /**
     * Crea las 4 tablas que espera `:app` (ver los `*Entity.kt` en `app/data/local`). Room, al abrir
     * un `artworks.db` empaquetado vía `createFromAsset`, valida el esquema de TODAS las
     * entidades declaradas en `AppDatabase` — no solo `artworks` — y no crea las que falten.
     * Por eso el harvester, que es quien produce el archivo que se empaqueta en `assets/`,
     * tiene que dejar las 4 ya creadas (aunque `favorites`/`history`/`widget_config` arranquen
     * vacías; esas las llena la app en tiempo de uso).
     */
    private fun createTable(conn: Connection) {
        conn.createStatement().use { stmt ->
            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS artworks (
                    id TEXT PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL,
                    artistName TEXT,
                    artistBirthYear INTEGER,
                    artistDeathYear INTEGER,
                    creationDateText TEXT,
                    creationYearStart INTEGER,
                    creationYearEnd INTEGER,
                    period TEXT,
                    movement TEXT,
                    century INTEGER,
                    culture TEXT,
                    country TEXT,
                    classification TEXT NOT NULL,
                    museum TEXT NOT NULL,
                    museumId TEXT NOT NULL,
                    imageUrlFull TEXT,
                    imageUrlThumbnail TEXT,
                    sourceUrl TEXT NOT NULL,
                    sourceApi TEXT NOT NULL,
                    license TEXT NOT NULL,
                    isPublicDomain INTEGER NOT NULL,
                    description TEXT,
                    creditLine TEXT,
                    descriptionAttribution TEXT,
                    dimensions TEXT,
                    accessionNumber TEXT,
                    museumFlaggedHighlight INTEGER NOT NULL,
                    rankScore REAL NOT NULL,
                    harvestedAt INTEGER NOT NULL,
                    isIconic INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent()
            )
            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS favorites (
                    artworkId TEXT PRIMARY KEY NOT NULL,
                    savedAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS history (
                    entryId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    widgetId INTEGER NOT NULL,
                    artworkId TEXT NOT NULL,
                    shownAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
            // museum/century -> yearFrom/yearTo el 2026-08-19 (ver ArtworkFilter en core-model).
            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS widget_config (
                    widgetId INTEGER PRIMARY KEY NOT NULL,
                    period TEXT,
                    movement TEXT,
                    artistName TEXT,
                    yearFrom INTEGER,
                    yearTo INTEGER,
                    avoidRepeatDays INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }

    /** `CREATE TABLE IF NOT EXISTS` no toca una tabla `artworks` que ya existía de antes de
     * agregar `isIconic` (2026-08-28) — hace falta un `ALTER TABLE` aparte para los archivos
     * `artworks.db` ya generados por corridas previas del harvester. Idempotente: si la
     * columna ya existe (tabla creada de cero con el `CREATE TABLE` de arriba, que ya la
     * incluye), SQLite tira "duplicate column name" y se ignora a propósito. */
    private fun addIconicColumnIfMissing(conn: Connection) {
        conn.createStatement().use { stmt ->
            try {
                stmt.execute("ALTER TABLE artworks ADD COLUMN isIconic INTEGER NOT NULL DEFAULT 0")
            } catch (e: java.sql.SQLException) {
                if (e.message?.contains("duplicate column", ignoreCase = true) != true) throw e
            }
        }
    }

    /** Bug real encontrado el 2026-09-02 (ver `docs/bitacora.md`): sin esto, el archivo crudo
     * que genera este writer arranca en `PRAGMA user_version = 0`. Room, al abrirlo vía
     * `createFromAsset` (`AppDatabase.version = 4` en `:app`), detecta el mismatch de versión
     * en CADA apertura del proceso — no solo la primera — y con `fallbackToDestructiveMigration`
     * activado (`DatabaseModule.kt`), eso significa borrar y reconstruir el esquema cada vez.
     * Favoritos, historial y cualquier dato guardado en tiempo de uso se perdían en cada
     * reinicio de la app (reproducido en vivo: force-stop/swipe en Recientes → todo vuelto al
     * estado del asset empaquetado). Documentado oficialmente por Android — hay que igualar
     * el `user_version` del archivo al `@Database(version = ...)` de Room:
     * https://developer.android.com/training/data-storage/room/prepopulate
     *
     * `SCHEMA_VERSION` tiene que mantenerse igual a `AppDatabase.version` a mano — no hay forma
     * de compartir la constante entre `:harvester` (JVM puro) y `:app` sin acoplar los módulos. */
    private fun setSchemaVersion(conn: Connection) {
        conn.createStatement().use { it.execute("PRAGMA user_version = $SCHEMA_VERSION") }
    }

    /** Todo el catálogo actual — para publicar un delta "completo" en vez de incremental
     * (mismo patrón ya usado a mano en releases anteriores, ej. `data-20260828`/`data-20260831`,
     * cuando conviene que cualquier dispositivo quede al día en un solo sync sin importar
     * cuántas cosechas se haya perdido). No hace falta abrir una conexión aparte para esto —
     * se llama después de [write], reutiliza el mismo archivo. */
    fun readAll(): List<Artwork> {
        DriverManager.getConnection("jdbc:sqlite:$dbPath").use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM artworks").use { rs ->
                    val result = mutableListOf<Artwork>()
                    while (rs.next()) result += rs.toArtwork()
                    return result
                }
            }
        }
    }

    /** Trae, de los ids dados, los que ya existían en la tabla — para poder diffear. */
    private fun readExisting(conn: Connection, ids: List<String>): Map<String, Artwork> {
        if (ids.isEmpty()) return emptyMap()
        val placeholders = ids.joinToString(",") { "?" }
        val sql = "SELECT * FROM artworks WHERE id IN ($placeholders)"
        conn.prepareStatement(sql).use { ps ->
            ids.forEachIndexed { i, id -> ps.setString(i + 1, id) }
            ps.executeQuery().use { rs ->
                val result = mutableMapOf<String, Artwork>()
                while (rs.next()) {
                    val artwork = rs.toArtwork()
                    result[artwork.id] = artwork
                }
                return result
            }
        }
    }

    private fun ResultSet.toArtwork(): Artwork = Artwork(
        id = getString("id"),
        title = getString("title"),
        artistName = getString("artistName"),
        artistBirthYear = getNullableInt("artistBirthYear"),
        artistDeathYear = getNullableInt("artistDeathYear"),
        creationDateText = getString("creationDateText"),
        creationYearStart = getNullableInt("creationYearStart"),
        creationYearEnd = getNullableInt("creationYearEnd"),
        period = getString("period"),
        movement = getString("movement"),
        century = getNullableInt("century"),
        culture = getString("culture"),
        country = getString("country"),
        classification = getString("classification"),
        museum = getString("museum"),
        museumId = getString("museumId"),
        imageUrlFull = getString("imageUrlFull"),
        imageUrlThumbnail = getString("imageUrlThumbnail"),
        sourceUrl = getString("sourceUrl"),
        sourceApi = getString("sourceApi"),
        license = getString("license"),
        isPublicDomain = getBoolean("isPublicDomain"),
        description = getString("description"),
        creditLine = getString("creditLine"),
        descriptionAttribution = getString("descriptionAttribution"),
        dimensions = getString("dimensions"),
        accessionNumber = getString("accessionNumber"),
        museumFlaggedHighlight = getBoolean("museumFlaggedHighlight"),
        rankScore = getFloat("rankScore"),
        harvestedAt = getLong("harvestedAt"),
        isIconic = getBoolean("isIconic")
    )

    private fun ResultSet.getNullableInt(column: String): Int? {
        val value = getInt(column)
        return if (wasNull()) null else value
    }

    private fun insertAll(conn: Connection, artworks: List<Artwork>): Int {
        val sql = """
            INSERT OR REPLACE INTO artworks (
                id, title, artistName, artistBirthYear, artistDeathYear, creationDateText,
                creationYearStart, creationYearEnd, period, movement, century, culture, country,
                classification, museum, museumId, imageUrlFull, imageUrlThumbnail, sourceUrl,
                sourceApi, license, isPublicDomain, description, creditLine, descriptionAttribution,
                dimensions, accessionNumber, museumFlaggedHighlight, rankScore, harvestedAt, isIconic
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        conn.prepareStatement(sql).use { ps ->
            for (a in artworks) {
                var i = 1
                ps.setString(i++, a.id)
                ps.setString(i++, a.title)
                ps.setNullableString(i++, a.artistName)
                ps.setNullableInt(i++, a.artistBirthYear)
                ps.setNullableInt(i++, a.artistDeathYear)
                ps.setNullableString(i++, a.creationDateText)
                ps.setNullableInt(i++, a.creationYearStart)
                ps.setNullableInt(i++, a.creationYearEnd)
                ps.setNullableString(i++, a.period)
                ps.setNullableString(i++, a.movement)
                ps.setNullableInt(i++, a.century)
                ps.setNullableString(i++, a.culture)
                ps.setNullableString(i++, a.country)
                ps.setString(i++, a.classification)
                ps.setString(i++, a.museum)
                ps.setString(i++, a.museumId)
                ps.setNullableString(i++, a.imageUrlFull)
                ps.setNullableString(i++, a.imageUrlThumbnail)
                ps.setString(i++, a.sourceUrl)
                ps.setString(i++, a.sourceApi)
                ps.setString(i++, a.license)
                ps.setBoolean(i++, a.isPublicDomain)
                ps.setNullableString(i++, a.description)
                ps.setNullableString(i++, a.creditLine)
                ps.setNullableString(i++, a.descriptionAttribution)
                ps.setNullableString(i++, a.dimensions)
                ps.setNullableString(i++, a.accessionNumber)
                ps.setBoolean(i++, a.museumFlaggedHighlight)
                ps.setFloat(i++, a.rankScore)
                ps.setLong(i++, a.harvestedAt)
                ps.setBoolean(i, a.isIconic)
                ps.addBatch()
            }
            val results = ps.executeBatch()
            return results.size
        }
    }

    private fun java.sql.PreparedStatement.setNullableString(index: Int, value: String?) {
        if (value == null) setNull(index, java.sql.Types.VARCHAR) else setString(index, value)
    }

    private fun java.sql.PreparedStatement.setNullableInt(index: Int, value: Int?) {
        if (value == null) setNull(index, java.sql.Types.INTEGER) else setInt(index, value)
    }

    private companion object {
        /** Debe coincidir con `AppDatabase.version` en `:app` — ver `setSchemaVersion`. */
        const val SCHEMA_VERSION = 4
    }
}
