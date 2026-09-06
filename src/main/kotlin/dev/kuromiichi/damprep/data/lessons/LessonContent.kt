package dev.kuromiichi.damprep.data.lessons

import dev.kuromiichi.damprep.data.lessons.contents.softwareDevelopmentLessonContent
import dev.kuromiichi.damprep.data.lessons.contents.welcomeRoadmapLessonContent

data class LessonStep(
    val title: String,
    val body: String,
    val visual: LessonVisualContent,
    val isOperatingSystemSensitive: Boolean = false,
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
}

data class LessonContent(
    val lessonId: String,
    val steps: List<LessonStep>,
)

object LessonContentCatalog {
    val lessons = listOf(
        welcomeRoadmapLessonContent,
        softwareDevelopmentLessonContent,
    )

    val currentLesson = lessons.first()

    fun findByLessonId(lessonId: String): LessonContent? =
        lessons.firstOrNull { it.lessonId == lessonId }

    fun hasContent(lessonId: String): Boolean =
        findByLessonId(lessonId) != null
}

internal fun codeLine(vararg tokens: LessonCodeToken): LessonCodeLine =
    LessonCodeLine(tokens = tokens.toList())

internal fun plain(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(text = text, hasErrorUnderline = hasErrorUnderline)

internal fun keyword(text: String): LessonCodeToken =
    LessonCodeToken(text = text, kind = LessonCodeTokenKind.Keyword)

internal fun function(text: String): LessonCodeToken =
    LessonCodeToken(text = text, kind = LessonCodeTokenKind.Function)

internal fun number(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.Number,
        hasErrorUnderline = hasErrorUnderline,
    )

internal fun operator(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.Operator,
        hasErrorUnderline = hasErrorUnderline,
    )

internal fun punctuation(text: String): LessonCodeToken =
    LessonCodeToken(text = text, kind = LessonCodeTokenKind.Punctuation)

internal fun string(text: String, hasErrorUnderline: Boolean = false): LessonCodeToken =
    LessonCodeToken(
        text = text,
        kind = LessonCodeTokenKind.StringLiteral,
        hasErrorUnderline = hasErrorUnderline,
    )
