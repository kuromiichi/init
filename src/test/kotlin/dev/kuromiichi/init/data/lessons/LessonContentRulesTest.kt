package dev.kuromiichi.init.data.lessons

import dev.kuromiichi.init.data.preferences.TargetOperatingSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Reglas que debe cumplir cualquier lección. Comprueban la estructura y el alcance del contenido,
 * no su redacción, para que reescribir un texto no obligue a tocar las pruebas.
 */
private const val MAX_PARAGRAPH_LENGTH = 320

class LessonContentRulesTest {
    private val lessons = LessonContentCatalog.lessons

    @Test
    fun `every step and platform variant has readable content`() {
        lessons.forEach { lesson ->
            lesson.steps.forEach { step ->
                val where = "${lesson.lessonId} › ${step.title}"
                assertTrue(step.title.isNotBlank(), "Paso sin título en ${lesson.lessonId}")
                step.allContents().forEach { content ->
                    assertTrue(content.explanation.isNotEmpty(), "Paso sin explicación: $where")
                    assertTrue(content.visual.readableText().isNotBlank(), "Visual vacío: $where")
                    assertTrue(content.explanation.all { it.readableText().isNotBlank() }, "Bloque vacío: $where")
                    content.explanation.filterIsInstance<LessonExplanationBlock.Paragraph>().forEach { paragraph ->
                        assertTrue(paragraph.text.plainText.length <= MAX_PARAGRAPH_LENGTH, "Párrafo demasiado largo: $where")
                    }
                }
            }
        }
    }

    @Test
    fun `platform sensitive steps cover every operating system`() {
        lessons.flatMap(LessonContent::steps).filter(LessonStep::isOperatingSystemSensitive).forEach { step ->
            assertEquals(TargetOperatingSystem.entries.toSet(), step.operatingSystemContent.keys, step.title)
            val variants = TargetOperatingSystem.entries.map(step::contentFor)
            assertTrue(variants.all { it.links.isNotEmpty() }, "Variante sin enlaces: ${step.title}")
            // Dos sistemas pueden compartir contenido (macOS y Linux suelen coincidir), pero si todos coinciden sobra el selector.
            assertTrue(variants.toSet().size > 1, "El paso no cambia según el sistema: ${step.title}")
        }
    }

    @Test
    fun `practices are complete and verifiable`() {
        val practices = lessons.flatMap(LessonContent::steps).mapNotNull(LessonStep::practice)

        assertEquals(practices.size, practices.map(LessonPractice::id).toSet().size, "Identificadores de práctica repetidos")
        practices.forEach { practice ->
            assertTrue(practice.objective.plainText.isNotBlank(), practice.id)
            assertTrue(practice.statement.plainText.isNotBlank(), practice.id)
            assertTrue(practice.requirements.isNotEmpty(), practice.id)
            assertTrue(practice.hints.isNotEmpty(), practice.id)
            assertTrue(practice.confirmation.plainText.isNotBlank(), practice.id)
            assertTrue(practice.expectedOutput.all { run -> run.lines.any { !it.isInput } }, "Ejecución sin salida: ${practice.id}")
        }
    }

    @Test
    fun `lessons with a practice end with it`() {
        lessons.filter { lesson -> lesson.steps.any { it.practice != null } }.forEach { lesson ->
            assertEquals(listOf(lesson.steps.last()), lesson.steps.filter { it.practice != null }, lesson.lessonId)
        }
    }

    @Test
    fun `code examples and links are well formed`() {
        lessons.flatMap(LessonContent::steps).flatMap(LessonStep::allContents).forEach { content ->
            content.visual.codeExamples().forEach { example ->
                assertTrue(example.title.isNotBlank())
                assertTrue(example.lines.isNotEmpty(), example.title)
                assertTrue(example.note.plainText.isNotBlank(), example.title)
            }
            content.links.forEach { link ->
                assertTrue(link.label.isNotBlank())
                assertTrue(link.url.startsWith("https://"), link.url)
            }
        }
    }

    @Test
    fun `copyable examples are complete programs`() {
        lessons
            .flatMap(LessonContent::steps)
            .flatMap(LessonStep::allContents)
            .flatMap { it.visual.codeExamples() }
            .filter(LessonCodeExample::isCopyable)
            .forEach { example ->
                assertTrue(example.sourceText.startsWith("fun main() {"), example.title)
                assertTrue(example.sourceText.endsWith("}"), example.title)
            }
    }

    @Test
    fun `trace tables have one value per column`() {
        lessons
            .flatMap(LessonContent::steps)
            .flatMap(LessonStep::allContents)
            .mapNotNull { (it.visual as? LessonVisualContent.Trace)?.table }
            .forEach { table ->
                assertTrue(table.rows.isNotEmpty(), table.title)
                assertTrue(table.rows.all { it.size == table.columns.size }, "Fila incompleta en ${table.title}")
            }
    }

    @Test
    fun `uml diagrams connect existing nodes without overlapping them`() {
        lessons
            .flatMap(LessonContent::steps)
            .flatMap(LessonStep::allContents)
            .mapNotNull { it.visual as? LessonVisualContent.UmlDiagram }
            .forEach { diagram ->
                val ids = diagram.nodes.map(UmlNode::id)
                assertEquals(ids.size, ids.toSet().size, "Nodos repetidos: $ids")
                diagram.relations.forEach { relation ->
                    assertTrue(relation.from in ids && relation.to in ids, "Relación con un nodo que no existe: $relation")
                }
                val occupied = diagram.nodes.flatMap { node -> (0 until node.cell.columnSpan).map { node.cell.row to node.cell.column + it } }
                assertEquals(occupied.size, occupied.toSet().size, "Nodos en la misma celda: $ids")
            }
    }

    @Test
    fun `learners never see curricular traceability`() {
        val forbidden = listOf(Regex("""\bRA\d"""), Regex("(?i)resultados? de aprendizaje"), Regex("(?i)currícul"), Regex("(?i)\\bBOE\\b|boe\\.es"))

        lessons.forEach { lesson ->
            val text = lesson.allVisibleText()
            forbidden.forEach { pattern ->
                assertFalse(pattern.containsMatchIn(text), "${lesson.lessonId} muestra trazabilidad curricular: $pattern")
            }
        }
    }

    @Test
    fun `lessons do not use constructs taught later`() {
        val laterConstructs =
            mapOf(
                "first-programs" to listOf("""\breadln\b""", """\bif \(""", """\bwhen \(""", """\bfor \(""", """\bwhile \("""),
                "input-decisions" to listOf("""\bfor \(""", """\bwhile \(""", """\bfun (?!main\b)""", """\blistOf\b"""),
                "debugging" to listOf("""\bfun (?!main\b)""", """\b(listOf|arrayOf)\b""", """\b(try|catch)\b"""),
                "functions-modularity" to listOf("""\b(listOf|arrayOf|Array)\b""", """\b(try|catch)\b""", """\bclass\b""", """\bvararg\b"""),
                "basic-testing" to listOf("""\b(listOf|arrayOf|Array)\b""", """\b(try|catch)\b""", """\bassertFailsWith\b""", """(?i)\bmock"""),
                "nullability-exceptions" to listOf("""\b(listOf|arrayOf|Array)\b""", """\bclass\b(?! \w+Test)""", """\.(let|also|apply|run)\b""", """\b(lateinit|as\?)"""),
                "arrays-lists" to listOf("""\b(setOf|mapOf|mutableSetOf|mutableMapOf|emptyList)\b""", """\bclass\b(?! \w+Test)""", """\.(filter|map|forEach|any|all|sortedBy|let|also|apply|run)\b""", """\{ it\b""", """\bArray\(\d"""),
                "sets-maps" to listOf("""\bclass\b(?! \w+Test)""", """\.(filter|map|forEach|any|all|count|groupBy|sortedBy|getOrPut|getOrElse|let|also|apply|run)\b""", """\{ it\b""", """\{ \w+ ->""", """\b(TreeSet|TreeMap|HashSet|HashMap|Comparator)\b"""),
                "oop-classes-objects" to listOf("""\b(open|abstract|sealed|interface|protected|inner|lateinit|lazy)\b""", """(?<!companion )\bobject\b""", """\bclass \w+(\([^)]*\))? ?: ?\w""", """\.(filter|map|forEach|any|all|count|let|also|apply|run)\b""", """\{ it\b""", """\{ \w+ ->"""),
                "oop-relationships" to listOf("""\b(open|abstract|sealed|interface|protected|inner|lateinit|lazy)\b""", """(?<!companion )\bobject\b""", """\bclass \w+(\([^)]*\))? ?: ?\w""", """\.(filter|map|forEach|any|all|count|sumOf|let|also|apply|run)\b""", """\{ it\b""", """\{ \w+ ->"""),
                "oop-inheritance-interfaces" to listOf("""\b(lateinit|lazy|inner|by)\b""", """(?<!companion )\bobject\b""", """\b(class|interface) \w+<""", """\bfun <""", """\.(filter|map|forEach|any|all|count|sumOf|let|also|apply|run)\b""", """\{ it\b""", """\{ \w+ ->"""),
                "basic-uml" to listOf("""\b(lateinit|lazy|inner|by)\b""", """(?<!companion )\bobject\b""", """\b(class|interface) \w+<""", """\bfun <""", """\.(filter|map|forEach|any|all|count|sumOf|let|also|apply|run)\b""", """\{ it\b""", """\{ \w+ ->"""),
                "loops" to listOf("""\bfun (?!main\b)""", """\b(listOf|arrayOf|Array)\b""", """\bRandom\b""", """\brepeat\b"""),
            )

        laterConstructs.forEach { (lessonId, patterns) ->
            val source = LessonContentCatalog.findByLessonId(lessonId)?.sourceCode().orEmpty()
            assertTrue(source.isNotBlank(), "Sin código en $lessonId")
            patterns.forEach { pattern ->
                assertFalse(Regex(pattern).containsMatchIn(source), "$lessonId usa $pattern antes de tiempo")
            }
        }
    }
}

private fun LessonStep.allContents(): List<LessonStepContent> = listOf(contentFor(null)) + operatingSystemContent.values

private fun LessonContent.sourceCode(): String =
    steps
        .flatMap(LessonStep::allContents)
        .map(LessonStepContent::visual)
        .flatMap { it.codeExamples() }
        .joinToString("\n", transform = LessonCodeExample::sourceText)

private fun LessonVisualContent.codeExamples(): List<LessonCodeExample> =
    when (this) {
        is LessonVisualContent.CodeExamples -> examples
        is LessonVisualContent.Trace -> listOf(example)
        else -> emptyList()
    }

private fun LessonContent.allVisibleText(): String =
    steps.joinToString(" ") { step ->
        buildList {
            add(step.title)
            step.allContents().forEach { content ->
                add(content.visual.readableText())
                addAll(content.explanation.map(LessonExplanationBlock::readableText))
                addAll(content.links.map(LessonLink::label))
            }
            step.practice?.let { practice ->
                add(practice.objective.plainText)
                add(practice.statement.plainText)
                addAll(practice.requirements.map(LessonRichText::plainText))
                addAll(practice.expectedOutput.flatMap(LessonConsoleRun::lines).map(LessonConsoleLine::text))
                addAll(practice.hints.map(LessonRichText::plainText))
                practice.extension?.plainText?.let(::add)
                add(practice.confirmation.plainText)
            }
        }.joinToString(" ")
    }

private fun LessonExplanationBlock.readableText(): String =
    when (this) {
        is LessonExplanationBlock.Callout -> "$title ${text.plainText}"
        is LessonExplanationBlock.Code -> "$title ${lines.joinToString(" ")} ${note.plainText}"
        is LessonExplanationBlock.Terminal -> "${caption.orEmpty()} ${commands.joinToString(" ")} ${note.plainText}"
        is LessonExplanationBlock.Paragraph -> text.plainText
        is LessonExplanationBlock.Steps -> items.joinToString(" ", transform = LessonRichText::plainText)
    }

private fun LessonVisualContent.readableText(): String =
    when (this) {
        is LessonVisualContent.CodeExamples -> examples.joinToString(" ", transform = LessonCodeExample::readableText)
        is LessonVisualContent.Trace -> "${example.readableText()} ${table.title} ${table.columns.joinToString(" ")}"
        is LessonVisualContent.Console -> "${caption.orEmpty()} " + runs.flatMap(LessonConsoleRun::lines).joinToString(" ", transform = LessonConsoleLine::text)
        is LessonVisualContent.Cycle -> steps.joinToString(" ")
        is LessonVisualContent.Flow -> nodes.joinToString(" ")
        is LessonVisualContent.IconText -> "$title $body"
        is LessonVisualContent.Screenshot -> description
        is LessonVisualContent.ToolCards -> tools.joinToString(" ") { "${it.name} ${it.kind}" }
        is LessonVisualContent.UmlDiagram -> description
    }

private fun LessonCodeExample.readableText(): String = "$title ${note.plainText} $sourceText"
