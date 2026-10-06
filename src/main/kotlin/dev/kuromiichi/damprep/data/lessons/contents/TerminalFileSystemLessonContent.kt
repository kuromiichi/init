package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonConsoleKind
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonStepContent
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.callout
import dev.kuromiichi.damprep.data.lessons.consoleError
import dev.kuromiichi.damprep.data.lessons.consoleInput
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.operatingSystemStep
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem

private const val INTELLIJ_TERMINAL_URL = "https://www.jetbrains.com/help/idea/terminal-emulator.html"
private const val POWERSHELL_URL = "https://learn.microsoft.com/es-es/powershell/scripting/overview"
private const val WINDOWS_TERMINAL_URL = "https://learn.microsoft.com/es-es/windows/terminal/"
private const val MACOS_TERMINAL_URL = "https://support.apple.com/es-es/guide/terminal/welcome/mac"
private const val LINUX_TERMINAL_URL = "https://ubuntu.com/tutorials/command-line-for-beginners"

/** Rutas del proyecto de notas tal como las ve cada sistema. Sin sistema elegido se usa el estilo de macOS y Linux. */
private class TerminalPaths(
    val home: String,
    val project: String,
    val sources: String,
) {
    val isWindows: Boolean = home.contains('\\')
}

private fun terminalPaths(system: TargetOperatingSystem?): TerminalPaths =
    when (system) {
        TargetOperatingSystem.Windows -> TerminalPaths("C:\\Users\\alumno", "C:\\Users\\alumno\\Notas", "C:\\Users\\alumno\\Notas\\src\\main\\kotlin")
        TargetOperatingSystem.Linux -> TerminalPaths("/home/alumno", "/home/alumno/Notas", "/home/alumno/Notas/src/main/kotlin")
        TargetOperatingSystem.MacOS, null -> TerminalPaths("/Users/alumno", "/Users/alumno/Notas", "/Users/alumno/Notas/src/main/kotlin")
    }

/** Lo que imprime `pwd`: una línea en macOS y Linux, una pequeña tabla en PowerShell. */
private fun pwdOutput(
    paths: TerminalPaths,
    path: String,
) = if (paths.isWindows) {
    arrayOf(consoleOutput("Path"), consoleOutput("----"), consoleOutput(path))
} else {
    arrayOf(consoleOutput(path))
}

private fun systemLinks(
    system: TargetOperatingSystem?,
    windows: LessonLink = windowsShellLink,
    macOs: LessonLink = macOsShellLink,
    linux: LessonLink = linuxShellLink,
): List<LessonLink> =
    when (system) {
        TargetOperatingSystem.Windows -> listOf(windows)
        TargetOperatingSystem.MacOS -> listOf(macOs)
        TargetOperatingSystem.Linux -> listOf(linux)
        null -> listOf(windows, macOs, linux)
    }

private val environmentVariablesWindowsLink = LessonLink("Variables de entorno en PowerShell", "https://learn.microsoft.com/es-es/powershell/module/microsoft.powershell.core/about/about_environment_variables")
private val environmentVariablesUnixLink = LessonLink("Variables de entorno de Bash (manual)", "https://www.gnu.org/software/bash/manual/html_node/Environment.html")
private val windowsShellLink = LessonLink("Primeros pasos con PowerShell", POWERSHELL_URL)
private val macOsShellLink = LessonLink("Guía de Terminal en Mac", MACOS_TERMINAL_URL)
private val linuxShellLink = LessonLink("Línea de comandos para principiantes (Ubuntu)", LINUX_TERMINAL_URL)

val terminalFileSystemLessonContent =
    LessonContent(
        lessonId = "terminal-file-system",
        steps =
            listOf(
                LessonStep(
                    title = "Hablar con el ordenador por escrito",
                    explanation =
                        listOf(
                            paragraph(regular("Hasta ahora has hecho casi todo con ventanas y botones. Casi todas las herramientas de un programador también se pueden manejar escribiendo "), highlight("comandos"), regular(" en una "), highlight("terminal"), regular(".")),
                            paragraph(regular("La terminal es la ventana donde escribes. Quien lee y ejecuta lo que escribes es la "), highlight("shell"), regular(": un programa que interpreta cada línea, busca el programa que le pides y te enseña lo que responde.")),
                            paragraph(regular("Git, Gradle y muchas herramientas de servidores se usan así. Un comando se escribe más rápido que una secuencia de clics, se puede copiar en un manual y funciona igual en cualquier ordenador.")),
                        ),
                    visual =
                        LessonVisualContent.Flow(listOf("Escribes un comando", "La shell lo interpreta", "Se ejecuta el programa", "Ves el resultado")),
                    links =
                        listOf(
                            LessonLink("La terminal en IntelliJ IDEA", INTELLIJ_TERMINAL_URL),
                        ),
                ),
                operatingSystemStep("Abrir una terminal") { system ->
                    LessonStepContent(
                        explanation =
                            listOf(
                                when (system) {
                                    TargetOperatingSystem.Windows -> paragraph(regular("Abre "), emphasis("Terminal"), regular(" desde el menú Inicio. En Windows 11 ya viene instalada y arranca "), highlight("PowerShell"), regular(", la shell que usaremos. También sirve PowerShell a secas."))
                                    TargetOperatingSystem.MacOS -> paragraph(regular("Abre "), emphasis("Terminal"), regular(" con Spotlight ("), inlineCode("⌘ Espacio"), regular(") o desde "), emphasis("Aplicaciones › Utilidades"), regular(". La shell por defecto es "), highlight("zsh"), regular("."))
                                    TargetOperatingSystem.Linux -> paragraph(regular("Abre la terminal con "), inlineCode("Ctrl+Alt+T"), regular(" o buscándola en el menú de aplicaciones. La shell habitual es "), highlight("bash"), regular("."))
                                    null -> paragraph(regular("Cada sistema trae su terminal: Terminal con PowerShell en Windows, Terminal con zsh en macOS y una terminal con bash en Linux."))
                                },
                                paragraph(regular("La línea que ves al abrirla se llama "), highlight("prompt"), regular(": indica quién eres, en qué equipo y en qué carpeta estás. Tu comando se escribe a continuación y se ejecuta al pulsar "), inlineCode("Intro"), regular(".")),
                                paragraph(regular("IntelliJ IDEA incluye su propia ventana "), emphasis("Terminal"), regular(" (pestaña inferior o "), inlineCode(if (system == TargetOperatingSystem.MacOS) "⌥ F12" else "Alt+F12"), regular("). Se abre ya en la carpeta del proyecto, lo que ahorra moverse.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                caption = "El prompt",
                                runs =
                                    listOf(
                                        consoleRun(
                                            when (system) {
                                                TargetOperatingSystem.Windows -> consoleOutput("PS C:\\Users\\alumno> ")
                                                TargetOperatingSystem.Linux -> consoleOutput("alumno@portatil:~\$ ")
                                                TargetOperatingSystem.MacOS, null -> consoleOutput("alumno@MacBook-Air ~ % ")
                                            },
                                        ),
                                    ),
                            ),
                        links = systemLinks(system, windows = LessonLink("Windows Terminal", WINDOWS_TERMINAL_URL)) + LessonLink("La terminal en IntelliJ IDEA", INTELLIJ_TERMINAL_URL),
                    )
                },
                LessonStep(
                    title = "Anatomía de un comando",
                    explanation =
                        listOf(
                            paragraph(regular("Un comando tiene hasta tres partes: el "), highlight("programa"), regular(" que quieres ejecutar, las "), highlight("opciones"), regular(" que cambian su comportamiento (suelen empezar por "), inlineCode("-"), regular(") y los "), highlight("argumentos"), regular(" sobre los que actúa.")),
                            paragraph(regular("Ya los has usado: en "), inlineCode("git config --global user.name \"Ana\""), regular(", "), inlineCode("git"), regular(" es el programa, "), inlineCode("config"), regular(" la acción, "), inlineCode("--global"), regular(" una opción y lo demás son argumentos.")),
                            paragraph(regular("Tres trucos te ahorran muchos errores: "), inlineCode("↑"), regular(" recupera el comando anterior, "), inlineCode("Tab"), regular(" completa nombres a medias y "), inlineCode("Ctrl+C"), regular(" cancela lo que se esté ejecutando.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            kind = LessonConsoleKind.Terminal,
                            caption = "Programa, opciones y argumentos",
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("git --version"),
                                        consoleOutput("git version 2.51.0"),
                                        consoleInput("java -version"),
                                        consoleOutput("openjdk version \"25\" 2025-09-16 LTS"),
                                    ),
                                ),
                        ),
                ),
                operatingSystemStep("El árbol de carpetas") { system ->
                    val paths = terminalPaths(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Los archivos se organizan en carpetas dentro de carpetas, como un árbol. Cada elemento tiene una "), highlight("ruta"), regular(": la lista de carpetas que hay que atravesar para llegar a él, empezando por la raíz.")),
                                when (system) {
                                    TargetOperatingSystem.Windows -> paragraph(regular("En Windows hay una raíz por unidad, como "), inlineCode("C:\\"), regular(", y las carpetas se separan con "), inlineCode("\\"), regular(". Tu carpeta personal es "), inlineCode("C:\\Users\\alumno"), regular("."))
                                    TargetOperatingSystem.MacOS -> paragraph(regular("En macOS hay una única raíz, "), inlineCode("/"), regular(", y las carpetas se separan con "), inlineCode("/"), regular(". Tu carpeta personal es "), inlineCode("/Users/alumno"), regular(" y se abrevia "), inlineCode("~"), regular("."))
                                    TargetOperatingSystem.Linux -> paragraph(regular("En Linux hay una única raíz, "), inlineCode("/"), regular(", y las carpetas se separan con "), inlineCode("/"), regular(". Tu carpeta personal es "), inlineCode("/home/alumno"), regular(" y se abrevia "), inlineCode("~"), regular("."))
                                    null -> paragraph(regular("Windows tiene una raíz por unidad ("), inlineCode("C:\\"), regular(") y separa con "), inlineCode("\\"), regular(". macOS y Linux tienen una sola raíz ("), inlineCode("/"), regular(") y separan con "), inlineCode("/"), regular("."))
                                },
                                paragraph(regular("Tu carpeta personal es el sitio natural para guardar proyectos: ahí tienes permiso para crear y borrar sin tocar nada del sistema.")),
                            ),
                        visual =
                            LessonVisualContent.Flow(
                                if (paths.isWindows) {
                                    listOf("C:\\", "Users", "alumno", "Notas", "src\\main\\kotlin")
                                } else {
                                    listOf("/", if (system == TargetOperatingSystem.Linux) "home" else "Users", "alumno", "Notas", "src/main/kotlin")
                                },
                            ),
                        links = systemLinks(system),
                    )
                },
                operatingSystemStep("Dónde estás y qué hay") { system ->
                    val paths = terminalPaths(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("La terminal siempre está "), emphasis("dentro de una carpeta"), regular(", el directorio actual. "), inlineCode("pwd"), regular(" muestra su ruta y "), inlineCode("ls"), regular(" lista lo que contiene.")),
                                if (paths.isWindows) {
                                    paragraph(regular("En PowerShell, "), inlineCode("ls"), regular(" enseña una tabla: "), inlineCode("d"), regular(" en "), inlineCode("Mode"), regular(" marca una carpeta y "), inlineCode("a"), regular(" un archivo. Los comandos de este paso son alias de "), inlineCode("Get-Location"), regular(" y "), inlineCode("Get-ChildItem"), regular("."))
                                } else {
                                    paragraph(regular("Las carpetas y los archivos salen juntos. Con "), inlineCode("ls -l"), regular(" ves una línea por elemento, con permisos, tamaño y fecha; una "), inlineCode("d"), regular(" al principio marca una carpeta."))
                                },
                                paragraph(regular("Los nombres que ves son los del proyecto de notas: el "), inlineCode("src"), regular(" con tu código, el "), inlineCode("gradle"), regular(" y el "), inlineCode("gradlew"), regular(" de Gradle y los "), inlineCode(".kts"), regular(" de configuración.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("pwd"),
                                            *pwdOutput(paths, paths.project),
                                            consoleInput("ls"),
                                            *if (paths.isWindows) {
                                                arrayOf(
                                                    consoleOutput(""),
                                                    consoleOutput("    Directory: ${paths.project}"),
                                                    consoleOutput(""),
                                                    consoleOutput("Mode  LastWriteTime    Length Name"),
                                                    consoleOutput("----  -------------    ------ ----"),
                                                    consoleOutput("d---- 05/10/2026 10:12        gradle"),
                                                    consoleOutput("d---- 05/10/2026 10:12        src"),
                                                    consoleOutput("-a--- 05/10/2026 10:12    512 build.gradle.kts"),
                                                    consoleOutput("-a--- 05/10/2026 10:12   8762 gradlew"),
                                                    consoleOutput("-a--- 05/10/2026 10:12   2966 gradlew.bat"),
                                                    consoleOutput("-a--- 05/10/2026 10:12     28 settings.gradle.kts"),
                                                )
                                            } else {
                                                arrayOf(consoleOutput("build.gradle.kts  gradle  gradlew  gradlew.bat  settings.gradle.kts  src"))
                                            },
                                        ),
                                    ),
                            ),
                        links = systemLinks(system),
                    )
                },
                operatingSystemStep("Moverse con cd") { system ->
                    val paths = terminalPaths(system)
                    val sep = if (paths.isWindows) "\\" else "/"
                    val sources = "src${sep}main${sep}kotlin"
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(inlineCode("cd"), regular(" ("), emphasis("change directory"), regular(") cambia el directorio actual. Con el nombre de una carpeta entras en ella; con "), inlineCode(".."), regular(" subes a la carpeta que la contiene; con "), inlineCode("~"), regular(" vuelves a tu carpeta personal.")),
                                paragraph(regular("No hace falta escribir las rutas enteras: pulsa "), inlineCode("Tab"), regular(" tras las primeras letras y la shell completa el nombre. Si la ruta tiene espacios, enciérrala entre comillas: "), inlineCode("cd \"Mis proyectos\""), regular(".")),
                                paragraph(inlineCode("cd"), regular(" no escribe nada si todo va bien. Para saber dónde has quedado, mira el prompt o usa "), inlineCode("pwd"), regular(".")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("cd Notas"),
                                            consoleInput("cd $sources"),
                                            consoleInput("pwd"),
                                            *pwdOutput(paths, paths.sources),
                                            consoleInput("cd ..$sep.."),
                                            consoleInput("pwd"),
                                            *pwdOutput(paths, paths.project + sep + "src"),
                                            consoleInput("cd ~"),
                                            consoleInput("pwd"),
                                            *pwdOutput(paths, paths.home),
                                        ),
                                    ),
                            ),
                        links = systemLinks(system),
                    )
                },
                operatingSystemStep("Rutas absolutas y relativas") { system ->
                    val paths = terminalPaths(system)
                    val sep = if (paths.isWindows) "\\" else "/"
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Una ruta "), highlight("absoluta"), regular(" empieza en la raíz y sirve desde cualquier sitio. Una ruta "), highlight("relativa"), regular(" parte de la carpeta actual: "), inlineCode("."), regular(" es esa carpeta y "), inlineCode(".."), regular(" la que la contiene.")),
                                paragraph(regular("Por eso el mismo comando puede funcionar en un sitio y fallar en otro. Si "), inlineCode("settings.gradle.kts"), regular(" no está en la carpeta actual, la shell no lo busca por ti.")),
                                paragraph(regular("Cuando veas un error de archivo inexistente, comprueba primero "), inlineCode("pwd"), regular(": casi siempre estás en otra carpeta de la que creías.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                caption = "Ruta relativa y ruta absoluta",
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("cd ~"),
                                            consoleInput("cat settings.gradle.kts"),
                                            if (paths.isWindows) {
                                                consoleError("Get-Content: Cannot find path '${paths.home}\\settings.gradle.kts' because it does not exist.")
                                            } else {
                                                consoleError("cat: settings.gradle.kts: No such file or directory")
                                            },
                                            consoleInput("cat Notas${sep}settings.gradle.kts"),
                                            consoleOutput("rootProject.name = \"Notas\""),
                                            consoleInput("cat ${paths.project}${sep}settings.gradle.kts"),
                                            consoleOutput("rootProject.name = \"Notas\""),
                                        ),
                                    ),
                            ),
                        links = systemLinks(system),
                    )
                },
                operatingSystemStep("Crear, copiar, mover y borrar") { system ->
                    val paths = terminalPaths(system)
                    val sep = if (paths.isWindows) "\\" else "/"
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(inlineCode("mkdir"), regular(" crea una carpeta, "), inlineCode("cp"), regular(" copia un archivo, "), inlineCode("mv"), regular(" lo mueve (y también sirve para renombrar) y "), inlineCode("rm"), regular(" lo borra. Los que mueven o copian necesitan origen y destino.")),
                                paragraph(regular("Para copiar o borrar una carpeta con su contenido hay que añadir la opción "), inlineCode("-r"), regular(" (de "), emphasis("recursivo"), regular("), porque hay que entrar en cada subcarpeta.")),
                                callout(
                                    title = "No hay papelera",
                                    text = richText(inlineCode("rm"), regular(" borra para siempre: no pasa por la papelera y no se puede deshacer. Antes de borrar, comprueba con "), inlineCode("pwd"), regular(" y "), inlineCode("ls"), regular(" dónde estás.")),
                                    tone = LessonCalloutTone.Warning,
                                ),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("mkdir copias"),
                                            *if (paths.isWindows) {
                                                arrayOf(
                                                    consoleOutput(""),
                                                    consoleOutput("    Directory: ${paths.home}"),
                                                    consoleOutput(""),
                                                    consoleOutput("Mode  LastWriteTime    Length Name"),
                                                    consoleOutput("----  -------------    ------ ----"),
                                                    consoleOutput("d---- 05/10/2026 10:20        copias"),
                                                )
                                            } else {
                                                emptyArray()
                                            },
                                            consoleInput("cp Notas${sep}settings.gradle.kts copias$sep"),
                                            consoleInput("mv copias${sep}settings.gradle.kts copias${sep}ajustes.kts"),
                                            consoleInput("rm copias${sep}ajustes.kts"),
                                            consoleInput("rm -r copias"),
                                        ),
                                    ),
                            ),
                        links = systemLinks(system),
                    )
                },
                LessonStep(
                    title = "Leer un archivo de texto",
                    explanation =
                        listOf(
                            paragraph(inlineCode("cat"), regular(" escribe en pantalla el contenido de un archivo de texto. Sirve para echar un vistazo rápido a un archivo de configuración sin abrir el editor.")),
                            paragraph(regular("Sirve con cualquier archivo de texto, sea cual sea su extensión: el código Kotlin, los "), inlineCode(".kts"), regular(" de Gradle o un "), inlineCode(".gitignore"), regular(". En PowerShell "), inlineCode("cat"), regular(" es un alias de "), inlineCode("Get-Content"), regular(", y se usa igual.")),
                            paragraph(regular("Si el archivo es largo, el resultado se va por la pantalla; entonces es mejor abrirlo en IntelliJ IDEA.")),
                        ),
                    visual =
                        LessonVisualContent.Console(
                            kind = LessonConsoleKind.Terminal,
                            runs =
                                listOf(
                                    consoleRun(
                                        consoleInput("cat settings.gradle.kts"),
                                        consoleOutput("rootProject.name = \"Notas\""),
                                    ),
                                ),
                        ),
                ),
                operatingSystemStep("Archivos ocultos") { system ->
                    val paths = terminalPaths(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Los proyectos guardan configuración en archivos y carpetas que normalmente no te interesan: "), inlineCode(".gradle"), regular(", "), inlineCode(".idea"), regular(" o "), inlineCode(".git"), regular(", la carpeta donde Git guarda la historia.")),
                                if (paths.isWindows) {
                                    paragraph(regular("En Windows, un archivo está oculto por un atributo, no por su nombre. Git lo activa en "), inlineCode(".git"), regular(", así que "), inlineCode("ls"), regular(" no la muestra: añade "), inlineCode("-Force"), regular(" para verla."))
                                } else {
                                    paragraph(regular("En macOS y Linux, un nombre que empieza por punto queda oculto. "), inlineCode("ls"), regular(" no lo muestra y "), inlineCode("ls -a"), regular(" ("), emphasis("all"), regular(") sí; los dos primeros, "), inlineCode("."), regular(" y "), inlineCode(".."), regular(", son la carpeta actual y la superior."))
                                },
                                paragraph(regular("Te servirá en la lección siguiente, cuando Git cree "), inlineCode(".git"), regular(" dentro del proyecto.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                caption = "Notas con un repositorio de Git",
                                runs =
                                    listOf(
                                        consoleRun(
                                            if (paths.isWindows) consoleInput("ls -Force") else consoleInput("ls -a"),
                                            *if (paths.isWindows) {
                                                arrayOf(
                                                    consoleOutput(""),
                                                    consoleOutput("    Directory: ${paths.project}"),
                                                    consoleOutput(""),
                                                    consoleOutput("Mode  LastWriteTime    Length Name"),
                                                    consoleOutput("----  -------------    ------ ----"),
                                                    consoleOutput("d--h- 05/10/2026 10:31        .git"),
                                                    consoleOutput("d---- 05/10/2026 10:12        .gradle"),
                                                    consoleOutput("d---- 05/10/2026 10:12        .idea"),
                                                    consoleOutput("d---- 05/10/2026 10:12        gradle"),
                                                    consoleOutput("d---- 05/10/2026 10:12        src"),
                                                    consoleOutput("-a--- 05/10/2026 10:12     61 .gitignore"),
                                                    consoleOutput("-a--- 05/10/2026 10:12    512 build.gradle.kts"),
                                                )
                                            } else {
                                                arrayOf(consoleOutput(".  ..  .git  .gitignore  .gradle  .idea  build.gradle.kts  gradle  gradlew  gradlew.bat  settings.gradle.kts  src"))
                                            },
                                        ),
                                    ),
                            ),
                        links = systemLinks(system),
                    )
                },
                operatingSystemStep("Variables de entorno") { system ->
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Una "), highlight("variable de entorno"), regular(" es un dato con nombre que el sistema ofrece a todos los programas: tu carpeta personal, tu usuario o dónde está instalado Java. Los programas la leen cuando la necesitan.")),
                                when (system) {
                                    TargetOperatingSystem.Windows -> paragraph(regular("En PowerShell se leen con "), inlineCode("\$env:"), regular(" seguido del nombre. Es lo que configuró el instalador de Java al activar "), inlineCode("JAVA_HOME"), regular("."))
                                    TargetOperatingSystem.MacOS, TargetOperatingSystem.Linux -> paragraph(regular("En la shell se leen escribiendo "), inlineCode("\$"), regular(" delante del nombre. "), inlineCode("printenv"), regular(" lista todas las que hay en tu sesión."))
                                    null -> paragraph(regular("En PowerShell se leen con "), inlineCode("\$env:NOMBRE"), regular(" y en macOS y Linux con "), inlineCode("\$NOMBRE"), regular("."))
                                },
                                paragraph(regular("Cambiar una variable en una terminal solo afecta a esa ventana. Para que dure, se guarda en la configuración del sistema o de la shell, que es lo que hacen los instaladores.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            *when (system) {
                                                TargetOperatingSystem.Windows ->
                                                    arrayOf(
                                                        consoleInput("echo \$env:USERNAME"),
                                                        consoleOutput("alumno"),
                                                        consoleInput("echo \$env:USERPROFILE"),
                                                        consoleOutput("C:\\Users\\alumno"),
                                                    )
                                                TargetOperatingSystem.Linux ->
                                                    arrayOf(
                                                        consoleInput("echo \$USER"),
                                                        consoleOutput("alumno"),
                                                        consoleInput("echo \$HOME"),
                                                        consoleOutput("/home/alumno"),
                                                    )
                                                TargetOperatingSystem.MacOS, null ->
                                                    arrayOf(
                                                        consoleInput("echo \$USER"),
                                                        consoleOutput("alumno"),
                                                        consoleInput("echo \$HOME"),
                                                        consoleOutput("/Users/alumno"),
                                                    )
                                            },
                                        ),
                                    ),
                            ),
                        links =
                            systemLinks(system, windows = environmentVariablesWindowsLink, macOs = environmentVariablesUnixLink, linux = environmentVariablesUnixLink),
                    )
                },
                operatingSystemStep("PATH: cómo encuentra la shell los programas") { system ->
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Cuando escribes "), inlineCode("git"), regular(", la shell no sabe dónde está ese programa. Busca en la lista de carpetas de una variable especial, "), inlineCode("PATH"), regular(", hasta dar con un ejecutable con ese nombre.")),
                                when (system) {
                                    TargetOperatingSystem.Windows -> paragraph(inlineCode("Get-Command git"), regular(" te dice cuál ha encontrado. Las carpetas de "), inlineCode("PATH"), regular(" se separan con "), inlineCode(";"), regular(" y puedes verlas una por línea con "), inlineCode("\$env:Path -split ';'"), regular("."))
                                    TargetOperatingSystem.MacOS, TargetOperatingSystem.Linux -> paragraph(inlineCode("which git"), regular(" te dice cuál ha encontrado. Las carpetas de "), inlineCode("PATH"), regular(" se separan con "), inlineCode(":"), regular(" y se recorren de izquierda a derecha."))
                                    null -> paragraph(regular("Para saber cuál ha encontrado, usa "), inlineCode("Get-Command git"), regular(" en PowerShell o "), inlineCode("which git"), regular(" en macOS y Linux."))
                                },
                                paragraph(regular("Si el programa no está en ninguna de esas carpetas, la shell responde que no lo encuentra. Por eso, tras instalar algo, hay que abrir una terminal nueva: la ya abierta conserva el "), inlineCode("PATH"), regular(" antiguo.")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            *when (system) {
                                                TargetOperatingSystem.Windows ->
                                                    arrayOf(
                                                        consoleInput("\$env:Path -split ';'"),
                                                        consoleOutput("C:\\Program Files\\Git\\cmd"),
                                                        consoleOutput("C:\\Program Files\\Eclipse Adoptium\\jdk-25\\bin"),
                                                        consoleInput("Get-Command git"),
                                                        consoleOutput(""),
                                                        consoleOutput("CommandType     Name       Version    Source"),
                                                        consoleOutput("-----------     ----       -------    ------"),
                                                        consoleOutput("Application     git.exe    2.51.0.1   C:\\Program Files\\Git\\cmd\\git.exe"),
                                                        consoleInput("programainventado"),
                                                        consoleError("programainventado: The term 'programainventado' is not recognized as a name of a cmdlet, function, script file, or executable program."),
                                                        consoleError("Check the spelling of the name, or if a path was included, verify that the path is correct and try again."),
                                                    )
                                                TargetOperatingSystem.Linux ->
                                                    arrayOf(
                                                        consoleInput("echo \$PATH"),
                                                        consoleOutput("/usr/local/bin:/usr/bin:/bin"),
                                                        consoleInput("which git"),
                                                        consoleOutput("/usr/bin/git"),
                                                        consoleInput("programainventado"),
                                                        consoleError("bash: programainventado: command not found"),
                                                    )
                                                TargetOperatingSystem.MacOS, null ->
                                                    arrayOf(
                                                        consoleInput("echo \$PATH"),
                                                        consoleOutput("/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin"),
                                                        consoleInput("which git"),
                                                        consoleOutput("/usr/bin/git"),
                                                        consoleInput("programainventado"),
                                                        consoleError("zsh: command not found: programainventado"),
                                                    )
                                            },
                                        ),
                                    ),
                            ),
                        links =
                            systemLinks(system, windows = environmentVariablesWindowsLink, macOs = environmentVariablesUnixLink, linux = environmentVariablesUnixLink),
                    )
                },
                operatingSystemStep("Ejecutar el proyecto desde la terminal") { system ->
                    val gradleBuild = if (system == TargetOperatingSystem.Windows) ".\\gradlew.bat build" else "./gradlew build"
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Todo proyecto de Gradle incluye el "), highlight("Wrapper"), regular(": el script "), inlineCode("gradlew"), regular(" que descarga y usa la versión de Gradle que el proyecto necesita. Se ejecuta desde la carpeta del proyecto, donde está el script.")),
                                when (system) {
                                    TargetOperatingSystem.Windows -> paragraph(regular("En Windows se lanza "), inlineCode("gradlew.bat"), regular(". El "), inlineCode(".\\"), regular(" indica que está en la carpeta actual: PowerShell no busca ahí por seguridad."))
                                    TargetOperatingSystem.MacOS, TargetOperatingSystem.Linux -> paragraph(regular("Se lanza con "), inlineCode("./gradlew"), regular(": el "), inlineCode("./"), regular(" indica que está en la carpeta actual, donde la shell no busca por seguridad. Si responde "), inlineCode("Permission denied"), regular(", ejecuta antes "), inlineCode("chmod +x gradlew"), regular("."))
                                    null -> paragraph(regular("Se lanza con "), inlineCode("./gradlew"), regular(" en macOS y Linux, y con "), inlineCode(".\\gradlew.bat"), regular(" en Windows. El prefijo indica que el script está en la carpeta actual."))
                                },
                                paragraph(inlineCode("build"), regular(" compila, ejecuta las pruebas y empaqueta el proyecto. Si todo va bien, termina con "), inlineCode("BUILD SUCCESSFUL"), regular(".")),
                            ),
                        visual =
                            LessonVisualContent.Console(
                                kind = LessonConsoleKind.Terminal,
                                runs =
                                    listOf(
                                        consoleRun(
                                            consoleInput("cd Notas"),
                                            consoleInput(gradleBuild),
                                            consoleOutput("BUILD SUCCESSFUL in 4s"),
                                            consoleOutput("7 actionable tasks: 7 executed"),
                                        ),
                                    ),
                            ),
                        links =
                            listOf(LessonLink("Gradle Wrapper", "https://docs.gradle.org/current/userguide/gradle_wrapper.html")),
                    )
                },
                LessonStep(
                    title = "Ahora te toca: ordenar tus prácticas",
                    explanation =
                        listOf(
                            paragraph(regular("Vas a crear una carpeta para tus prácticas del curso y a colocar en ella el proyecto de notas, usando solo la terminal.")),
                            paragraph(regular("Todo se hace con "), inlineCode("pwd"), regular(", "), inlineCode("ls"), regular(", "), inlineCode("cd"), regular(", "), inlineCode("mkdir"), regular(", "), inlineCode("cp"), regular(" y "), inlineCode("cat"), regular(". Comprueba con "), inlineCode("ls"), regular(" cada paso antes de seguir.")),
                        ),
                    visual =
                        LessonVisualContent.Flow(listOf("Crear la carpeta", "Copiar el proyecto", "Entrar con rutas relativas", "Compilar con el Wrapper")),
                    links =
                        listOf(
                            LessonLink("La terminal en IntelliJ IDEA", INTELLIJ_TERMINAL_URL),
                            LessonLink("Gradle Wrapper", "https://docs.gradle.org/current/userguide/gradle_wrapper.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "terminal-file-system-workspace",
                            objective = richText(highlight("Moverte por el sistema de ficheros y ejecutar un proyecto sin salir de la terminal.")),
                            statement = richText(regular("Crea la carpeta de prácticas, copia dentro el proyecto de notas y compílalo desde ahí.")),
                            requirements =
                                listOf(
                                    richText(regular("Desde tu carpeta personal, crea "), inlineCode("dam"), regular(" y, dentro de ella, "), inlineCode("programacion"), regular(".")),
                                    richText(regular("Copia la carpeta del proyecto de notas dentro de "), inlineCode("programacion"), regular(" con "), inlineCode("cp -r"), regular(" y confírmalo con "), inlineCode("ls"), regular(".")),
                                    richText(regular("Entra en la copia con una ruta relativa y comprueba dónde estás con "), inlineCode("pwd"), regular(".")),
                                    richText(regular("Lee "), inlineCode("settings.gradle.kts"), regular(" con "), inlineCode("cat"), regular(" y comprueba que contiene el nombre del proyecto.")),
                                    richText(regular("Lanza "), inlineCode("build"), regular(" con el Wrapper de la copia.")),
                                ),
                            expectedOutputKind = LessonConsoleKind.Terminal,
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleInput("cat settings.gradle.kts"),
                                        consoleOutput("rootProject.name = \"Notas\""),
                                        consoleInput("./gradlew build"),
                                        consoleOutput("BUILD SUCCESSFUL in 6s"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Si "), inlineCode("cp"), regular(" se queja de que es una carpeta, falta la opción "), inlineCode("-r"), regular(".")),
                                    richText(regular("Si "), inlineCode("cd"), regular(" no encuentra la carpeta, mira con "), inlineCode("pwd"), regular(" y "), inlineCode("ls"), regular(" en cuál estás y usa "), inlineCode("Tab"), regular(" para completar el nombre.")),
                                    richText(regular("En Windows, el Wrapper se lanza con "), inlineCode(".\\gradlew.bat build"), regular(".")),
                                    richText(regular("Si el Wrapper no se encuentra, no estás en la carpeta del proyecto: el script está junto a "), inlineCode("settings.gradle.kts"), regular(".")),
                                ),
                            extension = richText(regular("Mueve el proyecto original a "), inlineCode("dam/programacion"), regular(" con "), inlineCode("mv"), regular(" y borra la copia con "), inlineCode("rm -r"), regular(", después de comprobar con "), inlineCode("pwd"), regular(" que estás en la carpeta correcta.")),
                            confirmation = richText(regular("Confirma que "), inlineCode("ls"), regular(" muestra el proyecto dentro de "), inlineCode("dam/programacion"), regular(" y que el build termina con "), inlineCode("BUILD SUCCESSFUL"), regular(". El tiempo será distinto en tu ordenador.")),
                        ),
                ),
            ),
    )
