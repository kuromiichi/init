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
                body = "Esta app está pensada como una tutoría guiada. Vas a avanzar por pantallas breves, con ideas pequeñas y conectadas entre sí, hasta construir una forma sólida de pensar como persona desarrolladora.",
                visualTitle = "Ruta",
                visualBody = "Primero entenderás qué estás haciendo, después aprenderás a escribir código y más adelante usarás herramientas profesionales para trabajar con seguridad.",
            ),
            LessonStep(
                title = "Programar no es memorizar comandos",
                body = "Programar consiste en explicar una solución con tanta claridad que una máquina pueda ejecutarla. El lenguaje importa, pero la habilidad principal es aprender a descomponer problemas.",
                visualTitle = "Idea central",
                visualBody = "Problema, datos, reglas, pasos y resultado. Casi todo lo que haremos se puede mirar desde esa estructura.",
            ),
            LessonStep(
                title = "El entorno también forma parte del aprendizaje",
                body = "No solo aprenderás Kotlin. También aprenderás a ejecutar proyectos, leer errores, depurar, guardar versiones con Git y comprobar que tu código se comporta como esperas.",
                visualTitle = "Herramientas",
                visualBody = "El editor, el compilador, el depurador y las pruebas serán parte del camino, no un añadido separado.",
            ),
            LessonStep(
                title = "Cómo avanzar",
                body = "Lee cada pantalla con calma. Cuando aparezcan ejercicios en futuras versiones, servirán para desbloquear el siguiente bloque y confirmar que puedes aplicar la idea con tus propias manos.",
                visualTitle = "Progreso",
                visualBody = "Cada lección tendrá pasos internos y una meta concreta. No buscamos correr: buscamos que cada pieza encaje.",
            ),
        ),
    )
}
