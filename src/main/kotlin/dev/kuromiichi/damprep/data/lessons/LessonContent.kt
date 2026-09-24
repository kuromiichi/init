package dev.kuromiichi.damprep.data.lessons

import dev.kuromiichi.damprep.data.lessons.contents.developmentEnvironmentLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.firstProgramsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.inputDecisionsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.loopsLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.softwareDevelopmentLessonContent
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

data class LessonPractice(
    val id: String,
    val objective: LessonRichText,
    val statement: LessonRichText,
    val requirements: List<LessonRichText>,
    val expectedOutput: List<LessonConsoleRun> = emptyList(),
    val hints: List<LessonRichText> = emptyList(),
    val extension: LessonRichText? = null,
    val confirmation: LessonRichText,
)

/** Una ejecución del programa tal como se vería en la consola. */
data class LessonConsoleRun(
    val lines: List<LessonConsoleLine>,
)

data class LessonConsoleLine(
    val text: String,
    val isInput: Boolean = false,
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

    data class Code(
        val title: String,
        val lines: List<String>,
        val note: LessonRichText,
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

    /** Un ejemplo de código junto a la tabla que sigue sus variables vuelta a vuelta. */
    data class Trace(
        val example: LessonCodeExample,
        val table: LessonTraceTable,
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
)

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
        )

    fun findByLessonId(lessonId: String): LessonContent? = lessons.firstOrNull { it.lessonId == lessonId }

    fun hasContent(lessonId: String): Boolean = findByLessonId(lessonId) != null
}

internal fun consoleRun(vararg lines: LessonConsoleLine): LessonConsoleRun = LessonConsoleRun(lines.toList())

internal fun consoleInput(text: String): LessonConsoleLine = LessonConsoleLine(text = text, isInput = true)

internal fun consoleOutput(text: String): LessonConsoleLine = LessonConsoleLine(text = text)

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
