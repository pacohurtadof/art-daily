package com.artdaily.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ArtDailyColorScheme = lightColorScheme(
    primary = Orange40,
    onPrimary = Color.White,
    primaryContainer = Orange90,
    onPrimaryContainer = Orange10,
    secondary = Brown40,
    onSecondary = Color.White,
    secondaryContainer = Brown90,
    onSecondaryContainer = Brown10,
    tertiary = Terracotta40,
    onTertiary = Color.White,
    background = Beige,
    onBackground = WarmDark,
    surface = Beige,
    onSurface = WarmDark,
    surfaceVariant = BeigeVariant,
    onSurfaceVariant = WarmGray,
    outline = WarmOutline,
    surfaceDim = BeigeDim,
    surfaceBright = Beige,
    surfaceContainerLowest = BeigeContainerLowest,
    surfaceContainerLow = BeigeContainerLow,
    surfaceContainer = BeigeContainer,
    surfaceContainerHigh = BeigeContainerHigh,
    surfaceContainerHighest = BeigeContainerHighest,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = InverseOrange,
    surfaceTint = Orange40
)

/** Reemplaza el `MaterialTheme { ... }` liso que usaban `MainActivity`/
 * `ArtWidgetConfigActivity` — mismo `MaterialTheme`, con la paleta cálida en vez del
 * esquema por defecto de M3. Ver `Color.kt` para el porqué de cada tono. */
@Composable
fun ArtDailyTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ArtDailyColorScheme, content = content)
}
