package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.codeLine
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.explanationCode
import dev.kuromiichi.damprep.data.lessons.function
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.keyword
import dev.kuromiichi.damprep.data.lessons.number
import dev.kuromiichi.damprep.data.lessons.operator
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.paragraphs
import dev.kuromiichi.damprep.data.lessons.plain
import dev.kuromiichi.damprep.data.lessons.punctuation
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.string

val softwareDevelopmentLessonContent =
    LessonContent(
        lessonId = "software-development",
        steps =
            listOf(
                LessonStep(
                    title = "Software: de una idea a algo que funciona",
                    explanation =
                        paragraphs(
                            "El software no aparece cuando alguien escribe la primera línea de código. Empieza antes: cuando hay una necesidad, una idea o un problema que queremos resolver con ayuda de un ordenador.",
                            "Puede ser una app de móvil, una web, un videojuego, un cajero automático o un programa pequeño que ordena archivos.",
                            "Lo importante es la intención: queremos que la máquina haga algo útil, de forma repetible y con unas reglas claras.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Camino",
                            body = "Primero aparece una necesidad. Después la entendemos, pensamos una solución y la convertimos en algo que se pueda ejecutar.",
                        ),
                ),
                LessonStep(
                    title = "Problemas, algoritmos y programas",
                    explanation =
                        paragraphs(
                            "Antes de programar conviene saber qué queremos conseguir. Un algoritmo es una secuencia ordenada de pasos para resolver un problema, y no tiene por qué empezar en Kotlin ni en ningún lenguaje concreto.",
                            "Puedes pensar un algoritmo para preparar una receta, cruzar una calle o calcular el precio final de una compra.",
                            "Un programa aparece cuando expresamos esos pasos en un lenguaje que el ordenador puede traducir y ejecutar.",
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes = listOf("Problema", "Algoritmo", "Programa"),
                        ),
                ),
                LessonStep(
                    title = "Cómo es un buen algoritmo",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Un algoritmo útil es "),
                                emphasis("preciso"),
                                regular(", porque indica el orden exacto de los pasos; "),
                                emphasis("definido"),
                                regular(", porque con los mismos datos da siempre el mismo resultado; y "),
                                emphasis("finito"),
                                regular(", porque termina en algún momento."),
                            ),
                            paragraph(
                                regular("Antes de programar, podemos escribirlo en "),
                                highlight("pseudocódigo"),
                                regular(", un lenguaje cercano al nuestro, o dibujarlo como "),
                                highlight("diagrama de flujo"),
                                regular(". Ninguno depende de un lenguaje de programación concreto."),
                            ),
                            explanationCode(
                                "Pseudocódigo: aplicar un descuento",
                                richText("El diagrama de esta pantalla representa los mismos pasos."),
                                "Inicio",
                                "  Leer precio",
                                "  Si precio > 50 entonces",
                                "    precio ← precio × 0,9",
                                "  Fin si",
                                "  Mostrar precio",
                                "Fin",
                            ),
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes = listOf("Inicio", "Leer precio", "¿Más de 50 €?", "Aplicar descuento", "Mostrar precio", "Fin"),
                        ),
                ),
                LessonStep(
                    title = "Hay varias formas de programar",
                    explanation =
                        paragraphs(
                            "No todos los lenguajes ni todos los programas se organizan igual. A esas formas de plantear soluciones las llamamos paradigmas: programación estructurada, modular, orientada a objetos, funcional y otras.",
                            "Ahora no necesitas aprenderlas todas. Quédate con la idea de que un lenguaje no solo aporta palabras: también propone una forma de ordenar el pensamiento.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Paradigmas",
                            body = "Distintas reglas para expresar una solución. Más adelante veremos cuáles usa Kotlin y cuándo nos conviene cada una.",
                        ),
                ),
                LessonStep(
                    title = "El ciclo básico de desarrollo",
                    explanation =
                        paragraphs(
                            "Para construir software con cabeza solemos pasar por varias fases: analizar, diseñar, codificar, probar, documentar, desplegar y mantener.",
                            "No hace falta memorizar esos nombres ahora. Lo importante es no lanzarse a escribir código a ciegas.",
                            "Primero aclaramos qué hay que hacer, luego pensamos cómo hacerlo, después lo construimos y finalmente comprobamos si funciona.",
                        ),
                    visual =
                        LessonVisualContent.Cycle(
                            steps =
                                listOf(
                                    "Analizar",
                                    "Diseñar",
                                    "Codificar",
                                    "Probar",
                                    "Documentar",
                                    "Desplegar",
                                    "Mantener",
                                ),
                        ),
                ),
                LessonStep(
                    title = "Errores: sintaxis, ejecución y lógica",
                    explanation =
                        listOf(
                            paragraph("Al programar aparecerán errores. No significa que estés haciendo algo mal: estás trabajando con un sistema preciso. Aprender a distinguirlos te ahorrará mucha frustración."),
                            paragraph(
                                regular("Los errores "),
                                emphasis("sintácticos"),
                                regular(" aparecen cuando el código no respeta las reglas del lenguaje, como una frase con palabras mal colocadas. El programa ni siquiera llega a compilarse."),
                            ),
                            paragraph(
                                regular("Los errores "),
                                emphasis("de ejecución"),
                                regular(" aparecen con el programa en marcha: el código compila, pero una instrucción no puede completarse y el programa se detiene."),
                            ),
                            paragraph(
                                regular("Los errores "),
                                emphasis("lógicos"),
                                regular(" permiten ejecutar el programa hasta el final, pero producen un resultado distinto del esperado."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            examples =
                                listOf(
                                    LessonCodeExample(
                                        title = "Error sintáctico",
                                        lines =
                                            listOf(
                                                codeLine(
                                                    function("println"),
                                                    punctuation("("),
                                                    string("\"Hola", hasErrorUnderline = true),
                                                ),
                                            ),
                                        note = richText(regular("Faltan comillas y paréntesis de cierre: la frase queda incompleta.")),
                                    ),
                                    LessonCodeExample(
                                        title = "Error de ejecución",
                                        lines =
                                            listOf(
                                                codeLine(
                                                    keyword("val"),
                                                    plain(" minutos "),
                                                    operator("="),
                                                    plain(" "),
                                                    string("\"hola\"", hasErrorUnderline = true),
                                                    plain(".toInt()", hasErrorUnderline = true),
                                                ),
                                            ),
                                        note = richText(regular("Compila, pero al ejecutarse se detiene: "), inlineCode("hola"), regular(" no se puede convertir en número.")),
                                    ),
                                    LessonCodeExample(
                                        title = "Error lógico",
                                        lines =
                                            listOf(
                                                codeLine(
                                                    keyword("val"),
                                                    plain(" edad "),
                                                    operator("="),
                                                    plain(" "),
                                                    number("16"),
                                                ),
                                                codeLine(
                                                    keyword("val"),
                                                    plain(" puedeEntrar "),
                                                    operator("="),
                                                    plain(" "),
                                                    plain("edad", hasErrorUnderline = true),
                                                    plain(" ", hasErrorUnderline = true),
                                                    operator("<", hasErrorUnderline = true),
                                                    plain(" ", hasErrorUnderline = true),
                                                    number("18", hasErrorUnderline = true),
                                                ),
                                            ),
                                        note = richText(regular("Compila, pero permite entrar justo a quien no debería.")),
                                    ),
                                ),
                        ),
                ),
                LessonStep(
                    title = "Niveles de lenguaje y traductores",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Los lenguajes de "),
                                emphasis("bajo nivel"),
                                regular(", como el código máquina o el ensamblador, están muy cerca del procesador. Los de "),
                                emphasis("alto nivel"),
                                regular(", como Kotlin, Java o Python, se parecen más a nuestra forma de expresarnos."),
                            ),
                            paragraph(
                                regular("El procesador solo ejecuta código máquina, así que hace falta un traductor. Un "),
                                highlight("compilador"),
                                regular(" traduce todo el código fuente antes de ejecutarlo y genera un resultado que se puede ejecutar después."),
                            ),
                            paragraph(
                                regular("Un "),
                                highlight("intérprete"),
                                regular(" lee y ejecuta el programa instrucción a instrucción, sin generar antes un ejecutable. Algunos lenguajes combinan ambas ideas."),
                            ),
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Compilar o interpretar",
                            body = "El compilador traduce todo antes de ejecutar; el intérprete traduce y ejecuta sobre la marcha.",
                        ),
                ),
                LessonStep(
                    title = "Kotlin: compilado y ejecutado en la JVM",
                    explanation =
                        listOf(
                            paragraph(
                                regular("Kotlin para escritorio sigue un modelo mixto. El compilador traduce tu código fuente, escrito en archivos "),
                                inlineCode(".kt"),
                                regular(", a bytecode: un código intermedio que se guarda en archivos "),
                                inlineCode(".class"),
                                regular("."),
                            ),
                            paragraph("La máquina virtual de Java, la JVM, ejecuta ese bytecode: lo interpreta y, mientras el programa funciona, compila a código máquina las partes que más se usan."),
                            paragraph("Por eso el mismo bytecode funciona en Windows, macOS o Linux siempre que haya una JVM instalada."),
                        ),
                    visual =
                        LessonVisualContent.Flow(
                            nodes =
                                listOf(
                                    "Código fuente .kt",
                                    "Compilador de Kotlin",
                                    "Bytecode .class",
                                    "JVM",
                                    "Código máquina",
                                ),
                        ),
                ),
                LessonStep(
                    title = "Construir software es iterar",
                    explanation =
                        paragraphs(
                            "El software casi nunca queda terminado para siempre. Cambia porque encontramos fallos, entendemos mejor el problema o aparecen nuevas necesidades.",
                            "Hay distintos modelos de ciclo de vida. En el modelo en cascada, cada fase termina antes de empezar la siguiente; en los modelos iterativos y en las metodologías ágiles, el ciclo se repite en versiones pequeñas que se revisan y mejoran.",
                            "Por eso avanzaremos en pasos pequeños: escribir algo, probarlo, leer qué ocurre, corregir y mejorar.",
                            "Esa forma de trabajar es más importante que acertar a la primera.",
                        ),
                    visual =
                        LessonVisualContent.IconText(
                            title = "Evolución",
                            body = "Versión inicial, prueba, feedback, mejora y nueva versión. Programar también es revisar.",
                        ),
                ),
            ),
    )
