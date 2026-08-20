package com.artdaily.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta cálida (beige + naranja) — reemplazó el esquema por defecto de Material3
 * (morado sobre blanco/lavanda) el 2026-08-19, a pedido del usuario. Un solo lugar
 * centralizado: como ningún componente tenía colores hardcodeados (todos usaban
 * `MaterialTheme.colorScheme.*` por default — botones, switches, radio buttons, chips,
 * el tab seleccionado), cambiar esto acá cascadea a toda la app sin tocar cada pantalla.
 *
 * No incluye variante oscura a propósito — `Theme.ArtDaily` (XML) ya fuerza modo claro
 * (`android:Theme.Material.Light.NoActionBar`), decisión previa sin relación a este pedido.
 */

// Naranja — color de marca/acento (antes morado por defecto de M3).
val Orange40 = Color(0xFFC2662B)       // primary
val Orange90 = Color(0xFFFFDBC0)       // primaryContainer
val Orange10 = Color(0xFF4A2100)       // onPrimaryContainer

// Marrón cálido — secundario, complementa el naranja sin competir con él.
val Brown40 = Color(0xFF7C5B41)        // secondary
val Brown90 = Color(0xFFEDDCC7)        // secondaryContainer
val Brown10 = Color(0xFF2C1B0C)        // onSecondaryContainer

val Terracotta40 = Color(0xFF8B5E34)   // tertiary — variante del naranja, no un tercer tono nuevo

// Beige — reemplaza el blanco/lavanda por defecto de fondo y superficies.
val Beige = Color(0xFFF5EEE1)          // background / surface / surfaceBright
val BeigeVariant = Color(0xFFE8DCC8)   // surfaceVariant
val WarmDark = Color(0xFF2B2420)       // onBackground / onSurface — negro cálido, no puro
val WarmGray = Color(0xFF55483A)       // onSurfaceVariant
val WarmOutline = Color(0xFF8A7A63)    // outline

// Roles "surface container" (M3 los usa para NavigationBar, TopAppBar, cards elevadas,
// etc. — sin especificarlos acá quedaban con el tono lavanda por defecto de M3, aunque
// `surface`/`background` ya estuvieran en beige; se notaba en la barra de tabs de abajo).
val BeigeDim = Color(0xFFD8CEBB)              // surfaceDim
val BeigeContainerLowest = Color(0xFFFFFFFF)  // surfaceContainerLowest
val BeigeContainerLow = Color(0xFFF0E9DA)     // surfaceContainerLow
val BeigeContainer = Color(0xFFEBE3D2)        // surfaceContainer
val BeigeContainerHigh = Color(0xFFE5DCC8)    // surfaceContainerHigh
val BeigeContainerHighest = Color(0xFFDFD5C0) // surfaceContainerHighest
val InverseSurfaceDark = Color(0xFF3D342C)    // inverseSurface
val InverseOnSurfaceLight = Color(0xFFF5EEE1) // inverseOnSurface
val InverseOrange = Color(0xFFFFB77C)         // inversePrimary
