package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonCalloutTone
import dev.kuromiichi.init.data.lessons.LessonCodeExample
import dev.kuromiichi.init.data.lessons.LessonCodeLine
import dev.kuromiichi.init.data.lessons.LessonContent
import dev.kuromiichi.init.data.lessons.LessonLink
import dev.kuromiichi.init.data.lessons.LessonPractice
import dev.kuromiichi.init.data.lessons.LessonStep
import dev.kuromiichi.init.data.lessons.LessonStepContent
import dev.kuromiichi.init.data.lessons.LessonVisualContent
import dev.kuromiichi.init.data.lessons.callout
import dev.kuromiichi.init.data.lessons.codeLine
import dev.kuromiichi.init.data.lessons.consoleOutput
import dev.kuromiichi.init.data.lessons.consoleRun
import dev.kuromiichi.init.data.lessons.emphasis
import dev.kuromiichi.init.data.lessons.explanationCode
import dev.kuromiichi.init.data.lessons.highlight
import dev.kuromiichi.init.data.lessons.inlineCode
import dev.kuromiichi.init.data.lessons.kotlinCode
import dev.kuromiichi.init.data.lessons.operatingSystemStep
import dev.kuromiichi.init.data.lessons.paragraph
import dev.kuromiichi.init.data.lessons.plain
import dev.kuromiichi.init.data.lessons.regular
import dev.kuromiichi.init.data.lessons.richText
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem

private val kotlinIoPathLink = LessonLink("Referencia de kotlin.io.path", "https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io.path/")

/** Contenido de un archivo de texto, sin resaltado de Kotlin. */
private fun plainText(source: String): List<LessonCodeLine> = source.lines().map { line -> codeLine(plain(line)) }

/** La ruta de `datos/notas.txt` tal como la muestra cada sistema. Sin sistema elegido se usa el estilo de macOS. */
private fun notesPaths(system: TargetOperatingSystem?): Pair<String, String> =
    when (system) {
        TargetOperatingSystem.Windows -> "datos\\notas.txt" to "C:\\Users\\alumno\\Notas\\datos\\notas.txt"
        TargetOperatingSystem.Linux -> "datos/notas.txt" to "/home/alumno/Notas/datos/notas.txt"
        TargetOperatingSystem.MacOS, null -> "datos/notas.txt" to "/Users/alumno/Notas/datos/notas.txt"
    }

val filesDirectoriesLessonContent =
    LessonContent(
        lessonId = "files-directories",
        steps =
            listOf(
                LessonStep(
                    title = "Datos que sobreviven al programa",
                    explanation =
                        listOf(
                            paragraph(regular("Hasta ahora, lo que guardaba tu programa vivía en variables, en la memoria. Al terminar desaparecía, y cada ejecución empezaba de cero.")),
                            paragraph(regular("Un "), highlight("archivo"), regular(", o "), highlight("fichero"), regular(", guarda los datos en el disco. Sigue ahí cuando el programa termina, cuando apagas el ordenador y cuando otro programa lo abre.")),
                            paragraph(regular("En esta lección leerás y escribirás archivos de texto desde Kotlin, organizarás carpetas y harás que tu programa aguante archivos que faltan o que vienen mal escritos.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Ejecución 1: guarda las notas", "notas.txt en el disco", "Ejecución 2: las recupera")),
                ),
                operatingSystemStep("Rutas en Kotlin") { system ->
                    val (relative, absolute) = notesPaths(system)
                    LessonStepContent(
                        explanation =
                            listOf(
                                paragraph(regular("Kotlin representa una ruta con el tipo "), inlineCode("Path"), regular(". "), inlineCode("Path(\"datos\")"), regular(" la crea a partir de un texto y el operador "), inlineCode("/"), regular(" añade lo que hay dentro: "), inlineCode("Path(\"datos\") / \"notas.txt\""), regular(" es lo mismo que "), inlineCode("Path(\"datos/notas.txt\")"), regular(".")),
                                paragraph(regular("Crear un "), inlineCode("Path"), regular(" no toca el disco: es solo una dirección, y el archivo puede no existir todavía. Sus propiedades "), inlineCode("nameWithoutExtension"), regular(" y "), inlineCode("extension"), regular(" separan el nombre de la extensión.")),
                                paragraph(regular("Como en la terminal, una ruta relativa parte de la "), highlight("carpeta de trabajo"), regular(". Si ejecutas desde IntelliJ IDEA o con "), inlineCode("gradlew run"), regular(", es la raíz del proyecto, la que contiene "), inlineCode("build.gradle.kts"), regular(". "), inlineCode("absolute()"), regular(" muestra la ruta completa.")),
                                callout(
                                    "Escribe / en el código",
                                    if (system == TargetOperatingSystem.Windows) {
                                        richText(inlineCode("Path"), regular(" entiende "), inlineCode("/"), regular(" en todos los sistemas. Al mostrar la ruta, Windows usa "), inlineCode("\\"), regular(", pero en el código puedes escribir "), inlineCode("/"), regular(" y el programa funcionará también en macOS y Linux."))
                                    } else {
                                        richText(inlineCode("Path"), regular(" entiende "), inlineCode("/"), regular(" en todos los sistemas y, al mostrar la ruta, usa el separador del tuyo. Una ruta escrita con "), inlineCode("\\"), regular(" solo funcionaría en Windows."))
                                    },
                                    LessonCalloutTone.Tip,
                                ),
                            ),
                        visual =
                            LessonVisualContent.CodeExamples(
                                listOf(
                                    LessonCodeExample(
                                        title = "Una ruta y sus partes",
                                        lines =
                                            kotlinCode(
                                                """
                                                import kotlin.io.path.Path
                                                import kotlin.io.path.absolute
                                                import kotlin.io.path.div
                                                import kotlin.io.path.extension
                                                import kotlin.io.path.nameWithoutExtension

                                                fun main() {
                                                    val datos = Path("datos")
                                                    val notas = datos / "notas.txt"

                                                    println(notas)
                                                    println(notas.nameWithoutExtension)
                                                    println(notas.extension)
                                                    println(notas.absolute())
                                                }
                                                """.trimIndent(),
                                            ),
                                        note = richText(regular("En el proyecto "), inlineCode("Notas"), regular(" muestra "), inlineCode(relative), regular(", "), inlineCode("notas"), regular(", "), inlineCode("txt"), regular(" y "), inlineCode(absolute), regular(".")),
                                    ),
                                ),
                            ),
                        links =
                            listOf(
                                kotlinIoPathLink,
                                LessonLink("Rutas en Java: la clase Path", "https://dev.java/learn/java-io/file-system/path/"),
                            ),
                    )
                },
                LessonStep(
                    title = "Escribir texto",
                    explanation =
                        listOf(
                            paragraph(inlineCode("writeText"), regular(" escribe un texto en el archivo: si no existe lo crea y, si existe, reemplaza todo lo que tenía. "), inlineCode("appendText"), regular(" añade al final sin tocar lo anterior.")),
                            paragraph(regular("Ninguna de las dos añade saltos de línea: el "), inlineCode("\\n"), regular(" del final de cada texto es lo que separa las líneas.")),
                            explanationCode(
                                "diario.txt",
                                richText(regular("El archivo tras ejecutar el programa. Aparece en la raíz del proyecto, en la ventana "), emphasis("Project"), regular(", y se abre como cualquier otro.")),
                                "Lunes: bucles",
                                "Martes: listas",
                            ),
                            callout(
                                "writeText no pregunta",
                                richText(regular("Si apuntas por error a un archivo que ya existe, su contenido se pierde. Como con "), inlineCode("rm"), regular(" en la terminal, no hay papelera.")),
                                LessonCalloutTone.Warning,
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Un diario de estudio",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.io.path.Path
                                            import kotlin.io.path.appendText
                                            import kotlin.io.path.writeText

                                            fun main() {
                                                val diario = Path("diario.txt")

                                                diario.writeText("Lunes: bucles\n")
                                                diario.appendText("Martes: listas\n")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Ejecútalo dos veces: el archivo sigue teniendo dos líneas, porque "), inlineCode("writeText"), regular(" empieza de cero cada vez.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            kotlinIoPathLink,
                        ),
                ),
                LessonStep(
                    title = "Leer texto",
                    explanation =
                        listOf(
                            paragraph(inlineCode("readText"), regular(" devuelve el archivo entero en un "), inlineCode("String"), regular(", saltos de línea incluidos. Es cómodo con archivos pequeños que tratas como un todo.")),
                            paragraph(inlineCode("readLines"), regular(" lo parte en una "), inlineCode("List<String>"), regular(", un elemento por línea y ya sin el "), inlineCode("\\n"), regular(". Es lo habitual cuando cada línea es un dato.")),
                            paragraph(regular("Desde aquí, los ejemplos no muestran los "), inlineCode("import"), regular(": todas estas funciones están en "), inlineCode("kotlin.io.path"), regular(", e IntelliJ IDEA te propone añadirlos en cuanto escribes su nombre.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Leer el diario",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            fun main() {
                                                val diario = Path("diario.txt")

                                                val texto = diario.readText()
                                                println("Caracteres: ${texto.length}")

                                                val lineas = diario.readLines()
                                                println("Líneas: ${lineas.size}")
                                                for (linea in lineas) {
                                                    println("- $linea")
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Con el diario del paso anterior muestra "), inlineCode("Caracteres: 29"), regular(", "), inlineCode("Líneas: 2"), regular(" y cada entrada con un guion delante.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            kotlinIoPathLink,
                        ),
                ),
                LessonStep(
                    title = "Texto, bytes y codificación",
                    explanation =
                        listOf(
                            paragraph(regular("Para el disco, todo archivo es una fila de bytes. En uno de texto, esos bytes representan caracteres, y por eso lo abre cualquier editor. Una imagen o un "), inlineCode(".jar"), regular(" son archivos "), highlight("binarios"), regular(": sus bytes solo los entiende el programa que los usa.")),
                            paragraph(regular("La regla que convierte caracteres en bytes es la "), highlight("codificación"), regular(". La más usada es "), emphasis("UTF-8"), regular(": la "), inlineCode("a"), regular(" ocupa un byte y la "), inlineCode("ñ"), regular(", dos. Por eso "), inlineCode("readBytes"), regular(", que devuelve los bytes sin interpretarlos, cuenta cuatro en «año».")),
                            paragraph(regular("Las funciones de "), inlineCode("kotlin.io.path"), regular(" usan UTF-8 si no indicas otra. El ejemplo lee a propósito el mismo archivo con "), emphasis("ISO-8859-1"), regular(", la de algunos programas antiguos, y la "), inlineCode("ñ"), regular(" sale como "), inlineCode("Ã±"), regular(".")),
                            callout(
                                "Tildes que salen mal",
                                richText(regular("Si ves cosas como "), inlineCode("Ã©"), regular(" donde debería haber una "), inlineCode("é"), regular(", el archivo se escribió con una codificación y se está leyendo con otra. Averigua cuál usó el programa que lo creó y pásala a "), inlineCode("readText"), regular(".")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Una palabra, dos lecturas",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val ruta = Path("palabra.txt")
                                                ruta.writeText("año")

                                                println(ruta.readBytes().size)
                                                println(ruta.readText())
                                                println(
                                                    ruta.readText(Charsets.ISO_8859_1),
                                                )
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("4"), regular(", "), inlineCode("año"), regular(" y "), inlineCode("aÃ±o"), regular(": los mismos bytes, leídos con dos codificaciones.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("UTF-8 en la Wikipedia", "https://es.wikipedia.org/wiki/UTF-8"),
                            LessonLink("Codificación de archivos en IntelliJ IDEA", "https://www.jetbrains.com/help/idea/encoding.html"),
                        ),
                ),
                LessonStep(
                    title = "Una línea por registro",
                    explanation =
                        listOf(
                            paragraph(regular("Para guardar objetos, conviértelos en texto: una línea por objeto, con sus propiedades separadas por un carácter que no aparezca en los datos, como "), inlineCode(";"), regular(". "), inlineCode("writeLines"), regular(" escribe una lista de textos, cada uno en su línea.")),
                            paragraph(regular("Para recuperarlos se hace lo contrario: "), inlineCode("split(\";\")"), regular(" parte cada línea en una lista de textos y con ellos se vuelve a construir el objeto.")),
                            paragraph(regular("Las funciones del ejemplo reciben la ruta como parámetro. Su tipo, "), inlineCode("Path"), regular(", se importa de "), inlineCode("java.nio.file"), regular(".")),
                            explanationCode(
                                "alumnos.txt",
                                richText(regular("Lo que escribe "), inlineCode("guardar"), regular(". Es la idea de los archivos CSV, que verás en la lección siguiente.")),
                                "Ana;8.5",
                                "Luis;6.0",
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Guardar y cargar alumnos",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            data class Alumno(
                                                val nombre: String,
                                                val nota: Double,
                                            )

                                            fun guardar(
                                                alumnos: List<Alumno>,
                                                ruta: Path,
                                            ) {
                                                val lineas =
                                                    alumnos.map {
                                                        "${it.nombre};${it.nota}"
                                                    }
                                                ruta.writeLines(lineas)
                                            }

                                            fun cargar(ruta: Path): List<Alumno> =
                                                ruta.readLines().map { linea ->
                                                    val partes = linea.split(";")
                                                    val nota = partes[1].toDouble()
                                                    Alumno(partes[0], nota)
                                                }

                                            fun main() {
                                                val ruta = Path("alumnos.txt")
                                                val alumnos =
                                                    listOf(
                                                        Alumno("Ana", 8.5),
                                                        Alumno("Luis", 6.0),
                                                    )

                                                guardar(alumnos, ruta)
                                                println(cargar(ruta))
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("[Alumno(nombre=Ana, nota=8.5), Alumno(nombre=Luis, nota=6.0)]"), regular(": lo mismo que se guardó.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            kotlinIoPathLink,
                        ),
                ),
                LessonStep(
                    title = "Cuando el archivo no está",
                    explanation =
                        listOf(
                            paragraph(regular("Leer un archivo que no existe lanza "), inlineCode("NoSuchFileException"), regular(". Es una "), inlineCode("IOException"), regular(", la familia de errores de entrada y salida: archivo inexistente, sin permiso de lectura, disco lleno…")),
                            paragraph(regular("Que falte la primera vez que se ejecuta el programa es normal, no un error. Compruébalo con "), inlineCode("exists()"), regular(" o "), inlineCode("notExists()"), regular(" y decide qué hacer, como empezar con la lista vacía.")),
                            paragraph(regular("Lo que no puedes prever, como un archivo sin permisos, se captura con "), inlineCode("try"), regular(" y "), inlineCode("catch (e: IOException)"), regular(", como en la lección de excepciones. "), inlineCode("IOException"), regular(" se importa de "), inlineCode("java.io"), regular(".")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Leer sin comprobar",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val ruta = Path("alumnos.txt")
                                                println(ruta.readLines())
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Sin "), inlineCode("alumnos.txt"), regular(", termina con "), inlineCode("java.nio.file.NoSuchFileException: alumnos.txt"), regular(".")),
                                ),
                                LessonCodeExample(
                                    title = "Comprobar lo esperado, capturar lo imprevisto",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            fun main() {
                                                val ruta = Path("alumnos.txt")

                                                if (ruta.notExists()) {
                                                    println("Aún no hay alumnos")
                                                    return
                                                }

                                                try {
                                                    println(ruta.readLines())
                                                } catch (e: IOException) {
                                                    val motivo = e.message
                                                    println("No se pudo leer: $motivo")
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Sin el archivo muestra "), inlineCode("Aún no hay alumnos"), regular(" y termina con normalidad.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Excepciones de entrada y salida en Java", "https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/IOException.html"),
                        ),
                ),
                LessonStep(
                    title = "Líneas que no encajan",
                    explanation =
                        listOf(
                            paragraph(regular("Un archivo puede haberlo escrito una persona o un programa con errores. Si una sola línea mal escrita tumba el programa, como haría "), inlineCode("toDouble"), regular(" en el "), inlineCode("cargar"), regular(" de «Una línea por registro», se pierden también todos los datos buenos.")),
                            paragraph(regular("Es mejor validar cada línea. "), inlineCode("getOrNull(1)"), regular(" devuelve "), inlineCode("null"), regular(" si a la línea le falta la segunda parte, y "), inlineCode("toDoubleOrNull"), regular(", si no es un número.")),
                            paragraph(regular("En el ejemplo, las líneas válidas se cargan y de las demás se avisa con su número, contado desde 1, para que se puedan corregir. "), inlineCode("continue"), regular(" salta a la línea siguiente.")),
                            explanationCode(
                                "alumnos.txt",
                                richText(regular("Un archivo con una nota escrita en letra y una línea sin nota.")),
                                "Ana;8.5",
                                "Luis;seis",
                                "Marta",
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Cargar lo que se pueda",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            fun cargar(ruta: Path): List<Alumno> {
                                                val alumnos = mutableListOf<Alumno>()
                                                val lineas = ruta.readLines()
                                                for ((i, linea) in lineas.withIndex()) {
                                                    val numero = i + 1
                                                    val partes = linea.split(";")
                                                    val nota = partes.getOrNull(1)
                                                        ?.toDoubleOrNull()
                                                    if (nota == null) {
                                                        println("Salto la línea $numero")
                                                        continue
                                                    }
                                                    alumnos.add(Alumno(partes[0], nota))
                                                }
                                                return alumnos
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("Salto la línea 2"), regular(" y "), inlineCode("Salto la línea 3"), regular(", y devuelve solo a Ana.")),
                                ),
                            ),
                        ),
                ),
                LessonStep(
                    title = "Abrir, usar y cerrar",
                    explanation =
                        listOf(
                            paragraph(inlineCode("readLines"), regular(" carga el archivo entero en memoria. Con uno de millones de líneas, como el registro de un servidor, conviene ir línea a línea.")),
                            paragraph(regular("Para eso el archivo se abre, se recorre y se cierra. Un archivo abierto es un recurso del sistema: si no se cierra, lo escrito puede no llegar al disco y otros programas pueden no poder usarlo.")),
                            paragraph(inlineCode("use"), regular(" lo cierra al terminar su bloque, aunque salte una excepción, como un "), inlineCode("finally"), regular(". "), inlineCode("bufferedWriter"), regular(" abre el archivo para escribir poco a poco y "), inlineCode("useLines"), regular(" lo abre para leer.")),
                            paragraph(inlineCode("useLines"), regular(" entrega las líneas en una "), highlight("secuencia"), regular(" ("), inlineCode("Sequence"), regular("): se recorre con las mismas funciones que una lista, como "), inlineCode("count"), regular(", pero cada línea se lee cuando hace falta y no se guarda.")),
                            callout(
                                "Solo cuando abres tú",
                                richText(inlineCode("writeText"), regular(", "), inlineCode("readLines"), regular(" y las demás funciones de la lección abren y cierran el archivo por ti. Solo necesitas "), inlineCode("use"), regular(" cuando lo abres tú.")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Cien mil líneas",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            fun main() {
                                                val ruta = Path("numeros.txt")

                                                ruta.bufferedWriter().use { escritor ->
                                                    for (n in 1..100_000) {
                                                        escritor.write("$n")
                                                        escritor.newLine()
                                                    }
                                                }

                                                val pares = ruta.useLines { lineas ->
                                                    lineas.count { it.toInt() % 2 == 0 }
                                                }
                                                println("Pares: $pares")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Escribe los números del 1 al 100 000, uno por línea, y muestra "), inlineCode("Pares: 50000"), regular(" sin tener nunca el archivo entero en memoria.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("La función use", "https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/use.html"),
                            LessonLink("Secuencias", "https://kotlinlang.org/docs/sequences.html"),
                        ),
                ),
                LessonStep(
                    title = "Carpetas",
                    explanation =
                        listOf(
                            paragraph(inlineCode("writeText"), regular(" no crea carpetas: si "), inlineCode("datos"), regular(" no existe, falla con "), inlineCode("NoSuchFileException"), regular(". "), inlineCode("createDirectories"), regular(" crea la carpeta y las que falten por encima, y no hace nada si ya existe, así que se puede llamar al empezar.")),
                            paragraph(inlineCode("listDirectoryEntries"), regular(" devuelve lo que hay dentro. Con un patrón como "), inlineCode("\"*.txt\""), regular(" se queda con lo que encaja: "), inlineCode("*"), regular(" vale por cualquier texto. El orden no está garantizado, y por eso el ejemplo los ordena con "), inlineCode("sorted()"), regular(".")),
                            paragraph(inlineCode("isDirectory()"), regular(" dice si una ruta es una carpeta, y "), inlineCode("deleteIfExists()"), regular(" borra un archivo o una carpeta vacía. Devuelve "), inlineCode("false"), regular(" si no había nada que borrar.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Preparar la carpeta de datos",
                                    lines =
                                        kotlinCode(
                                            """
                                            fun main() {
                                                val datos = Path("datos")
                                                datos.createDirectories()

                                                (datos / "lunes.txt").writeText("")
                                                (datos / "martes.txt").writeText("")
                                                (datos / "notas.csv").writeText("")

                                                val textos =
                                                    datos.listDirectoryEntries("*.txt")
                                                for (ruta in textos.sorted()) {
                                                    println(ruta.nameWithoutExtension)
                                                }

                                                (datos / "lunes.txt").deleteIfExists()
                                                val resto = datos.listDirectoryEntries()
                                                println(resto.size)
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Muestra "), inlineCode("lunes"), regular(", "), inlineCode("martes"), regular(" y "), inlineCode("2"), regular(": tras borrar "), inlineCode("lunes.txt"), regular(" quedan "), inlineCode("martes.txt"), regular(" y "), inlineCode("notas.csv"), regular(". "), inlineCode("writeText(\"\")"), regular(" crea un archivo vacío.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            kotlinIoPathLink,
                        ),
                ),
                LessonStep(
                    title = "File, la forma antigua",
                    explanation =
                        listOf(
                            paragraph(regular("En código Java y en muchos tutoriales verás "), inlineCode("java.io.File"), regular(", la forma antigua de representar una ruta. Kotlin le añade las mismas funciones de texto, así que el código se parece mucho.")),
                            paragraph(inlineCode("Path"), regular(", de "), inlineCode("java.nio.file"), regular(", la sustituyó: explica mejor los fallos y maneja más cosas del sistema de archivos, como permisos y enlaces. Por ejemplo, "), inlineCode("delete()"), regular(" de "), inlineCode("File"), regular(" solo devuelve "), inlineCode("false"), regular(" cuando no puede borrar; con "), inlineCode("Path"), regular(", la excepción dice por qué.")),
                            paragraph(regular("Si una biblioteca te da un "), inlineCode("File"), regular(", "), inlineCode("toPath()"), regular(" lo convierte en "), inlineCode("Path"), regular(", y "), inlineCode("toFile()"), regular(" hace lo contrario.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Con File",
                                    lines =
                                        kotlinCode(
                                            """
                                            import java.io.File

                                            fun main() {
                                                val notas = File("datos", "notas.txt")
                                                if (notas.exists()) {
                                                    println(notas.readLines())
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Lo encontrarás en código existente.")),
                                ),
                                LessonCodeExample(
                                    title = "Con Path",
                                    lines =
                                        kotlinCode(
                                            """
                                            import kotlin.io.path.Path
                                            import kotlin.io.path.exists
                                            import kotlin.io.path.readLines

                                            fun main() {
                                                val notas = Path("datos", "notas.txt")
                                                if (notas.exists()) {
                                                    println(notas.readLines())
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Lo mismo con "), inlineCode("Path"), regular(". Es lo que usarás en tu código.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("De File a Path en Java", "https://docs.oracle.com/javase/tutorial/essential/io/legacy.html"),
                        ),
                ),
                LessonStep(
                    title = "Probar código que usa archivos",
                    explanation =
                        listOf(
                            paragraph(regular("Una prueba no debería usar los archivos de tu proyecto: podría estropearlos o fallar porque otra prueba los cambió.")),
                            paragraph(inlineCode("createTempDirectory()"), regular(" crea una carpeta nueva y vacía en la carpeta temporal del sistema. Cada prueba prepara ahí sus archivos, llama a la función y comprueba el resultado.")),
                            paragraph(regular("Por eso "), inlineCode("guardar"), regular(" y "), inlineCode("cargar"), regular(" reciben la ruta como parámetro en lugar de escribirla dentro: el programa les pasa la de verdad y la prueba, una temporal.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "src/test/kotlin/AlumnosTest.kt",
                                    fileName = "AlumnosTest.kt",
                                    lines =
                                        kotlinCode(
                                            """
                                            class AlumnosTest {
                                                @Test
                                                fun `ignora las líneas sin nota`() {
                                                    val carpeta = createTempDirectory()
                                                    val ruta = carpeta / "alumnos.txt"
                                                    ruta.writeText("Ana;8.5\nLuis;seis")
                                                    val ana = Alumno("Ana", 8.5)

                                                    val alumnos = cargar(ruta)

                                                    assertEquals(listOf(ana), alumnos)
                                                }

                                                @Test
                                                fun `cargar devuelve lo guardado`() {
                                                    val carpeta = createTempDirectory()
                                                    val ruta = carpeta / "alumnos.txt"
                                                    val ana = Alumno("Ana", 8.5)
                                                    val alumnos = listOf(ana)

                                                    guardar(alumnos, ruta)

                                                    assertEquals(alumnos, cargar(ruta))
                                                }
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Cada prueba trabaja en su propia carpeta temporal, así que no se pisan entre ellas.")),
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("createTempDirectory", "https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io.path/create-temp-directory.html"),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: los gastos del mes",
                    explanation =
                        listOf(
                            paragraph(regular("Vas a escribir un programa que resume los gastos de un mes a partir de un archivo que podrías ir completando a mano.")),
                            paragraph(regular("En la raíz de tu proyecto, crea la carpeta "), inlineCode("datos"), regular(" y, dentro, "), inlineCode("gastos.txt"), regular(" con este contenido. Cada línea es "), inlineCode("fecha;concepto;categoría;importe"), regular(", y una está mal escrita a propósito.")),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Los gastos de noviembre",
                                    fileName = "gastos.txt",
                                    lines =
                                        plainText(
                                            """
                                            2026-11-02;Menú del día;Comida;11.50
                                            2026-11-03;Abono transporte;Transporte;20.00
                                            2026-11-05;Cine;Ocio;siete
                                            2026-11-08;Supermercado;Comida;34.25
                                            2026-11-12;Libro de Kotlin;Estudios;29.75
                                            2026-11-15;Gasolina;Transporte;45.00
                                            2026-11-20;Concierto;Ocio;25.00
                                            2026-11-27;Supermercado;Comida;41.75
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Los importes usan punto decimal, como los "), inlineCode("Double"), regular(" de Kotlin.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            kotlinIoPathLink,
                            LessonLink("createTempDirectory", "https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io.path/create-temp-directory.html"),
                        ),
                    practice =
                        LessonPractice(
                            id = "files-directories-expenses",
                            objective = richText(highlight("Guardar y recuperar datos en archivos de texto con un programa que no se rompe cuando un archivo falta o viene mal escrito.")),
                            statement = richText(regular("Escribe un programa que lea los gastos, muestre cuánto se ha gastado en cada categoría y en total, y guarde ese mismo resumen en un archivo.")),
                            requirements =
                                listOf(
                                    richText(regular("Cada gasto es un "), inlineCode("data class Gasto(val concepto: String, val categoria: String, val importe: Double)"), regular(".")),
                                    richText(inlineCode("leerGastos(ruta: Path): List<Gasto>"), regular(" devuelve los gastos de las líneas válidas. Por cada línea que no se pueda leer muestra "), inlineCode("Línea N ignorada:"), regular(" seguido de la línea, con "), inlineCode("N"), regular(" contado desde 1.")),
                                    richText(regular("El resumen muestra las categorías de mayor a menor gasto, con el importe como lo escribe "), inlineCode("println"), regular(" y el símbolo "), inlineCode("€"), regular(", y termina con el total.")),
                                    richText(inlineCode("escribirResumen(gastos: List<Gasto>, ruta: Path)"), regular(" guarda en "), inlineCode("ruta"), regular(" las líneas del resumen, sin los avisos. El programa lo guarda en "), inlineCode("datos/resumen.txt"), regular(".")),
                                    richText(regular("Si "), inlineCode("datos/gastos.txt"), regular(" no existe, el programa muestra "), inlineCode("No encuentro datos/gastos.txt"), regular(" y termina sin excepción.")),
                                    richText(regular("Crea "), inlineCode("GastosTest"), regular(" con pruebas de "), inlineCode("leerGastos"), regular(" y "), inlineCode("escribirResumen"), regular(" que no usen los archivos de "), inlineCode("datos"), regular(" y pasen con "), inlineCode("./gradlew test"), regular(" ("), inlineCode(".\\gradlew.bat test"), regular(" en Windows).")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Línea 3 ignorada: 2026-11-05;Cine;Ocio;siete"),
                                        consoleOutput("Comida: 87.5 €"),
                                        consoleOutput("Transporte: 65.0 €"),
                                        consoleOutput("Estudios: 29.75 €"),
                                        consoleOutput("Ocio: 25.0 €"),
                                        consoleOutput("Total: 207.25 €"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Empieza por leer el archivo y mostrar cada línea tal cual. Después, parte cada una y construye el "), inlineCode("Gasto"), regular(": repasa «Leer texto» y «Una línea por registro». La fecha no hace falta guardarla.")),
                                    richText(regular("Para saltar las líneas mal escritas sin que el programa se detenga, y saber su número, repasa «Líneas que no encajan».")),
                                    richText(regular("Para los totales, agrupa los gastos por categoría y suma los importes de cada grupo, como en la lección de genéricos y colecciones. Luego ordena por el total, de mayor a menor.")),
                                    richText(regular("Comprueba que el archivo existe antes de leerlo, como en «Cuando el archivo no está». Para las pruebas, escribe un archivo pequeño en una carpeta de "), inlineCode("createTempDirectory()"), regular(" y compara lo que devuelve o escribe cada función.")),
                                ),
                            extension = richText(regular("Pide un gasto nuevo por teclado y añádelo al final de "), inlineCode("gastos.txt"), regular(" sin borrar los demás. Al ejecutar el programa otra vez, el resumen debe incluirlo.")),
                            confirmation = richText(regular("Confirma que la salida coincide con la esperada, que "), inlineCode("datos/resumen.txt"), regular(" contiene el resumen sin los avisos, que al renombrar "), inlineCode("gastos.txt"), regular(" aparece el aviso en lugar de una excepción y que las pruebas pasan.")),
                        ),
                ),
            ),
    )
