package com.artdaily.core.normalize

/** Convierte cadenas vacías/blancas a null. Las APIs de museos suelen mandar "" en vez de omitir la clave. */
fun String?.blankToNull(): String? = this?.trim()?.ifBlank { null }

private val HTML_TAG_REGEX = Regex("<[^>]*>")
private val WHITESPACE_RUN_REGEX = Regex("\\s+")

/** Algunas reseñas curatoriales (ej. `description` de AIC) vienen con HTML simple
 * (`<p>`, `<em>`) — se limpia para mostrar texto plano en la UI. No es un parser HTML
 * completo (no hace falta, es texto editorial simple, no markup arbitrario). */
fun String?.stripHtmlTags(): String? = this
    ?.replace(HTML_TAG_REGEX, " ")
    ?.replace(WHITESPACE_RUN_REGEX, " ")
    ?.trim()
    ?.ifBlank { null }
