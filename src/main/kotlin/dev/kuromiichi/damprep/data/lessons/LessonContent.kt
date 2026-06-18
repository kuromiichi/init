package dev.kuromiichi.damprep.data.lessons

data class LessonStep(
    val title: String,
    val body: String,
    val visualTitle: String,
    val visualBody: String,
)

data class LessonContent(
    val lessonId: String,
    val steps: List<LessonStep>,
)

object LessonContentCatalog {
    val currentLesson = LessonContent(
        lessonId = LessonCatalog.lessons.first().id,
        steps = listOf(
            LessonStep(
                title = "Bienvenida y mapa del camino",
                body = "Esta app está pensada como una tutoría guiada. Irás avanzando poco a poco, con pantallas breves y una idea importante cada vez.",
                visualTitle = "Ruta",
                visualBody = "Primero entenderás el camino, después escribirás código y más adelante usarás herramientas profesionales.",
            ),
            LessonStep(
                title = "No necesitas saber programar todavía",
                body = "Empezamos desde cero. No pasa nada si aún no sabes qué es una variable, un proyecto o un error de compilación. Lo iremos construyendo paso a paso.",
                visualTitle = "Punto de partida",
                visualBody = "La idea no es correr, sino entender qué estás haciendo y por qué.",
            ),
            LessonStep(
                title = "Usaremos Kotlin e IntelliJ IDEA",
                body = "Kotlin será el lenguaje principal: la forma en la que escribiremos instrucciones para el ordenador. IntelliJ IDEA será el entorno donde escribiremos, ejecutaremos y revisaremos esos programas.",
                visualTitle = "Kotlin e IntelliJ IDEA",
                visualBody = "Lenguaje y entorno de desarrollo.",
            ),
            LessonStep(
                title = "Programar es aprender a pensar con orden",
                body = "Programar no va de memorizar comandos. Va de observar un problema, dividirlo en partes pequeñas y explicar una solución con claridad.",
                visualTitle = "Forma de pensar",
                visualBody = "Problema, datos, pasos y resultado. Esa estructura aparecerá una y otra vez.",
            ),
            LessonStep(
                title = "Las herramientas también cuentan",
                body = "También aprenderás a trabajar con un entorno real: ejecutar proyectos, leer errores, depurar, guardar versiones y comprobar que tu código hace lo esperado.",
                visualTitle = "Herramientas",
                visualBody = "El editor, el compilador, Git y las pruebas serán parte natural del aprendizaje.",
            ),
            LessonStep(
                title = "Cómo avanzar",
                body = "Lee cada pantalla con calma. Más adelante, cada tema tendrá actividades para confirmar que puedes aplicar lo aprendido antes de continuar.",
                visualTitle = "Progreso",
                visualBody = "Cada paso debe dejarte un poco más situado que el anterior.",
            ),
        ),
    )
}
