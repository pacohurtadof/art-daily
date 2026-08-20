package com.artdaily.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.artdaily.app.R
import com.artdaily.app.ui.Routes
import com.artdaily.app.ui.detail.DetailScreen
import com.artdaily.app.ui.detail.DetailViewModel
import com.artdaily.app.ui.explore.ExploreScreen
import com.artdaily.app.ui.explore.ExploreViewModel
import com.artdaily.app.ui.favorites.FavoritesScreen
import com.artdaily.app.ui.favorites.FavoritesViewModel
import com.artdaily.app.ui.home.HomeScreen
import com.artdaily.app.ui.home.HomeViewModel
import com.artdaily.app.ui.settings.SettingsScreen
import com.artdaily.app.ui.settings.SettingsViewModel
import com.artdaily.app.ui.theme.ArtDailyTheme
import com.artdaily.app.widget.ArtWidget
import dagger.hilt.android.AndroidEntryPoint

/**
 * Punto de entrada de la app (ícono de launcher). Tabs abajo: Hoy / Explorar / Favoritos /
 * Ajustes, más una pantalla de detalle compartida a la que se llega desde cualquiera de
 * las tres primeras — o directo tocando el widget (ver `pendingArtworkId` abajo).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Antes de super.onCreate() — así lo pide la API de SplashScreen.
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Si la app se abrió tocando el widget, el Intent trae el id de esa obra —
        // ver `ArtWidget.ARTWORK_ID_PARAM`. `name` porque `ActionParameters.Key` guarda su
        // nombre ahí, no hay forma directa de leerlo como constante de compilación.
        val pendingArtworkId = intent?.getStringExtra(ArtWidget.ARTWORK_ID_PARAM.name)

        setContent {
            ArtDailyTheme {
                ArtDailyApp(homeViewModel = homeViewModel, pendingArtworkId = pendingArtworkId)
            }
        }
    }
}

/**
 * Íconos monocromo de Material (`material-icons-core`, no `-extended` — solo trae los
 * ~40 más comunes) en vez de emoji: antes cada tab tenía su propio estilo visual
 * (casita a color, lupa, corazón de texto), ahora los tres comparten el mismo trazo y
 * se pintan con el color que ya maneja `NavigationBarItem` (transparente, sin fondo
 * propio) — outline cuando el tab no está seleccionado, relleno cuando sí.
 */
private data class BottomTab(
    val route: String,
    @StringRes val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val BOTTOM_TABS = listOf(
    BottomTab(Routes.HOME, R.string.nav_home, Icons.Filled.Home, Icons.Outlined.Home),
    BottomTab(Routes.EXPLORE, R.string.nav_explore, Icons.Filled.Search, Icons.Outlined.Search),
    BottomTab(Routes.FAVORITES, R.string.nav_favorites, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    BottomTab(Routes.SETTINGS, R.string.nav_settings, Icons.Filled.Settings, Icons.Outlined.Settings)
)

@Composable
private fun ArtDailyApp(homeViewModel: HomeViewModel, pendingArtworkId: String?) {
    val navController = rememberNavController()

    LaunchedEffect(pendingArtworkId) {
        if (pendingArtworkId != null) {
            navController.navigate(Routes.detail(pendingArtworkId))
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                BOTTOM_TABS.forEach { tab ->
                    val selected = currentRoute?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                // Sin `saveState`/`restoreState`: con esos activados (el
                                // recipe estándar de Google para bottom nav), si Detalle
                                // estaba empujado ENCIMA de un tab cuando cambiás de tab,
                                // Nav Compose guarda y luego restaura Detalle junto con
                                // ese tab como una sola unidad — bug real confirmado:
                                // volver a "Hoy" podía mostrar Detalle en vez de Hoy, o
                                // reaparecer con datos de favorito desactualizados. Al
                                // quitar saveState/restoreState, cada cambio de tab colapsa
                                // el stack hasta la raíz de ese tab de verdad — se pierde
                                // el scroll/filtros al volver a un tab, pero la pantalla
                                // mostrada siempre es la correcta.
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = stringResource(tab.labelRes)
                            )
                        },
                        label = { Text(stringResource(tab.labelRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onShowDetail = { artwork -> navController.navigate(Routes.detail(artwork.id)) }
                )
            }
            composable(Routes.EXPLORE) {
                val viewModel: ExploreViewModel = hiltViewModel()
                ExploreScreen(
                    viewModel = viewModel,
                    onArtworkClick = { artwork -> navController.navigate(Routes.detail(artwork.id)) }
                )
            }
            composable(Routes.FAVORITES) {
                val viewModel: FavoritesViewModel = hiltViewModel()
                FavoritesScreen(
                    viewModel = viewModel,
                    onArtworkClick = { artwork -> navController.navigate(Routes.detail(artwork.id)) }
                )
            }
            composable(Routes.DETAIL) {
                val viewModel: DetailViewModel = hiltViewModel()
                DetailScreen(viewModel = viewModel)
            }
            composable(Routes.SETTINGS) {
                val viewModel: SettingsViewModel = hiltViewModel()
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
