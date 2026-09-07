package dev.kuromiichi.damprep.data.lessons

import dev.kuromiichi.damprep.data.lessons.contents.developmentEnvironmentLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.softwareDevelopmentLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.welcomeRoadmapLessonContent
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem

data class LessonStep(
    val title: String,
    val body: String,
    val visual: LessonVisualContent,
    val links: List<LessonLink> = emptyList(),
    val explanation: List<LessonExplanationBlock> = body.toParagraphBlocks(),
    val operatingSystemContent: Map<TargetOperatingSystem, LessonStepContent> = emptyMap(),
    val checkpoint: LessonCheckpoint? = null,
) {
    val isOperatingSystemSensitive: Boolean
        get() = operatingSystemContent.isNotEmpty()

    fun contentFor(targetOperatingSystem: TargetOperatingSystem?): LessonStepContent =
        targetOperatingSystem
            ?.let(operatingSystemContent::get)
            ?: LessonStepContent(
                body = body,
                visual = visual,
                links = links,
                explanation = explanation,
            )
}

data class LessonCheckpoint(
    val id: String,
    val prompt: String,
)

data class LessonStepContent(
    val body: String,
    val visual: LessonVisualContent,
    val links: List<LessonLink> = emptyList(),
    val explanation: List<LessonExplanationBlock> = body.toParagraphBlocks(),
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
        val note: String,
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
}

data class LessonCodeExample(
    val title: String,
    val lines: List<LessonCodeLine>,
    val note: String,
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
        )

    val currentLesson = lessons.first()

    fun findByLessonId(lessonId: String): LessonContent? = lessons.firstOrNull { it.lessonId == lessonId }

    fun hasContent(lessonId: String): Boolean = findByLessonId(lessonId) != null
}

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

internal fun steps(vararg items: String): LessonExplanationBlock.Steps = LessonExplanationBlock.Steps(items.map { item -> LessonRichText(listOf(regular(item))) })

internal fun callout(
    title: String,
    text: String,
    tone: LessonCalloutTone = LessonCalloutTone.Information,
): LessonExplanationBlock.Callout =
    LessonExplanationBlock.Callout(
        title = title,
        text = LessonRichText(listOf(regular(text))),
        tone = tone,
    )

internal fun explanationCode(
    title: String,
    note: String,
    vararg lines: String,
): LessonExplanationBlock.Code =
    LessonExplanationBlock.Code(
        title = title,
        lines = lines.toList(),
        note = note,
    )

internal fun structuredStepContent(
    explanation: List<LessonExplanationBlock>,
    visual: LessonVisualContent,
    links: List<LessonLink> = emptyList(),
): LessonStepContent =
    LessonStepContent(
        body = explanation.joinToString("\n\n", transform = LessonExplanationBlock::plainText),
        visual = visual,
        links = links,
        explanation = explanation,
    )

internal fun regular(text: String): LessonTextSpan = LessonTextSpan(text)

internal fun emphasis(text: String): LessonTextSpan = LessonTextSpan(text, LessonTextSpanStyle.Emphasis)

internal fun highlight(text: String): LessonTextSpan = LessonTextSpan(text, LessonTextSpanStyle.Highlight)

internal fun inlineCode(text: String): LessonTextSpan = LessonTextSpan(text, LessonTextSpanStyle.InlineCode)

private fun String.toParagraphBlocks(): List<LessonExplanationBlock> =
    trim()
        .split(Regex("\\n\\s*\\n"))
        .filter(String::isNotBlank)
        .map(::paragraph)

private fun LessonExplanationBlock.plainText(): String =
    when (this) {
        is LessonExplanationBlock.Callout -> "$title: ${text.plainText}"
        is LessonExplanationBlock.Code -> listOf(title, lines.joinToString("\n"), note).joinToString("\n")
        is LessonExplanationBlock.Paragraph -> text.plainText
        is LessonExplanationBlock.Steps -> items.joinToString("\n") { item -> item.plainText }
    }
