package dev.kuromiichi.damprep.data.lessons

import dev.kuromiichi.damprep.data.lessons.contents.arraysListsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.basicTestingLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.basicUmlLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.debuggingLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.developmentEnvironmentLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.firstProgramsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.functionsModularityLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.gitVersionControlLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.inputDecisionsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.loopsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.nullabilityExceptionsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.oopClassesObjectsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.oopInheritanceInterfacesLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.oopRelationshipsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.setsMapsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.softwareDevelopmentLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.terminalFileSystemLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.welcomeRoadmapLessonContent
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem

data class LessonStep(
    val title: String,
    val explanation: List<LessonExplanationBlock>,
    val visual: LessonVisualContent,
    val links: List<LessonLink> = emptyList(),
    val operatingSystemContent: Map<TargetOperatingSystem, LessonStepContent> = emptyMap(),
    val practice: LessonPractice? = null,
) {
    val isOperatingSystemSensitive: Boolean
        get() = operatingSystemContent.isNotEmpty()

    fun contentFor(targetOperatingSystem: TargetOperatingSystem?): LessonStepContent =
        targetOperatingSystem
            ?.let(operatingSystemContent::get)
            ?: LessonStepContent(
                visual = visual,
                links = links,
                explanation = explanation,
            )
}

/**
 * Paso cuyo contenido cambia según el sistema operativo (comandos, rutas, archivos del sistema).
 * El contenido con `null` se muestra mientras no hay sistema elegido; el selector aparece solo en estos pasos.
 */
internal fun operatingSystemStep(
    title: String,
    content: (TargetOperatingSystem?) -> LessonStepContent,
): LessonStep {
    val base = content(null)
    return LessonStep(
        title = title,
        explanation = base.explanation,
        visual = base.visual,
        links = base.links,
        operatingSystemContent = TargetOperatingSystem.entries.associateWith(content),
    )
}

data class LessonPractice(
    val id: String,
    val objective: LessonRichText,
    val statement: LessonRichText,
    val requirements: List<LessonRichText>,
    val expectedOutput: List<LessonConsoleRun> = emptyList(),
    val expectedOutputKind: LessonConsoleKind = LessonConsoleKind.IntelliJRun,
    val hints: List<LessonRichText> = emptyList(),
    val extension: LessonRichText? = null,
    val confirmation: LessonRichText,
)

/** Dónde se vería una salida: en la terminal del sistema o en la ventana Run de IntelliJ IDEA. */
enum class LessonConsoleKind {
    Terminal,
    IntelliJRun,
}

/** Una ejecución del programa tal como se vería en la consola. */
data class LessonConsoleRun(
    val lines: List<LessonConsoleLine>,
)

data class LessonConsoleLine(
    val text: String,
    val isInput: Boolean = false,
    val isError: Boolean = false,
)

data class LessonStepContent(
    val explanation: List<LessonExplanationBlock>,
    val visual: LessonVisualContent,
    val links: List<LessonLink> = emptyList(),
)

sealed interface LessonExplanationBlock {
    data class Paragraph(
        val text: LessonRichText,
    ) : LessonExplanationBlock

    data class Steps(
        val items: List<LessonRichText>,
    ) : LessonExplanationBlock

    data class Callout(
        val title: String,
        val text: LessonRichText,
        val tone: LessonCalloutTone = LessonCalloutTone.Information,
    ) : LessonExplanationBlock

    /** Código o texto de un archivo; el título es el nombre que lleva la pestaña del editor. */
    data class Code(
        val title: String,
        val lines: List<String>,
        val note: LessonRichText,
    ) : LessonExplanationBlock

    /** Comandos para pegar en la terminal. Las líneas que empiezan por `#` son comentarios. */
    data class Terminal(
        val commands: List<String>,
        val note: LessonRichText,
        val caption: String? = null,
    ) : LessonExplanationBlock
}

data class LessonRichText(
    val spans: List<LessonTextSpan>,
) {
    val plainText: String
        get() = spans.joinToString(separator = "", transform = LessonTextSpan::text)
}

data class LessonTextSpan(
    val text: String,
    val style: LessonTextSpanStyle = LessonTextSpanStyle.Regular,
)

enum class LessonTextSpanStyle {
    Regular,
    Emphasis,
    Highlight,
    InlineCode,
}

enum class LessonCalloutTone {
    Information,
    Tip,
    Warning,
}

data class LessonLink(
    val label: String,
    val url: String,
)

sealed interface LessonVisualContent {
    data class IconText(
        val title: String,
        val body: String,
    ) : LessonVisualContent

    data class ToolCards(
        val tools: List<LessonTool>,
    ) : LessonVisualContent

    data class Flow(
        val nodes: List<String>,
    ) : LessonVisualContent

    data class Screenshot(
        val image: LessonScreenshot,
        val description: String,
    ) : LessonVisualContent

    data class Cycle(
        val steps: List<String>,
    ) : LessonVisualContent

    data class CodeExamples(
        val examples: List<LessonCodeExample>,
    ) : LessonVisualContent

    /** Una o varias ejecuciones tal como se verían en la consola, incluidos los mensajes de error. */
    data class Console(
        val kind: LessonConsoleKind,
        val runs: List<LessonConsoleRun>,
        val caption: String? = null,
    ) : LessonVisualContent

    /** Un ejemplo de código junto a la tabla que sigue sus variables vuelta a vuelta. */
    data class Trace(
        val example: LessonCodeExample,
        val table: LessonTraceTable,
    ) : LessonVisualContent

    /** Diagrama UML: cajas colocadas en una rejilla y unidas por relaciones. */
    data class UmlDiagram(
        val nodes: List<UmlNode>,
        val relations: List<UmlRelation> = emptyList(),
        val caption: String? = null,
    ) : LessonVisualContent
}

data class LessonTraceTable(
    val title: String,
    val columns: List<String>,
    val rows: List<List<String>>,
)

data class LessonCodeExample(
    val title: String,
    val lines: List<LessonCodeLine>,
    val note: LessonRichText,
    /** Nombre del archivo que muestra la pestaña del editor. */
    val fileName: String = DEFAULT_CODE_FILE_NAME,
    /** Muestra un botón para copiar el código, pensado para programas que el alumno debe pegar en su proyecto. */
    val isCopyable: Boolean = false,
) {
    val sourceText: String
        get() = lines.joinToString("\n") { line -> line.tokens.joinToString("", transform = LessonCodeToken::text) }
}

internal const val DEFAULT_CODE_FILE_NAME = "Main.kt"

data class LessonCodeLine(
    val tokens: List<LessonCodeToken>,
)

data class LessonCodeToken(
    val text: String,
    val kind: LessonCodeTokenKind = LessonCodeTokenKind.Plain,
    val hasErrorUnderline: Boolean = false,
)

enum class LessonCodeTokenKind {
    Plain,
    Keyword,
    Function,
    Number,
    Operator,
    Punctuation,
    StringLiteral,
    Interpolation,
    Comment,
    ClassName,
    Parameter,
    Constant,
    MutableVariable,
    LambdaDelimiter,
    Bracket0,
    Bracket1,
    Bracket2,
    Bracket3,
    Bracket4,
}

data class LessonTool(
    val name: String,
    val kind: String,
    val logo: LessonToolLogo,
)

enum class LessonToolLogo {
    Kotlin,
    IntellijIdea,
    Jdk,
    Git,
}

enum class LessonScreenshot {
    IntellijIdeWorkspace,
    IntellijDebugBreakpoint,
    IntellijDebuggerPaused,
    IntellijDebuggerPanel,
    IntellijGitCommit,
    IntellijGitBranches,
    IntellijGitLog,
    IntellijGitConflicts,
    IntellijTestResults,
    IntellijMermaidClass,
}

data class LessonContent(
    val lessonId: String,
    val steps: List<LessonStep>,
)

object LessonContentCatalog {
    val lessons =
        listOf(
            welcomeRoadmapLessonContent,
            softwareDevelopmentLessonContent,
            developmentEnvironmentLessonContent,
            firstProgramsLessonContent,
            inputDecisionsLessonContent,
            loopsLessonContent,
            debuggingLessonContent,
            functionsModularityLessonContent,
            terminalFileSystemLessonContent,
            gitVersionControlLessonContent,
            basicTestingLessonContent,
            nullabilityExceptionsLessonContent,
            arraysListsLessonContent,
            setsMapsLessonContent,
            oopClassesObjectsLessonContent,
            oopRelationshipsLessonContent,
            oopInheritanceInterfacesLessonContent,
            basicUmlLessonContent,
        )

    fun findByLessonId(lessonId: String): LessonContent? = lessons.firstOrNull { it.lessonId == lessonId }

    fun hasContent(lessonId: String): Boolean = findByLessonId(lessonId) != null
}

internal fun consoleRun(vararg lines: LessonConsoleLine): LessonConsoleRun = LessonConsoleRun(lines.toList())

internal fun consoleInput(text: String): LessonConsoleLine = LessonConsoleLine(text = text, isInput = true)

internal fun consoleOutput(text: String): LessonConsoleLine = LessonConsoleLine(text = text)

internal fun consoleError(text: String): LessonConsoleLine = LessonConsoleLine(text = text, isError = true)

internal fun codeLine(vararg tokens: LessonCodeToken): LessonCodeLine = LessonCodeLine(tokens = tokens.toList())

internal fun plain(
    text: String,
    hasErrorUnderline: Boolean = false,
): LessonCodeToken = LessonCodeToken(text = text, hasErrorUnderline = hasErrorUnderline)

internal fun keyword(text: String): LessonCodeToken = LessonCodeToken(text = text, kind = LessonCodeTokenKind.Keyword)

internal fun function(text: String): LessonCodeToken = LessonCodeToken(text = text, kind = LessonCodeTokenKind.Function)

internal fun number(
    text: String,
    hasErrorUnderline: Boolean = false,
): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.Number,
        hasErrorUnderline = hasErrorUnderline,
    )

internal fun operator(
    text: String,
    hasErrorUnderline: Boolean = false,
): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.Operator,
        hasErrorUnderline = hasErrorUnderline,
    )

internal fun punctuation(text: String): LessonCodeToken = LessonCodeToken(text = text, kind = LessonCodeTokenKind.Punctuation)

internal fun string(
    text: String,
    hasErrorUnderline: Boolean = false,
): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.StringLiteral,
        hasErrorUnderline = hasErrorUnderline,
    )

internal fun paragraph(vararg spans: LessonTextSpan): LessonExplanationBlock.Paragraph = LessonExplanationBlock.Paragraph(LessonRichText(spans.toList()))

internal fun paragraph(text: String): LessonExplanationBlock.Paragraph = paragraph(regular(text))

/** Párrafos de texto sin formato, para pasos que no citan código ni elementos de la interfaz. */
internal fun paragraphs(vararg texts: String): List<LessonExplanationBlock> = texts.map(::paragraph)

internal fun richText(vararg spans: LessonTextSpan): LessonRichText = LessonRichText(spans.toList())

internal fun richText(text: String): LessonRichText = richText(regular(text))

internal fun steps(vararg items: String): LessonExplanationBlock.Steps = LessonExplanationBlock.Steps(items.map { item -> LessonRichText(listOf(regular(item))) })

internal fun steps(vararg items: LessonRichText): LessonExplanationBlock.Steps = LessonExplanationBlock.Steps(items.toList())

internal fun callout(
    title: String,
    text: String,
    tone: LessonCalloutTone = LessonCalloutTone.Information,
): LessonExplanationBlock.Callout = callout(title, richText(text), tone)

internal fun callout(
    title: String,
    text: LessonRichText,
    tone: LessonCalloutTone = LessonCalloutTone.Information,
): LessonExplanationBlock.Callout =
    LessonExplanationBlock.Callout(
        title = title,
        text = text,
        tone = tone,
    )

internal fun explanationTerminal(
    note: LessonRichText,
    vararg commands: String,
    caption: String? = null,
): LessonExplanationBlock.Terminal =
    LessonExplanationBlock.Terminal(
        commands = commands.toList(),
        note = note,
        caption = caption,
    )

internal fun explanationCode(
    title: String,
    note: LessonRichText,
    vararg lines: String,
): LessonExplanationBlock.Code =
    LessonExplanationBlock.Code(
        title = title,
        lines = lines.toList(),
        note = note,
    )

internal fun regular(text: String): LessonTextSpan = LessonTextSpan(text)

internal fun emphasis(text: String): LessonTextSpan = LessonTextSpan(text, LessonTextSpanStyle.Emphasis)

internal fun highlight(text: String): LessonTextSpan = LessonTextSpan(text, LessonTextSpanStyle.Highlight)

internal fun inlineCode(text: String): LessonTextSpan = LessonTextSpan(text, LessonTextSpanStyle.InlineCode)
