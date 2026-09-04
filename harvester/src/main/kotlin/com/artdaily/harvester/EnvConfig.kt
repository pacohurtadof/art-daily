package com.artdaily.harvester

import java.io.File

/**
 * Carga `harvester/.env` (nunca commiteado, ver `.gitignore` y `harvester/.env.example`) para
 * las API keys que necesita el harvester — hoy solo `SMITHSONIAN_API_KEY`
 * (2026-09-04, ver `docs/bitacora.md`). Ninguna otra fuente necesitó key hasta ahora.
 *
 * Solo hace falta para CORRER el harvester (cosechar obras). La app nunca usa esto — las
 * imágenes se sirven de `ids.si.edu` sin key, la key solo tapa el buscador de metadatos
 * (`api.si.edu`).
 */
object EnvConfig {
    private val values: Map<String, String> by lazy { load() }

    /** Prioriza una variable de entorno real (ej. en CI) sobre `.env` local. */
    fun get(key: String): String? = System.getenv(key) ?: values[key]

    fun require(key: String): String =
        get(key) ?: error(
            "Falta $key — copiá harvester/.env.example a harvester/.env y completá tu key " +
                "(o exportala como variable de entorno)."
        )

    private fun load(): Map<String, String> {
        val file = File(".env")
        if (!file.exists()) return emptyMap()
        return file.readLines()
            .filter { it.isNotBlank() && !it.trimStart().startsWith("#") }
            .mapNotNull { line ->
                val idx = line.indexOf('=')
                if (idx < 0) null else line.substring(0, idx).trim() to line.substring(idx + 1).trim()
            }
            .toMap()
    }
}
