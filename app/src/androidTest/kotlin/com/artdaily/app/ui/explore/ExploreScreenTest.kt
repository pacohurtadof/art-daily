package com.artdaily.app.ui.explore

import android.content.Context
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artdaily.app.MainActivity
import com.artdaily.app.R
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Primer test de Compose UI del proyecto (2026-08-21) — hasta ahora solo había unit tests
 * (JVM, con fakes) y un test instrumentado que ejercitaba Room directo, nada que toque
 * botones de verdad en una pantalla real, al estilo Selenium/Playwright. Corre contra la
 * app real completa (Hilt real, `artworks.db` empaquetada real) — no reemplaza dependencias
 * por fakes, igual que `AppDatabaseSmokeTest`.
 */
@RunWith(AndroidJUnit4::class)
class ExploreScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /**
     * Regresión del crash real encontrado en vivo el 2026-08-21: seleccionar 2+ chips de
     * movimiento a la vez en Explorar tumbaba la app entera ("ArtDaily keeps stopping" —
     * Room expandía mal un parámetro de lista, ver el comentario en `ArtworkDao`). Este
     * test toca la UI real de punta a punta, tal como se hizo a mano ese día para
     * encontrarlo — si el bug vuelve, este test lo agarra en vez de que alguien lo
     * descubra tocando la app.
     */
    @Test
    fun selectingTwoMovementChipsAtOnceDoesNotCrashAndKeepsBothSelected() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Texto real de los recursos (no un literal hardcodeado) — así el test funciona
        // sin importar en qué idioma esté configurado el dispositivo.
        val exploreLabel = context.getString(R.string.nav_explore)
        // `runCatching` acá a propósito: en un dispositivo real (no en el emulador) hay un
        // hueco real entre que la Activity arranca y Compose registra su primera
        // jerarquía ante el framework de test (de seguro por `installSplashScreen()`) —
        // sin esto, `fetchSemanticsNodes()` lanza "No compose hierarchies found" en el
        // primer intento y `waitUntil` no reintenta un `IllegalStateException`, corta de
        // una en vez de esperar el timeout completo. Encontrado en vivo en el Pixel 10.
        composeRule.waitUntil(timeoutMillis = 15_000) {
            runCatching {
                composeRule.onAllNodesWithText(exploreLabel).fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false)
        }
        composeRule.onNodeWithText(exploreLabel).performClick()

        // Los chips de Movement tardan en aparecer — `available` se carga desde Room de
        // forma asíncrona al entrar a la pantalla.
        composeRule.waitUntil(timeoutMillis = 15_000) {
            runCatching {
                composeRule.onAllNodes(hasTestTagPrefix(MOVEMENT_CHIP_PREFIX))
                    .fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false)
        }

        val movementChips = composeRule.onAllNodes(hasTestTagPrefix(MOVEMENT_CHIP_PREFIX))
        val chipCount = movementChips.fetchSemanticsNodes().size
        // Si el catálogo empaquetado en este dispositivo de prueba solo tiene un movimiento
        // reconocido, no hay forma de probar multi-selección — se salta en vez de fallar
        // (mismo criterio defensivo que ya usa `AppDatabaseSmokeTest`).
        assumeTrue("Se necesitan 2+ movimientos en el catálogo para este test", chipCount >= 2)

        movementChips[0].performClick()
        movementChips[1].performClick()

        // Si el bug de Room reaparece, la app crashea ANTES de llegar a esta línea — el
        // test fallaría con la excepción de la instrumentación, no con un assert normal.
        movementChips[0].assertIsSelected()
        movementChips[1].assertIsSelected()
    }

    private fun hasTestTagPrefix(prefix: String) =
        SemanticsMatcher("TestTag starts with '$prefix'") { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)?.startsWith(prefix) == true
        }

    private companion object {
        const val MOVEMENT_CHIP_PREFIX = "explore_movement_chip_"
    }
}
