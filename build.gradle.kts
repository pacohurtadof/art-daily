// Nota importante (verificado hoy contra la documentación oficial):
// Con AGP 9.x, el soporte de Kotlin viene INTEGRADO en el plugin de Android.
// Por eso NO se aplica aquí el plugin clásico "org.jetbrains.kotlin.android":
// aplicarlo explícitamente junto a Kotlin 2.3.x + AGP 9.x produce un error de
// configuración (es un cambio muy reciente, de enero 2026). Si al sincronizar
// en Android Studio ves un error relacionado con esto, es la señal de que la
// versión de AGP/Kotlin instalada en tu máquina difiere de la que verificamos
// aquí — dímelo y lo ajustamos a lo que tengas instalado.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
