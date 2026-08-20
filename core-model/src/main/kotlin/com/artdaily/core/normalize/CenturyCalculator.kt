package com.artdaily.core.normalize

/**
 * Deriva el siglo a partir de un año. Convención: siglos d.C. son positivos (1667 -> 17),
 * siglos a.C. son negativos (-3000 -> -30, "siglo XXX a.C."). Necesario porque el catálogo
 * del Met incluye antigüedades egipcias/griegas/romanas con `objectBeginDate` negativo.
 */
object CenturyCalculator {
    fun fromYear(year: Int?): Int? {
        if (year == null) return null
        return when {
            year > 0 -> (year - 1) / 100 + 1
            year < 0 -> -((-year - 1) / 100 + 1)
            else -> null // año 0 no existe en el calendario, dato no confiable
        }
    }
}
