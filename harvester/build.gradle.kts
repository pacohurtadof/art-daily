plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}

application {
    mainClass.set("com.artdaily.harvester.MainKt")
    // sqlite-jdbc carga una librería nativa; sin esto, versiones futuras de Java lo bloquean.
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

dependencies {
    implementation(project(":core-model"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    // SQLite plano (no Room aquí — el harvester genera el .db directamente
    // con el driver JDBC de SQLite, sin necesidad del framework Android)
    implementation("org.xerial:sqlite-jdbc:3.46.1.3")

    testImplementation(libs.junit)
}
