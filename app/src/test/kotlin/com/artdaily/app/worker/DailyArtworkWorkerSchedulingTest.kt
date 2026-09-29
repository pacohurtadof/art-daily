package com.artdaily.app.worker

import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Regresión del bug real reportado por el usuario (2026-08-25): "el fondo no cambia a
 * medianoche". Antes, `schedulePeriodic` armaba un `PeriodicWorkRequest` de 24h sin
 * `setInitialDelay`, así que la primera corrida (y por lo tanto todas las siguientes, cada
 * ~24h desde ahí) quedaba anclada a la hora en la que se llamó `schedulePeriodic` por
 * primera vez (ej. la hora en la que se abrió la app la primera vez), nunca a medianoche.
 *
 * Estos tests prueban únicamente la cuenta pura (`millisUntilNextLocalMidnight`), no que
 * `WorkManager` efectivamente dispare a esa hora — eso depende del scheduler real del
 * sistema operativo, no es algo que un test rápido y determinista pueda verificar.
 */
class DailyArtworkWorkerSchedulingTest {

    private val zone = ZoneId.of("America/Santiago")

    @Test
    fun `a mitad del dia, faltan justo las horas que quedan hasta medianoche`() {
        val noon = ZonedDateTime.of(2026, 8, 25, 12, 0, 0, 0, zone)

        val millis = DailyArtworkWorker.millisUntilNextLocalMidnight(noon)

        assertEquals(12 * 60 * 60 * 1000L, millis)
    }

    @Test
    fun `un segundo antes de medianoche, falta un segundo`() {
        val almostMidnight = ZonedDateTime.of(2026, 8, 25, 23, 59, 59, 0, zone)

        val millis = DailyArtworkWorker.millisUntilNextLocalMidnight(almostMidnight)

        assertEquals(1000L, millis)
    }

    @Test
    fun `justo al cruzar medianoche, faltan casi 24 horas para la siguiente`() {
        val rightAfterMidnight = ZonedDateTime.of(2026, 8, 25, 0, 0, 1, 0, zone)

        val millis = DailyArtworkWorker.millisUntilNextLocalMidnight(rightAfterMidnight)

        assertEquals(23 * 60 * 60 * 1000L + 59 * 60 * 1000L + 59 * 1000L, millis)
    }
}
