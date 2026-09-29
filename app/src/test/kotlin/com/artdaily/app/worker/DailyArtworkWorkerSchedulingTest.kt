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

/**
 * Regresión del bug real reportado por el usuario (2026-09-28): "el wallpaper... a veces se
 * cambia por la tarde de forma aleatoria". Causa: `ArtWidgetReceiver.onUpdate()` dispara una
 * corrida "extra" de `DailyArtworkWorker` (vía `enqueueOneTime`) no solo al agregar un
 * widget, sino cada vez que Android reparte `APPWIDGET_UPDATE` — lo que también pasa al
 * reiniciar el dispositivo o reinstalar la app. Sin esta guarda, cada corrida extra volvía a
 * aplicar el fondo automático — con la fuente Favoritos, avanzaba la rotación una posición
 * más cada vez, visible como un cambio "aleatorio" a cualquier hora.
 */
class DailyArtworkWorkerAlreadyAppliedTodayTest {

    @Test
    fun `no se aplico nunca (null) - no esta aplicado hoy`() {
        assertEquals(false, DailyArtworkWorker.alreadyAppliedToday(lastAppliedEpochDay = null, todayEpochDay = 19_000L))
    }

    @Test
    fun `ya se aplico hoy - se salta la corrida extra`() {
        assertEquals(true, DailyArtworkWorker.alreadyAppliedToday(lastAppliedEpochDay = 19_000L, todayEpochDay = 19_000L))
    }

    @Test
    fun `se aplico un dia distinto (incluido ayer) - no esta aplicado hoy, se vuelve a aplicar`() {
        assertEquals(false, DailyArtworkWorker.alreadyAppliedToday(lastAppliedEpochDay = 18_999L, todayEpochDay = 19_000L))
    }
}
