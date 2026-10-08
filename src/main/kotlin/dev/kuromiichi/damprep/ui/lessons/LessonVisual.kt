package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kuromiichi.dam_prep.generated.resources.Res
import dev.kuromiichi.dam_prep.generated.resources.git
import dev.kuromiichi.dam_prep.generated.resources.intellij_debug_breakpoint_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_debug_breakpoint_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_debugger_panel_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_debugger_panel_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_debugger_paused_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_debugger_paused_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_branches_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_branches_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_commit_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_commit_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_conflicts_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_conflicts_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_log_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_git_log_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_ide_workspace_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_ide_workspace_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_idea
import dev.kuromiichi.dam_prep.generated.resources.intellij_mermaid_class_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_mermaid_class_light
import dev.kuromiichi.dam_prep.generated.resources.intellij_test_results_dark
import dev.kuromiichi.dam_prep.generated.resources.intellij_test_results_light
import dev.kuromiichi.dam_prep.generated.resources.jdk
import dev.kuromiichi.dam_prep.generated.resources.kotlin
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonCodeLine
import dev.kuromiichi.damprep.data.lessons.LessonCodeToken
import dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind
import dev.kuromiichi.damprep.data.lessons.LessonConsoleKind
import dev.kuromiichi.damprep.data.lessons.LessonConsoleRun
import dev.kuromiichi.damprep.data.lessons.LessonScreenshot
import dev.kuromiichi.damprep.data.lessons.LessonToolLogo
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
import dev.kuromiichi.damprep.ui.common.CodeExamplesVisual
import dev.kuromiichi.damprep.ui.common.ConsoleOutputVisual
import dev.kuromiichi.damprep.ui.common.CycleDiagram
import dev.kuromiichi.damprep.ui.common.FlowDiagram
import dev.kuromiichi.damprep.ui.common.IconTextLearningVisual
import dev.kuromiichi.damprep.ui.common.LearningCodeExample
import dev.kuromiichi.damprep.ui.common.LearningCodeLine
import dev.kuromiichi.damprep.ui.common.LearningCodeToken
import dev.kuromiichi.damprep.ui.common.LearningCodeTokenKind
import dev.kuromiichi.damprep.ui.common.LearningConsoleLine
import dev.kuromiichi.damprep.ui.common.LearningConsoleRun
import dev.kuromiichi.damprep.ui.common.LearningToolCard
import dev.kuromiichi.damprep.ui.common.LearningTraceTable
import dev.kuromiichi.damprep.ui.common.LearningVisualSurface
import dev.kuromiichi.damprep.ui.common.LearningVisualSurfacePadding
import dev.kuromiichi.damprep.ui.common.ScreenshotLearningVisual
import dev.kuromiichi.damprep.ui.common.TerminalWindowVisual
import dev.kuromiichi.damprep.ui.common.ToolLogoCards
import dev.kuromiichi.damprep.ui.common.TraceTableVisual
import dev.kuromiichi.damprep.ui.common.focusOutline
import dev.kuromiichi.damprep.ui.common.verticalFadingEdges
import dev.kuromiichi.damprep.ui.theme.CatppuccinMacchiato
import dev.kuromiichi.damprep.ui.theme.LocalCatppuccinFlavor
import org.jetbrains.compose.resources.DrawableResource

/** Margen que deja el contorno del visual resaltado fuera de su panel. */
internal val LessonVisualOutlineOutset = 8.dp

/**
 * Con [scrollState] el visual se desplaza dentro de su panel, con los bordes difuminados, cuando no cabe en el alto
 * que se le da; sin él ocupa su alto natural.
 */
@Composable
internal fun LessonVisual(
    visual: LessonVisualContent,
    targetOperatingSystem: TargetOperatingSystem?,
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null,
    isOutlined: Boolean = false,
) {
    if (visual is LessonVisualContent.Screenshot) {
        val screenshot: @Composable (Modifier) -> Unit = { screenshotModifier ->
            ScreenshotLearningVisual(
                image = visual.image.drawableResource(isDark = LocalCatppuccinFlavor.current == CatppuccinMacchiato),
                description = visual.description,
                modifier = screenshotModifier,
            )
        }
        if (scrollState == null) {
            screenshot(modifier)
        } else {
            Box(
                modifier =
                    modifier
                        .focusOutline(isOutlined, LessonVisualOutlineOutset)
                        .verticalFadingEdges(scrollState, MaterialTheme.colorScheme.background, LessonScrollFadeHeight)
                        .verticalScroll(scrollState),
            ) {
                screenshot(Modifier.fillMaxWidth())
            }
        }
        return
    }

    val content: @Composable () -> Unit = {
        when (visual) {
            is LessonVisualContent.CodeExamples ->
                CodeExamplesVisual(
                    examples = visual.examples.map { it.toLearningCodeExample() },
                )

            is LessonVisualContent.Console ->
                ConsoleVisual(
                    kind = visual.kind,
                    runs = visual.runs.map { it.toLearningConsoleRun() },
                    targetOperatingSystem = targetOperatingSystem,
                    caption = visual.caption,
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

            is LessonVisualContent.Screenshot -> Unit

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

            is LessonVisualContent.UmlDiagram -> UmlDiagramVisual(diagram = visual)
        }
    }

    if (scrollState == null) {
        LearningVisualSurface(modifier = modifier) { content() }
    } else {
        // El relleno va dentro del desplazamiento para que el contenido llegue hasta el borde del panel y se difumine con él.
        LearningVisualSurface(
            modifier = modifier.focusOutline(isOutlined, LessonVisualOutlineOutset),
            contentPadding = 0.dp,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalFadingEdges(scrollState, MaterialTheme.colorScheme.surfaceContainerLow, LessonScrollFadeHeight)
                        .verticalScroll(scrollState)
                        .padding(LearningVisualSurfacePadding),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    }
}

internal val LessonVisualContent.isRich: Boolean
    get() =
        this is LessonVisualContent.Flow ||
            this is LessonVisualContent.Cycle ||
            this is LessonVisualContent.CodeExamples ||
            this is LessonVisualContent.Console ||
            this is LessonVisualContent.Trace ||
            this is LessonVisualContent.UmlDiagram ||
            this is LessonVisualContent.Screenshot

/** La terminal del sistema elegido o la ventana Run de IntelliJ IDEA, según dónde se vería la salida. */
@Composable
internal fun ConsoleVisual(
    kind: LessonConsoleKind,
    runs: List<LearningConsoleRun>,
    targetOperatingSystem: TargetOperatingSystem?,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    when (kind) {
        LessonConsoleKind.Terminal ->
            TerminalWindowVisual(
                runs = runs,
                operatingSystem = targetOperatingSystem,
                modifier = modifier,
                caption = caption,
            )

        LessonConsoleKind.IntelliJRun -> ConsoleOutputVisual(runs = runs, modifier = modifier, caption = caption)
    }
}

private fun LessonCodeExample.toLearningCodeExample(): LearningCodeExample =
    LearningCodeExample(
        fileName = fileName,
        title = title.takeUnless { it == fileName },
        lines = lines.map { it.toLearningCodeLine() },
        note = note.toCodeNote(),
        copyText = sourceText.takeIf { isCopyable },
    )

internal fun LessonConsoleRun.toLearningConsoleRun(): LearningConsoleRun =
    LearningConsoleRun(
        lines = lines.map { line -> LearningConsoleLine(text = line.text, isInput = line.isInput, isError = line.isError) },
    )

internal fun LessonCodeLine.toLearningCodeLine(): LearningCodeLine = LearningCodeLine(tokens = tokens.map { it.toLearningCodeToken() })

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
        LessonCodeTokenKind.ClassName -> LearningCodeTokenKind.ClassName
        LessonCodeTokenKind.Parameter -> LearningCodeTokenKind.Parameter
        LessonCodeTokenKind.Constant -> LearningCodeTokenKind.Constant
        LessonCodeTokenKind.MutableVariable -> LearningCodeTokenKind.MutableVariable
        LessonCodeTokenKind.LambdaDelimiter -> LearningCodeTokenKind.LambdaDelimiter
        LessonCodeTokenKind.Bracket0 -> LearningCodeTokenKind.Bracket0
        LessonCodeTokenKind.Bracket1 -> LearningCodeTokenKind.Bracket1
        LessonCodeTokenKind.Bracket2 -> LearningCodeTokenKind.Bracket2
        LessonCodeTokenKind.Bracket3 -> LearningCodeTokenKind.Bracket3
        LessonCodeTokenKind.Bracket4 -> LearningCodeTokenKind.Bracket4
    }

private val LessonToolLogo.drawableResource: DrawableResource
    get() =
        when (this) {
            LessonToolLogo.Kotlin -> Res.drawable.kotlin
            LessonToolLogo.IntellijIdea -> Res.drawable.intellij_idea
            LessonToolLogo.Jdk -> Res.drawable.jdk
            LessonToolLogo.Git -> Res.drawable.git
        }

/** Las capturas de IntelliJ IDEA existen con el tema Catppuccin claro y oscuro del IDE; se elige la que acompaña al tema de la app. */
private fun LessonScreenshot.drawableResource(isDark: Boolean): DrawableResource =
    when (this) {
        LessonScreenshot.IntellijIdeWorkspace -> if (isDark) Res.drawable.intellij_ide_workspace_dark else Res.drawable.intellij_ide_workspace_light
        LessonScreenshot.IntellijDebugBreakpoint -> if (isDark) Res.drawable.intellij_debug_breakpoint_dark else Res.drawable.intellij_debug_breakpoint_light
        LessonScreenshot.IntellijDebuggerPaused -> if (isDark) Res.drawable.intellij_debugger_paused_dark else Res.drawable.intellij_debugger_paused_light
        LessonScreenshot.IntellijDebuggerPanel -> if (isDark) Res.drawable.intellij_debugger_panel_dark else Res.drawable.intellij_debugger_panel_light
        LessonScreenshot.IntellijGitCommit -> if (isDark) Res.drawable.intellij_git_commit_dark else Res.drawable.intellij_git_commit_light
        LessonScreenshot.IntellijGitBranches -> if (isDark) Res.drawable.intellij_git_branches_dark else Res.drawable.intellij_git_branches_light
        LessonScreenshot.IntellijGitLog -> if (isDark) Res.drawable.intellij_git_log_dark else Res.drawable.intellij_git_log_light
        LessonScreenshot.IntellijGitConflicts -> if (isDark) Res.drawable.intellij_git_conflicts_dark else Res.drawable.intellij_git_conflicts_light
        LessonScreenshot.IntellijTestResults -> if (isDark) Res.drawable.intellij_test_results_dark else Res.drawable.intellij_test_results_light
        LessonScreenshot.IntellijMermaidClass -> if (isDark) Res.drawable.intellij_mermaid_class_dark else Res.drawable.intellij_mermaid_class_light
    }
