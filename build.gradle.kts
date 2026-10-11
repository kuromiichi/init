import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.0"
    id("org.jetbrains.compose") version "1.11.1"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("com.google.devtools.ksp") version "2.3.10"
    id("androidx.room") version "2.8.4"
}

group = "dev.kuromiichi"

/** Versión del tag sin la `v` (`0.1.0`); la pasa el workflow de releases. Sin ella es una ejecución de desarrollo. */
val appVersion = providers.gradleProperty("appVersion").orElse("dev")

fun gitOutput(vararg arguments: String): Provider<String> =
    providers
        .exec {
            commandLine("git", *arguments)
            isIgnoreExitValue = true
        }.standardOutput.asText
        .map { it.trim() }

val appCommit = providers.gradleProperty("appCommit").orElse(gitOutput("rev-parse", "--short", "HEAD"))

/**
 * Versión numérica de los instaladores. Windows y Linux usan la del tag; macOS exige una mayor de al menos 1, así que
 * durante la etapa 0.x lleva `1.0.<nº de commits>`, que solo sirve para el sistema y siempre crece.
 */
val releaseVersion = appVersion.get().takeIf { Regex("""\d+\.\d+\.\d+""").matches(it) }
val installerVersion = releaseVersion ?: "1.0.0"
val macInstallerVersion =
    if (releaseVersion?.startsWith("0.") == true) "1.0.${gitOutput("rev-list", "--count", "HEAD").get()}" else installerVersion

version = appVersion.get()

repositories {
    google()
    mavenCentral()
}

val nativeDistributionOutput = providers.gradleProperty("nativeDistributionOutput")

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3:1.9.0")
    implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
    implementation("org.jetbrains.compose.components:components-resources:1.11.1")
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.2")
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.sqlite:sqlite-bundled:2.6.2")
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.6")
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.9.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("io.insert-koin:koin-core:4.2.2")
    implementation("io.insert-koin:koin-compose-viewmodel:4.2.2")
    ksp("androidx.room:room-compiler:2.8.4")
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("org.jetbrains.compose.ui:ui-test:1.11.1")
}

kotlin {
    jvmToolchain(25)
}

ktlint {
    filter {
        exclude { it.file.path.contains("/build/generated/") }
    }
}

val appVersionResourcesDirectory = layout.buildDirectory.dir("generated/appVersion")

val generateAppVersionInfo =
    tasks.register<WriteProperties>("generateAppVersionInfo") {
        destinationFile = appVersionResourcesDirectory.map { it.file("init-version.properties") }
        property("version", appVersion)
        property("commit", appCommit)
    }

sourceSets.main {
    resources.srcDir(generateAppVersionInfo.map { appVersionResourcesDirectory.get() })
}

room {
    schemaDirectory("$projectDir/schemas")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

compose.desktop {
    application {
        mainClass = "dev.kuromiichi.init.MainKt"
        jvmArgs("--enable-native-access=ALL-UNNAMED")

        nativeDistributions {
            nativeDistributionOutput.orNull?.let { outputDirectory ->
                outputBaseDir.set(file(outputDirectory))
            }
            // Un formato por sistema: es el que descarga e instala el actualizador.
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "init"
            packageVersion = installerVersion
            description = "Guía interactiva para aprender a programar desde cero con Kotlin"
            modules("java.instrument", "jdk.unsupported", "java.net.http")

            macOS {
                bundleID = "dev.kuromiichi.init"
                packageVersion = macInstallerVersion
                dmgPackageVersion = macInstallerVersion
            }
            windows {
                // Fijo para siempre: es lo que hace que cada MSI sustituya a la versión instalada en vez de sumarse.
                upgradeUuid = "50BAFD72-E030-4D55-B94D-0988E64EE21F"
                // Sin permisos de administrador: el actualizador puede lanzar el MSI sin pedir elevación.
                perUserInstall = true
            }
            linux {
                // «init» es un paquete esencial de Debian y Ubuntu.
                packageName = "init-app"
            }
        }
    }
}
