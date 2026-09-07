import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.0"
    id("org.jetbrains.compose") version "1.11.1"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
}

group = "dev.kuromiichi"
version = "1.0.0"

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
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

ktlint {
    filter {
        exclude { it.file.path.contains("/build/generated/") }
    }
}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "dev.kuromiichi.damprep.MainKt"

        nativeDistributions {
            nativeDistributionOutput.orNull?.let { outputDirectory ->
                outputBaseDir.set(file(outputDirectory))
            }
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Exe,
                TargetFormat.Deb,
                TargetFormat.Rpm,
            )
            packageName = "DAM Prep"
            description = "Preparación interactiva para el ciclo de Desarrollo de Aplicaciones Multiplataforma"
            modules("java.instrument", "jdk.unsupported")
        }
    }
}
