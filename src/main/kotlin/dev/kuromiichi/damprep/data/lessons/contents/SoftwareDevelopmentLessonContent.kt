package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.codeLine
import dev.kuromiichi.damprep.data.lessons.function
import dev.kuromiichi.damprep.data.lessons.keyword
import dev.kuromiichi.damprep.data.lessons.number
import dev.kuromiichi.damprep.data.lessons.operator
import dev.kuromiichi.damprep.data.lessons.plain
import dev.kuromiichi.damprep.data.lessons.punctuation
import dev.kuromiichi.damprep.data.lessons.string

val softwareDevelopmentLessonContent = LessonContent(
    lessonId = LessonCatalog.lessons[1].id,
    steps = listOf(
        LessonStep(
            title = "Software: de una idea a algo que funciona",
            body = "El software no aparece cuando alguien escribe la primera línea de código. Empieza antes: cuando hay una necesidad, una idea o un problema que queremos resolver con ayuda de un ordenador. Puede ser una app de móvil, una web, un videojuego, un cajero automático o un programa pequeño que ordena archivos. Lo importante es que hay una intención: queremos que la máquina haga algo útil, de forma repetible y con unas reglas claras.",
            visual = LessonVisualContent.IconText(
                title = "Camino",
                body = "Primero aparece una necesidad. Después la entendemos, pensamos una solución y la convertimos en algo que se pueda ejecutar.",
            ),
        ),
        LessonStep(
            title = "Problemas, algoritmos y programas",
            body = "Antes de programar conviene saber qué queremos conseguir. Un algoritmo es una secuencia ordenada de pasos para resolver un problema, y no tiene por qué empezar en Kotlin ni en ningún lenguaje concreto. Puedes pensar un algoritmo para preparar una receta, cruzar una calle o calcular el precio final de una compra. Un programa aparece cuando expresamos esos pasos en un lenguaje que el ordenador puede traducir y ejecutar.",
            visual = LessonVisualContent.Flow(
                nodes = listOf("Problema", "Algoritmo", "Programa"),
            ),
        ),
        LessonStep(
            title = "Hay varias formas de programar",
            body = "No todos los lenguajes ni todos los programas se organizan igual. A esas formas de plantear soluciones las llamamos paradigmas: programación estructurada, modular, orientada a objetos, funcional y otras. Ahora no necesitas aprenderlas todas. Quédate con la idea de que un lenguaje no solo aporta palabras: también propone una forma de ordenar el pensamiento.",
            visual = LessonVisualContent.IconText(
                title = "Paradigmas",
                body = "Distintas reglas para expresar una solución. Más adelante veremos cuáles usa Kotlin y cuándo nos conviene cada una.",
            ),
        ),
        LessonStep(
            title = "El ciclo básico de desarrollo",
            body = "Para construir software con cabeza solemos pasar por varias fases: planificar, analizar, diseñar, codificar, probar, desplegar y mantener. No hace falta memorizar esos nombres ahora, pero sí entender la idea: no se trata de lanzarse a escribir código a ciegas. Primero aclaramos qué hay que hacer, luego pensamos cómo hacerlo, después lo construimos y finalmente comprobamos si realmente funciona.",
            visual = LessonVisualContent.Cycle(
                steps = listOf(
                    "Planificar",
                    "Analizar",
                    "Diseñar",
                    "Codificar",
                    "Probar",
                    "Mantener",
                ),
            ),
        ),
        LessonStep(
            title = "Errores: reglas y significado",
            body = "Al programar aparecerán errores, y eso no significa que estés haciendo algo mal: significa que estás trabajando con un sistema preciso. Algunos errores son sintácticos: el código no respeta las reglas del lenguaje, como una frase con palabras mal colocadas. Otros son semánticos: el código se puede ejecutar, pero la solución no hace lo que queríamos. Aprender a distinguirlos te ahorrará mucha frustración.",
            visual = LessonVisualContent.CodeExamples(
                examples = listOf(
                    LessonCodeExample(
                        title = "Error sintáctico",
                        lines = listOf(
                            codeLine(
                                function("println"),
                                punctuation("("),
                                string("\"Hola", hasErrorUnderline = true),
                            ),
                        ),
                        note = "Faltan comillas y paréntesis de cierre: la frase queda incompleta.",
                    ),
                    LessonCodeExample(
                        title = "Error semántico",
                        lines = listOf(
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
                        note = "Compila, pero permite entrar justo a quien no debería.",
                    ),
                ),
            ),
        ),
        LessonStep(
            title = "Lenguajes y traducción",
            body = "Nosotros escribiremos en Kotlin, un lenguaje de alto nivel. Eso significa que está pensado para que las personas podamos leerlo y escribirlo con cierta comodidad. En escritorio, lo habitual es que Kotlin se compile a bytecode, una forma intermedia que la máquina virtual de Java entiende. Después, la JVM se encarga de convertir y ejecutar esas instrucciones en una forma que el ordenador pueda realizar.",
            visual = LessonVisualContent.Flow(
                nodes = listOf(
                    "Kotlin",
                    "Bytecode JVM",
                    "Código máquina",
                ),
            ),
        ),
        LessonStep(
            title = "Construir software es iterar",
            body = "El software casi nunca queda terminado para siempre. Cambia porque encontramos fallos, porque entendemos mejor el problema o porque aparecen nuevas necesidades. Por eso aprenderemos a avanzar en pasos pequeños: escribir algo, probarlo, leer qué ocurre, corregir y mejorar. Esa forma de trabajar es más importante que acertar a la primera, porque en programación rara vez se acierta todo a la primera.",
            visual = LessonVisualContent.IconText(
                title = "Evolución",
                body = "Versión inicial, prueba, feedback, mejora y nueva versión. Programar también es revisar.",
            ),
        ),
    ),
)
