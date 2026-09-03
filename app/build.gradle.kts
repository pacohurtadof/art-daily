import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// Firma de release (2026-08-21, para publicar en Play Store). `keystore.properties`
// tiene las contraseñas reales y NO se versiona (está en .gitignore) — este archivo solo
// lee de ahí. Sin ese archivo (ej. clonando el repo en otra máquina sin el keystore),
// releaseSigningConfig queda sin configurar y `assembleRelease`/`bundleRelease` fallan
// con un error claro en vez de silenciosamente firmar con nada.
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) load(FileInputStream(keystorePropertiesFile))
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
        // v10 (2026-09-02): 1 y 2 quedaron consumidos en Play Console (el 1 con el .aab viejo,
        // de antes del fix crítico de Room; el 2 rechazado también por motivo desconocido —
        // probablemente pruebas previas del usuario en la consola). Salto a 10 para tener
        // margen y no ir de a uno — Play Console nunca permite reusar un versionCode ya subido.
        versionCode = 10
        versionName = "0.1.1-mvp"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
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

    // --- Tests de Compose UI (2026-08-21) — el equivalente a Selenium/Playwright para
    // esta app: tocan botones/chips reales sobre la app real corriendo en un
    // emulador/dispositivo, no solo Room o lógica en fakes. ---
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Forzada explícita — ver el comentario en libs.versions.toml sobre por qué la
    // transitiva (3.5.0) falla en este dispositivo de prueba.
    androidTestImplementation(libs.androidx.espresso.core)
    // Necesario para que createAndroidComposeRule pueda lanzar una Activity de prueba
    // (agrega un ComponentActivity de test al manifest instrumentado) — solo en debug,
    // no viaja en el APK de release.
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
