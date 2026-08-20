plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.artdaily.app"
    // 37, no 36: androidx.hilt 1.4.0 (ver libs.versions.toml) trae transitivamente
    // androidx.lifecycle 2.11.0, que exige compileSdk >= 37. Verificado el 2026-08-17 —
    // el build fallaba en `checkDebugAarMetadata` con compileSdk 36.
    compileSdk = 37

    defaultConfig {
        applicationId = "com.artdaily.app"
        minSdk = 26      // API 26+: buen soporte de Glance/WorkManager sin parches
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0-mvp"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    // Room compara el esquema real contra este JSON versionado — permite escribir
    // migraciones de verdad si la tabla cambia, en vez de improvisar en producción.
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":core-model"))

    // --- Core / Compose ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // --- Room (fuente de verdad local; ver Etapa 2) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // --- Hilt (DI, incluida la variante para Worker) ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler) // genera la fábrica de @HiltWorker — faltaba

    // --- Retrofit (usado en :harvester; aquí solo si la app llega a hacer
    // llamadas puntuales, p. ej. para descargar el delta.json de sync) ---
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // --- Coil (imágenes) ---
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // --- Traducción on-device (ML Kit) — modelos se descargan una vez por par de
    // idiomas y quedan en el dispositivo; sin API key, sin costo por uso. ---
    implementation(libs.mlkit.translate)
    implementation(libs.mlkit.language.id)
    implementation(libs.kotlinx.coroutines.play.services) // Task<T>.await() para las APIs de ML Kit

    // --- Widget + trabajo en segundo plano ---
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.work.runtime.ktx)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
