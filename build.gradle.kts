import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractJPackageTask
import javax.inject.Inject

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
val appDescription = "Guía interactiva para aprender a programar desde cero con Kotlin"
val macBundleId = "dev.kuromiichi.init"

// Fijo para siempre: es lo que hace que cada MSI sustituya a la versión instalada en vez de sumarse.
val windowsUpgradeUuid = "50BAFD72-E030-4D55-B94D-0988E64EE21F"
val packagingDirectory = layout.projectDirectory.dir("packaging")

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
            description = appDescription
            modules("java.instrument", "jdk.unsupported", "java.net.http")

            macOS {
                bundleID = macBundleId
                iconFile = packagingDirectory.file("icons/init.icns")
                packageVersion = macInstallerVersion
                dmgPackageVersion = macInstallerVersion
            }
            windows {
                upgradeUuid = windowsUpgradeUuid
                iconFile = packagingDirectory.file("icons/init.ico")
                // Sin permisos de administrador: el actualizador puede lanzar el MSI sin pedir elevación.
                perUserInstall = true
            }
            linux {
                // «init» es un paquete esencial de Debian y Ubuntu.
                packageName = "init-app"
                iconFile = packagingDirectory.file("icons/init.png")
            }
        }
    }
}

/**
 * Crea un instalador con jpackage a partir de la app de `createDistributable`, con los recursos de una carpeta de
 * `packaging/`. Compose vacía su directorio de recursos de jpackage antes de cada empaquetado, así que no admite
 * recursos propios como el fondo del DMG o las imágenes del asistente del MSI.
 */
abstract class StyledInstallerTask : DefaultTask() {
    @get:Inject
    abstract val execOperations: ExecOperations

    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations

    @get:Input
    abstract val javaHome: Property<String>

    @get:InputDirectory
    abstract val appImage: DirectoryProperty

    @get:InputDirectory
    abstract val resources: DirectoryProperty

    @get:Input
    abstract val jpackageArguments: ListProperty<String>

    /** WiX 3, que descarga Compose; solo en Windows. */
    @get:Internal
    abstract val wixToolsetDir: DirectoryProperty

    @get:OutputDirectory
    abstract val destinationDir: DirectoryProperty

    @TaskAction
    fun run() {
        val resourceDir = temporaryDir.resolve("resources")
        val resourcesPath = resources.get().asFile.invariantSeparatorsPath
        fileSystemOperations.sync {
            from(resources)
            into(resourceDir)
            // El proyecto WiX enlaza las imágenes del asistente por ruta absoluta.
            filesMatching("*.wxs") { filter { line: String -> line.replace("@PACKAGING_DIR@", resourcesPath) } }
        }
        fileSystemOperations.delete { delete(destinationDir) }

        val isWindows = System.getProperty("os.name").startsWith("Windows")
        execOperations.exec {
            executable = File(javaHome.get(), if (isWindows) "bin/jpackage.exe" else "bin/jpackage").path
            args(jpackageArguments.get())
            args("--app-image", appImage.get().asFile.path)
            args("--resource-dir", resourceDir.path)
            args("--dest", destinationDir.get().asFile.path)
            args("--verbose")
            wixToolsetDir.orNull?.let { environment("PATH", "${it.asFile.path};${System.getenv("PATH")}") }
        }
    }
}

/**
 * Sustituye el empaquetado de Compose de [composeTaskName] por un [StyledInstallerTask]: `packageDistributionForCurrentOS`
 * y la tarea de Compose siguen siendo la forma de crearlo, y el instalador queda en la misma carpeta.
 */
fun registerStyledInstaller(
    composeTaskName: String,
    operatingSystem: String,
    appImageName: String?,
    resourcesName: String,
    options: Map<String, String?>,
) {
    val createDistributable = tasks.named<AbstractJPackageTask>("createDistributable")
    val composeTask = tasks.named<AbstractJPackageTask>(composeTaskName)
    val styledTask =
        tasks.register<StyledInstallerTask>("${composeTaskName}Styled") {
            group = "compose desktop"
            description = "Crea el instalador de $composeTaskName con los recursos de packaging/$resourcesName."
            onlyIf { System.getProperty("os.name").startsWith(operatingSystem) }
            dependsOn(createDistributable)
            tasks.findByName("unzipWix")?.let { unzipWix -> dependsOn(unzipWix) }
            javaHome = composeTask.flatMap { it.javaHome }
            // Igual que Compose: en macOS, el .app; en Windows, la carpeta de la app.
            appImage = createDistributable.flatMap { task -> appImageName?.let { task.destinationDir.dir(it) } ?: task.destinationDir }
            resources = packagingDirectory.dir(resourcesName)
            jpackageArguments = options.flatMap { (option, value) -> listOfNotNull(option, value) }
            wixToolsetDir = composeTask.flatMap { it.wixToolsetDir }
            destinationDir = composeTask.flatMap { it.destinationDir }
        }
    composeTask.configure {
        dependsOn(styledTask)
        enabled = false
    }
}

afterEvaluate {
    // Compose registra sus tareas de empaquetado al terminar de evaluar el proyecto.
    registerStyledInstaller(
        composeTaskName = "packageDmg",
        operatingSystem = "Mac",
        appImageName = "init.app",
        resourcesName = "macos",
        options =
            mapOf(
                "--type" to "dmg",
                "--name" to "init",
                "--app-version" to macInstallerVersion,
                "--description" to appDescription,
                "--mac-package-identifier" to macBundleId,
            ),
    )
    registerStyledInstaller(
        composeTaskName = "packageMsi",
        operatingSystem = "Windows",
        appImageName = null,
        resourcesName = "windows",
        options =
            mapOf(
                "--type" to "msi",
                "--name" to "init",
                "--app-version" to installerVersion,
                "--description" to appDescription,
                "--win-dir-chooser" to null,
                "--win-per-user-install" to null,
                "--win-upgrade-uuid" to windowsUpgradeUuid,
            ),
    )
}
