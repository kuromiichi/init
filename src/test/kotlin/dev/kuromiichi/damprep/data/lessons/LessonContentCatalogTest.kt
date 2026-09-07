package dev.kuromiichi.damprep.data.lessons

import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LessonContentCatalogTest {
    @Test
    fun `current lesson content belongs to first catalog lesson`() {
        assertEquals(
            LessonCatalog.lessons.first().id,
            LessonContentCatalog.currentLesson.lessonId,
        )
    }

    @Test
    fun `initial catalog lessons have content`() {
        val lessonIdsWithContent = LessonContentCatalog.lessons.map { it.lessonId }

        assertEquals(LessonCatalog.lessons.take(3).map { it.id }, lessonIdsWithContent)
    }

    @Test
    fun `lesson content can be found by lesson id`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")

        assertNotNull(softwareLesson)
        assertEquals("software-development", softwareLesson.lessonId)
        assertTrue(LessonContentCatalog.hasContent("welcome-roadmap"))
        assertTrue(LessonContentCatalog.hasContent("software-development"))
        assertTrue(LessonContentCatalog.hasContent("development-environment"))
        assertFalse(LessonContentCatalog.hasContent("first-programs"))
    }

    @Test
    fun `current lesson has navigable steps with visual content`() {
        val steps = LessonContentCatalog.currentLesson.steps

        assertEquals(6, steps.size)
        assertTrue(steps.all { it.title.isNotBlank() })
        assertTrue(steps.all { it.body.isNotBlank() })
        assertTrue(steps.all { it.visual.hasReadableContent() })
    }

    @Test
    fun `current lesson introduces kotlin and intellij idea`() {
        val toolStep = LessonContentCatalog.currentLesson.steps[2]

        assertEquals("Usaremos Kotlin e IntelliJ IDEA", toolStep.title)
        assertTrue(toolStep.body.contains("Kotlin"))
        assertTrue(toolStep.body.contains("IntelliJ IDEA"))
        val visual = toolStep.visual
        assertTrue(visual is LessonVisualContent.ToolCards)
        assertEquals(
            listOf(LessonToolLogo.Kotlin, LessonToolLogo.IntellijIdea),
            visual.tools.map { it.logo },
        )
    }

    @Test
    fun `software development lesson introduces the development process`() {
        val softwareLesson = LessonContentCatalog.lessons[1]
        val combinedText = softwareLesson.steps.joinToString(" ") {
            "${it.title} ${it.body} ${it.visual.readableText()}"
        }

        assertEquals("software-development", softwareLesson.lessonId)
        assertEquals(7, softwareLesson.steps.size)
        assertTrue(combinedText.contains("software", ignoreCase = true))
        assertTrue(combinedText.contains("algoritmo", ignoreCase = true))
        assertTrue(combinedText.contains("paradigmas", ignoreCase = true))
        assertTrue(combinedText.contains("sintácticos", ignoreCase = true))
        assertTrue(combinedText.contains("Kotlin"))
        assertTrue(combinedText.contains("bytecode", ignoreCase = true))
        assertTrue(combinedText.contains("JVM"))
        assertTrue(combinedText.contains("traducción", ignoreCase = true))
    }

    @Test
    fun `lesson steps can provide flow visuals`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val flowVisual = softwareLesson?.steps?.map { it.visual }
            ?.filterIsInstance<LessonVisualContent.Flow>()
            ?.firstOrNull()

        assertNotNull(flowVisual)
        assertEquals(listOf("Problema", "Algoritmo", "Programa"), flowVisual.nodes)
    }

    @Test
    fun `translation step explains kotlin to jvm execution flow`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val translationStep = softwareLesson?.steps?.firstOrNull { it.title == "Lenguajes y traducción" }
        val visual = translationStep?.visual

        assertNotNull(translationStep)
        assertTrue(translationStep.body.contains("bytecode", ignoreCase = true))
        assertTrue(translationStep.body.contains("JVM"))
        assertTrue(visual is LessonVisualContent.Flow)
        assertEquals(
            listOf("Kotlin", "Bytecode JVM", "Código máquina"),
            visual.nodes,
        )
    }

    @Test
    fun `development cycle step provides a cycle visual`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val cycleStep = softwareLesson?.steps?.firstOrNull { it.title == "El ciclo básico de desarrollo" }
        val visual = cycleStep?.visual

        assertNotNull(cycleStep)
        assertTrue(visual is LessonVisualContent.Cycle)
        assertEquals(
            listOf("Analizar", "Diseñar", "Codificar", "Probar", "Documentar", "Desplegar", "Mantener"),
            visual.steps,
        )
    }

    @Test
    fun `error explanation step provides syntax and logic code examples`() {
        val softwareLesson = LessonContentCatalog.findByLessonId("software-development")
        val errorStep = softwareLesson?.steps?.firstOrNull { it.title == "Errores: sintaxis y lógica" }
        val visual = errorStep?.visual

        assertNotNull(errorStep)
        assertTrue(visual is LessonVisualContent.CodeExamples)
        assertEquals(listOf("Error sintáctico", "Error lógico"), visual.examples.map { it.title })
        val syntaxExampleText = visual.examples.first().lines.joinToString("\n") { line ->
            line.tokens.joinToString("") { token -> token.text }
        }
        assertTrue(syntaxExampleText.contains("println"))
        assertFalse(syntaxExampleText.contains("val"))
        assertTrue(visual.examples.all { example ->
            example.lines.any { line -> line.tokens.any { token -> token.hasErrorUnderline } }
        })
    }

    @Test
    fun `development environment lesson prepares ide and jdk setup`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val combinedText = environmentLesson?.steps?.joinToString(" ") {
            it.allReadableText()
        }.orEmpty()

        assertNotNull(environmentLesson)
        assertEquals(13, environmentLesson.steps.size)
        assertTrue(combinedText.contains("IDE"))
        assertTrue(combinedText.contains("IntelliJ IDEA"))
        assertTrue(combinedText.contains("Kotlin"))
        assertTrue(combinedText.contains("JDK"))
        assertTrue(combinedText.contains("JRE"))
        assertTrue(combinedText.contains("JVM"))
        assertTrue(combinedText.contains("Gradle"))
        assertTrue(combinedText.contains("Git"))
        assertTrue(combinedText.contains("depur", ignoreCase = true))
        assertTrue(combinedText.contains("plugins", ignoreCase = true))
        assertTrue(combinedText.contains("Instalar el JDK 25"))
    }

    @Test
    fun `development environment first step focuses on initial tools without gradle`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val firstStep = environmentLesson?.steps?.first()
        val visual = firstStep?.visual

        assertNotNull(firstStep)
        assertFalse(firstStep.body.contains("Gradle"))
        assertTrue(visual is LessonVisualContent.ToolCards)
        assertEquals(
            listOf(
                LessonToolLogo.IntellijIdea,
                LessonToolLogo.Kotlin,
                LessonToolLogo.Jdk,
                LessonToolLogo.Git,
            ),
            visual.tools.map { it.logo },
        )
    }

    @Test
    fun `development environment lesson requires running the generated hello world`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val helloWorldStep = environmentLesson?.steps
            ?.firstOrNull { it.title == "Comprobar el entorno con Hello World" }

        assertNotNull(helloWorldStep)
        assertEquals(helloWorldStep, environmentLesson.steps.last())
        assertFalse(helloWorldStep.isOperatingSystemSensitive)
        assertTrue(helloWorldStep.body.contains("Main.kt"))
        assertTrue(helloWorldStep.body.contains("MainKt"))
        assertTrue(helloWorldStep.body.contains("Hello World!"))
        assertTrue(helloWorldStep.checkpoint?.prompt?.contains("Hello World!") == true)

        val visual = helloWorldStep.visual
        assertTrue(visual is LessonVisualContent.CodeExamples)
        val sourceCode = visual.examples.flatMap(LessonCodeExample::lines).joinToString("\n") { line ->
            line.tokens.joinToString("") { token -> token.text }
        }
        assertTrue(sourceCode.contains("fun main()"))
        assertTrue(sourceCode.contains("println(\"Hello World!\")"))
    }

    @Test
    fun `development environment lesson provides official installation links`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val installationSteps = environmentLesson?.steps
            ?.filter { it.title.firstOrNull()?.isDigit() == true }
            .orEmpty()

        assertEquals(4, installationSteps.size)
        TargetOperatingSystem.entries.forEach { operatingSystem ->
            val links = installationSteps.flatMap { step -> step.contentFor(operatingSystem).links }

            assertEquals(8, links.size)
            assertTrue(links.all { it.label.isNotBlank() })
            assertTrue(links.all { it.url.startsWith("https://") })
            assertTrue(links.any { it.url.contains("adoptium.net") })
            assertTrue(links.any { it.url.contains("jetbrains.com") })
            assertTrue(links.any { it.url.contains("git-scm.com") })
            assertTrue(links.any { it.url.contains("kotlinlang.org") })
            assertTrue(links.any { it.url.contains("docs.gradle.org") })
        }
    }

    @Test
    fun `development environment marks operating system sensitive steps`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val sensitiveStepTitles = environmentLesson?.steps
            ?.filter(LessonStep::isOperatingSystemSensitive)
            ?.map(LessonStep::title)

        assertEquals(
            listOf(
                "1. Instalar el JDK 25 para DAM Prep",
                "2. Instalar IntelliJ IDEA",
                "3. Instalar Git",
            ),
            sensitiveStepTitles,
        )
    }

    @Test
    fun `every sensitive environment step provides complete content for every operating system`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val sensitiveSteps = environmentLesson?.steps
            ?.filter(LessonStep::isOperatingSystemSensitive)
            .orEmpty()

        assertTrue(sensitiveSteps.isNotEmpty())
        sensitiveSteps.forEach { step ->
            assertEquals(TargetOperatingSystem.entries.toSet(), step.operatingSystemContent.keys)
            TargetOperatingSystem.entries.forEach { operatingSystem ->
                val content = step.contentFor(operatingSystem)

                assertTrue(content.body.isNotBlank())
                assertTrue(content.visual.hasReadableContent())
                assertTrue(content.links.isNotEmpty())
                assertTrue(content.explanation.size >= 2)
            }
        }
    }

    @Test
    fun `platform instructions combine paragraphs steps callouts and inline code blocks`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val platformBlocks = environmentLesson?.steps
            ?.flatMap { step -> step.operatingSystemContent.values }
            ?.flatMap(LessonStepContent::explanation)
            .orEmpty()

        assertTrue(platformBlocks.any { block -> block is LessonExplanationBlock.Paragraph })
        assertTrue(platformBlocks.any { block -> block is LessonExplanationBlock.Steps })
        assertTrue(platformBlocks.any { block -> block is LessonExplanationBlock.Callout })
        assertTrue(platformBlocks.any { block -> block is LessonExplanationBlock.Code })
    }

    @Test
    fun `platform variants apply the same semantic text styles`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val sensitiveSteps = environmentLesson?.steps
            ?.filter(LessonStep::isOperatingSystemSensitive)
            .orEmpty()

        sensitiveSteps.forEach { step ->
            TargetOperatingSystem.entries.forEach { operatingSystem ->
                val paragraph = step.contentFor(operatingSystem).explanation
                    .filterIsInstance<LessonExplanationBlock.Paragraph>()
                    .first()
                val styles = paragraph.text.spans.map(LessonTextSpan::style).toSet()

                assertTrue(LessonTextSpanStyle.Highlight in styles)
                if (step.title.startsWith("1.") || step.title.startsWith("2.")) {
                    assertTrue(LessonTextSpanStyle.InlineCode in styles)
                }
                if (step.title.startsWith("1.")) {
                    assertTrue(LessonTextSpanStyle.Emphasis in styles)
                }
            }
        }
    }

    @Test
    fun `long lesson explanations are divided into readable blocks`() {
        val allContent = LessonContentCatalog.lessons.flatMap { lesson ->
            lesson.steps.flatMap { step ->
                listOf(
                    LessonStepContent(
                        body = step.body,
                        visual = step.visual,
                        links = step.links,
                        explanation = step.explanation,
                    ),
                ) + step.operatingSystemContent.values
            }
        }

        assertTrue(allContent.all { content ->
            content.body.length < 240 || content.explanation.size > 1
        })
        assertTrue(allContent.flatMap(LessonStepContent::explanation).all { block ->
            block.hasReadableContent()
        })
    }

    @Test
    fun `selector resolves distinct installation instructions for each operating system`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val jdkStep = environmentLesson?.steps
            ?.firstOrNull { it.title == "1. Instalar el JDK 25 para DAM Prep" }

        assertNotNull(jdkStep)
        val windowsContent = jdkStep.contentFor(TargetOperatingSystem.Windows)
        val macOsContent = jdkStep.contentFor(TargetOperatingSystem.MacOS)
        val linuxContent = jdkStep.contentFor(TargetOperatingSystem.Linux)

        assertTrue(windowsContent.body.contains("MSI"))
        assertTrue(windowsContent.body.contains("JAVA_HOME"))
        assertTrue(macOsContent.body.contains("PKG"))
        assertTrue(macOsContent.body.contains("Apple Silicon"))
        assertTrue(linuxContent.body.contains("temurin-25-jdk"))
        assertTrue(linuxContent.body.contains("distribución"))
        assertEquals(3, setOf(windowsContent.body, macOsContent.body, linuxContent.body).size)
    }

    @Test
    fun `development environment lesson shows ide tooling as flows and cards`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val visuals = environmentLesson?.steps?.map { it.visual }.orEmpty()

        assertTrue(visuals.any { visual ->
            visual is LessonVisualContent.ToolCards &&
                visual.tools.map { it.logo } == listOf(LessonToolLogo.Kotlin, LessonToolLogo.IntellijIdea)
        })
        assertTrue(visuals.any { visual ->
            visual is LessonVisualContent.Flow && visual.nodes == listOf(
                "New Project",
                "Kotlin",
                "Gradle",
                "JDK 25",
                "Gradle sync",
            )
        })
    }

    @Test
    fun `development environment ide step uses an ide screenshot`() {
        val environmentLesson = LessonContentCatalog.findByLessonId("development-environment")
        val ideStep = environmentLesson?.steps?.firstOrNull { it.title == "IDE: la mesa de trabajo" }
        val visual = ideStep?.visual

        assertNotNull(ideStep)
        assertTrue(ideStep.body.contains("entorno de desarrollo integrado", ignoreCase = true))
        assertTrue(visual is LessonVisualContent.Screenshot)
        assertEquals(LessonScreenshot.IntellijIdeWorkspace, visual.image)
        assertTrue(visual.description.contains("IntelliJ IDEA"))
        assertTrue(visual.description.contains("Git"))
    }

}

private fun LessonVisualContent.hasReadableContent(): Boolean =
    readableText().isNotBlank()

private fun LessonExplanationBlock.hasReadableContent(): Boolean = when (this) {
    is LessonExplanationBlock.Callout -> title.isNotBlank() && text.plainText.isNotBlank()
    is LessonExplanationBlock.Code -> title.isNotBlank() && lines.isNotEmpty() && lines.all(String::isNotBlank)
    is LessonExplanationBlock.Paragraph -> text.plainText.isNotBlank()
    is LessonExplanationBlock.Steps -> items.isNotEmpty() && items.all { item -> item.plainText.isNotBlank() }
}

private fun LessonStep.allReadableText(): String = buildString {
    append(title)
    append(' ')
    append(body)
    append(' ')
    append(visual.readableText())
    operatingSystemContent.values.forEach { content ->
        append(' ')
        append(content.body)
        append(' ')
        append(content.visual.readableText())
    }
}

private fun LessonVisualContent.readableText(): String =
    when (this) {
        is LessonVisualContent.CodeExamples -> examples.joinToString(" ") { example ->
            "${example.title} ${example.note} " +
                example.lines.joinToString(" ") { line -> line.tokens.joinToString("") { token -> token.text } }
        }
        is LessonVisualContent.Cycle -> steps.joinToString(" ")
        is LessonVisualContent.Flow -> nodes.joinToString(" ")
        is LessonVisualContent.IconText -> "$title $body"
        is LessonVisualContent.Screenshot -> description
        is LessonVisualContent.ToolCards -> tools.joinToString(" ") { "${it.name} ${it.kind}" }
    }
