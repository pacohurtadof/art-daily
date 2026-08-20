package com.artdaily.harvester.rijks

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/**
 * Helpers para leer el JSON-LD (EDM "framed") de Rijksmuseum sin data classes estrictas —
 * verificado en vivo el 2026-08-18: el mismo tipo de dato viene en formas distintas según
 * el campo (a veces un objeto `{"@language","@value"}`, a veces un arreglo de esos objetos,
 * a veces un mapa `{"en": [...], "nl": [...]}`, a veces un string plano). Modelarlo todo
 * con data classes estrictas hubiera significado una clase distinta por cada variante.
 */

private const val SKOS_PREF_LABEL = "http://www.w3.org/2004/02/skos/core#prefLabel"

/**
 * Valor con idioma en forma `{"@language","@value"}` — sea un objeto suelto o un arreglo de
 * ellos. Prueba los idiomas preferidos en orden; si ninguno matchea, toma el primero que haya.
 */
fun JsonElement?.langText(vararg preferredLangs: String): String? {
    if (this == null) return null
    val candidates: List<JsonObject> = when (this) {
        is JsonArray -> mapNotNull { it as? JsonObject }
        is JsonObject -> listOf(this)
        else -> return (this as? JsonPrimitive)?.contentOrNull
    }
    for (lang in preferredLangs) {
        val match = candidates.firstOrNull { (it["@language"] as? JsonPrimitive)?.contentOrNull == lang }
        if (match != null) return (match["@value"] as? JsonPrimitive)?.contentOrNull
    }
    return (candidates.firstOrNull()?.get("@value") as? JsonPrimitive)?.contentOrNull
}

/** Como [langText], pero primero entra a la propiedad SKOS `prefLabel` de un concepto. */
fun JsonElement?.prefLabel(vararg preferredLangs: String): String? {
    val obj = when (this) {
        is JsonArray -> firstOrNull() as? JsonObject
        is JsonObject -> this
        else -> null
    } ?: return null
    return obj[SKOS_PREF_LABEL].langText(*preferredLangs)
}

/** El campo `title` (y `description`/`alternative`) de EDM viene como mapa idioma->valor(es),
 * ej. `{"en": ["a","b"], "nl": [...]}` — un patrón distinto al de `@language`/`@value`. */
fun JsonElement?.edmLangMapFirst(vararg preferredLangs: String): String? {
    val obj = this as? JsonObject ?: return null
    for (lang in preferredLangs) {
        val value = obj[lang] ?: continue
        val text = when (value) {
            is JsonArray -> (value.firstOrNull() as? JsonPrimitive)?.contentOrNull
            is JsonPrimitive -> value.contentOrNull
            else -> null
        }
        if (text != null) return text
    }
    val firstEntry = obj.entries.firstOrNull()?.value ?: return null
    return when (firstEntry) {
        is JsonArray -> (firstEntry.firstOrNull() as? JsonPrimitive)?.contentOrNull
        is JsonPrimitive -> firstEntry.contentOrNull
        else -> null
    }
}

/** Un arreglo de strings planos, ej. `identifier: ["SK-A-1935"]`. */
fun JsonElement?.firstPlainString(): String? {
    val arr = this as? JsonArray ?: return (this as? JsonPrimitive)?.contentOrNull
    return (arr.firstOrNull() as? JsonPrimitive)?.contentOrNull
}

/** `dateOfBirth`/`dateOfDeath` vienen como fecha ISO ("1606-07-15") dentro de un valor con
 * idioma — el idioma es irrelevante para una fecha, solo interesan los primeros 4 dígitos. */
fun JsonElement?.yearFromIsoDate(): Int? = langText("en", "nl")?.take(4)?.toIntOrNull()
