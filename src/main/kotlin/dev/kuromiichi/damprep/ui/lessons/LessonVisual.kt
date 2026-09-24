package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kuromiichi.dam_prep.generated.resources.Res
import dev.kuromiichi.dam_prep.generated.resources.git
import dev.kuromiichi.dam_prep.generated.resources.intellij_debug_breakpoint
import dev.kuromiichi.dam_prep.generated.resources.intellij_ide_workspace
import dev.kuromiichi.dam_prep.generated.resources.intellij_idea
import dev.kuromiichi.dam_prep.generated.resources.jdk
import dev.kuromiichi.dam_prep.generated.resources.kotlin
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonCodeLine
import dev.kuromiichi.damprep.data.lessons.LessonCodeToken
import dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind
import dev.kuromiichi.damprep.data.lessons.LessonScreenshot
import dev.kuromiichi.damprep.data.lessons.LessonToolLogo
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.ui.common.CodeExamplesVisual
import dev.kuromiichi.damprep.ui.common.CycleDiagram
import dev.kuromiichi.damprep.ui.common.FlowDiagram
import dev.kuromiichi.damprep.ui.common.IconTextLearningVisual
import dev.kuromiichi.damprep.ui.common.LearningCodeExample
import dev.kuromiichi.damprep.ui.common.LearningCodeLine
import dev.kuromiichi.damprep.ui.common.LearningCodeToken
import dev.kuromiichi.damprep.ui.common.LearningCodeTokenKind
import dev.kuromiichi.damprep.ui.common.LearningToolCard
import dev.kuromiichi.damprep.ui.common.LearningTraceTable
import dev.kuromiichi.damprep.ui.common.LearningVisualSurface
import dev.kuromiichi.damprep.ui.common.ScreenshotLearningVisual
import dev.kuromiichi.damprep.ui.common.ToolLogoCards
import dev.kuromiichi.damprep.ui.common.TraceTableVisual
import org.jetbrains.compose.resources.DrawableResource

@Composable
internal fun LessonVisual(
    visual: LessonVisualContent,
    modifier: Modifier = Modifier,
) {
    LearningVisualSurface(
        modifier = modifier,
        contentPadding = if (visual is LessonVisualContent.Screenshot) 12.dp else 28.dp,
    ) {
        when (visual) {
            is LessonVisualContent.CodeExamples ->
                CodeExamplesVisual(
                    examples = visual.examples.map { it.toLearningCodeExample() },
                )

            is LessonVisualContent.Cycle ->
                CycleDiagram(
                    steps = visual.steps,
                )

            is LessonVisualContent.Flow -> FlowDiagram(nodes = visual.nodes)
            is LessonVisualContent.IconText ->
                IconTextLearningVisual(
                    title = visual.title,
                    body = visual.body,
                )

            is LessonVisualContent.Screenshot ->
                ScreenshotLearningVisual(
                    image = visual.image.drawableResource,
                    description = visual.description,
                )

            is LessonVisualContent.Trace ->
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    CodeExamplesVisual(examples = listOf(visual.example.toLearningCodeExample()))
                    TraceTableVisual(
                        table =
                            LearningTraceTable(
                                title = visual.table.title,
                                columns = visual.table.columns,
                                rows = visual.table.rows,
                            ),
                    )
                }

            is LessonVisualContent.ToolCards ->
                ToolLogoCards(
                    tools =
                        visual.tools.map { tool ->
                            LearningToolCard(
                                title = tool.name,
                                subtitle = tool.kind,
                                logo = tool.logo.drawableResource,
                            )
                        },
                )
        }
    }
}

internal val LessonVisualContent.isRich: Boolean
    get() =
        this is LessonVisualContent.Flow ||
            this is LessonVisualContent.Cycle ||
            this is LessonVisualContent.CodeExamples ||
            this is LessonVisualContent.Trace ||
            this is LessonVisualContent.Screenshot

private fun LessonCodeExample.toLearningCodeExample(): LearningCodeExample =
    LearningCodeExample(
        title = title,
        lines = lines.map { it.toLearningCodeLine() },
        note = note.toCodeNote(),
    )

private fun LessonCodeLine.toLearningCodeLine(): LearningCodeLine = LearningCodeLine(tokens = tokens.map { it.toLearningCodeToken() })

private fun LessonCodeToken.toLearningCodeToken(): LearningCodeToken =
    LearningCodeToken(
        text = text,
        kind = kind.toLearningCodeTokenKind(),
        hasErrorUnderline = hasErrorUnderline,
    )

private fun LessonCodeTokenKind.toLearningCodeTokenKind(): LearningCodeTokenKind =
    when (this) {
        LessonCodeTokenKind.Plain -> LearningCodeTokenKind.Plain
        LessonCodeTokenKind.Keyword -> LearningCodeTokenKind.Keyword
        LessonCodeTokenKind.Function -> LearningCodeTokenKind.Function
        LessonCodeTokenKind.Number -> LearningCodeTokenKind.Number
        LessonCodeTokenKind.Operator -> LearningCodeTokenKind.Operator
        LessonCodeTokenKind.Punctuation -> LearningCodeTokenKind.Punctuation
        LessonCodeTokenKind.StringLiteral -> LearningCodeTokenKind.StringLiteral
        LessonCodeTokenKind.Interpolation -> LearningCodeTokenKind.Interpolation
        LessonCodeTokenKind.Comment -> LearningCodeTokenKind.Comment
    }

private val LessonToolLogo.drawableResource: DrawableResource
    get() =
        when (this) {
            LessonToolLogo.Kotlin -> Res.drawable.kotlin
            LessonToolLogo.IntellijIdea -> Res.drawable.intellij_idea
            LessonToolLogo.Jdk -> Res.drawable.jdk
            LessonToolLogo.Git -> Res.drawable.git
        }

private val LessonScreenshot.drawableResource: DrawableResource
    get() =
        when (this) {
            LessonScreenshot.IntellijIdeWorkspace -> Res.drawable.intellij_ide_workspace
            LessonScreenshot.IntellijDebugBreakpoint -> Res.drawable.intellij_debug_breakpoint
        }
