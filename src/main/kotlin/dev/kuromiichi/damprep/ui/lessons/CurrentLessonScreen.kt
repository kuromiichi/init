package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.dam_prep.generated.resources.Res
import dev.kuromiichi.dam_prep.generated.resources.intellij_idea
import dev.kuromiichi.dam_prep.generated.resources.kotlin
import dev.kuromiichi.damprep.data.lessons.LessonCodeLine
import dev.kuromiichi.damprep.data.lessons.LessonCodeToken
import dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonToolLogo
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.ui.common.AppScreen
import dev.kuromiichi.damprep.ui.common.AppScreenContentMaxWidth
import dev.kuromiichi.damprep.ui.common.CodeExamplesVisual
import dev.kuromiichi.damprep.ui.common.CycleDiagram
import dev.kuromiichi.damprep.ui.common.FlowDiagram
import dev.kuromiichi.damprep.ui.common.IconTextLearningVisual
import dev.kuromiichi.damprep.ui.common.LearningCodeExample
import dev.kuromiichi.damprep.ui.common.LearningCodeLine
import dev.kuromiichi.damprep.ui.common.LearningCodeToken
import dev.kuromiichi.damprep.ui.common.LearningCodeTokenKind
import dev.kuromiichi.damprep.ui.common.LearningToolCard
import dev.kuromiichi.damprep.ui.common.LearningVisualSurface
import dev.kuromiichi.damprep.ui.common.ToolLogoCards
import dev.kuromiichi.damprep.ui.common.hoverScale
import org.jetbrains.compose.resources.DrawableResource

private val LessonContentMaxWidth = 1240.dp
private val LessonCompactBreakpoint = 860.dp
private val LessonVisualMaxWidth = 420.dp
private val LessonCompactVisualMaxWidth = 440.dp
private val LessonRichVisualMaxWidth = 560.dp
private val LessonCompactRichVisualMaxWidth = 560.dp

@Composable
fun CurrentLessonScreen(
    lessonId: String,
    onBack: () -> Unit,
) {
    val viewModel = remember(lessonId) {
        CurrentLessonViewModel(lessonId = lessonId)
    }
    val uiState by viewModel.uiState.collectAsState()
    val lessonContent = uiState.lessonContent

    if (uiState.isMissing || lessonContent == null) {
        MissingLessonScreen(onBack = onBack)
        return
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(lessonContent.lessonId) {
        focusRequester.requestFocus()
    }

    AppScreen(
        onBack = onBack,
        contentMaxWidth = LessonContentMaxWidth,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = LessonContentMaxWidth)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { event ->
                    if (event.type != KeyEventType.KeyDown) {
                        return@onPreviewKeyEvent false
                    }

                    when (event.key) {
                        Key.DirectionLeft -> {
                            if (uiState.canGoBack) {
                                viewModel.goToPreviousStep()
                                true
                            } else {
                                false
                            }
                        }

                        Key.DirectionRight -> {
                            if (uiState.canGoForward) {
                                viewModel.goToNextStep()
                                true
                            } else {
                                false
                            }
                        }

                        else -> false
                    }
                }
                .focusable()
                .padding(top = 8.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedContent(
                        targetState = uiState.currentStepIndex,
                        modifier = Modifier.fillMaxWidth(),
                        transitionSpec = {
                            val direction = if (targetState > initialState) 1 else -1

                            (
                                fadeIn(animationSpec = tween(durationMillis = 220)) +
                                    slideInHorizontally(
                                        animationSpec = tween(
                                            durationMillis = 260,
                                            easing = FastOutSlowInEasing,
                                        ),
                                        initialOffsetX = { it / 12 * direction },
                                    )
                                ).togetherWith(
                                fadeOut(animationSpec = tween(durationMillis = 160)) +
                                    slideOutHorizontally(
                                        animationSpec = tween(
                                            durationMillis = 180,
                                            easing = FastOutSlowInEasing,
                                        ),
                                        targetOffsetX = { -it / 12 * direction },
                                    ),
                            ).using(SizeTransform(clip = true))
                        },
                        label = "Lesson step",
                    ) { animatedStepIndex ->
                        LessonStepContent(step = lessonContent.steps[animatedStepIndex])
                    }
                }
            }

            LessonStepNavigation(
                currentStepIndex = uiState.currentStepIndex,
                stepCount = uiState.stepCount,
                canGoBack = uiState.canGoBack,
                canGoForward = uiState.canGoForward,
                onPrevious = viewModel::goToPreviousStep,
                onNext = viewModel::goToNextStep,
            )
        }
    }
}

@Composable
private fun MissingLessonScreen(onBack: () -> Unit) {
    AppScreen(
        onBack = onBack,
        contentMaxWidth = LessonContentMaxWidth,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = LessonContentMaxWidth)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Lección no disponible",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Todavía no hemos preparado el contenido de esta lección.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun LessonStepContent(step: LessonStep) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        Text(
            text = step.title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val useCompactLayout = maxWidth < LessonCompactBreakpoint
            val hasRichVisual = step.visual.isRich
            val visualMaxWidth = if (hasRichVisual) {
                LessonRichVisualMaxWidth
            } else {
                LessonVisualMaxWidth
            }
            val compactVisualMaxWidth = if (hasRichVisual) {
                LessonCompactRichVisualMaxWidth
            } else {
                LessonCompactVisualMaxWidth
            }

            if (useCompactLayout) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    LessonText(step.body)
                    LessonVisual(
                        step = step,
                        modifier = Modifier
                            .widthIn(max = compactVisualMaxWidth)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(56.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LessonText(
                        text = step.body,
                        modifier = Modifier.weight(if (hasRichVisual) 1f else 1.25f),
                    )
                    Box(
                        modifier = Modifier.weight(if (hasRichVisual) 1f else 0.75f),
                        contentAlignment = Alignment.Center,
                    ) {
                        LessonVisual(
                            step = step,
                            modifier = Modifier
                                .widthIn(max = visualMaxWidth)
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(lineHeight = 34.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Composable
private fun LessonVisual(
    step: LessonStep,
    modifier: Modifier = Modifier,
) {
    LearningVisualSurface(modifier = modifier) {
        when (val visual = step.visual) {
            is LessonVisualContent.CodeExamples -> CodeExamplesVisual(
                examples = visual.examples.map { example ->
                    LearningCodeExample(
                        title = example.title,
                        lines = example.lines.map { it.toLearningCodeLine() },
                        note = example.note,
                    )
                },
            )

            is LessonVisualContent.Cycle -> CycleDiagram(
                steps = visual.steps,
            )

            is LessonVisualContent.Flow -> FlowDiagram(nodes = visual.nodes)
            is LessonVisualContent.IconText -> IconTextLearningVisual(
                title = visual.title,
                body = visual.body,
            )

            is LessonVisualContent.ToolCards -> ToolLogoCards(
                tools = visual.tools.map { tool ->
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

private val LessonVisualContent.isRich: Boolean
    get() = this is LessonVisualContent.Flow ||
        this is LessonVisualContent.Cycle ||
        this is LessonVisualContent.CodeExamples

private fun LessonCodeLine.toLearningCodeLine(): LearningCodeLine =
    LearningCodeLine(tokens = tokens.map { it.toLearningCodeToken() })

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
    get() = when (this) {
        LessonToolLogo.Kotlin -> Res.drawable.kotlin
        LessonToolLogo.IntellijIdea -> Res.drawable.intellij_idea
    }

@Composable
private fun LessonStepNavigation(
    currentStepIndex: Int,
    stepCount: Int,
    canGoBack: Boolean,
    canGoForward: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .widthIn(max = AppScreenContentMaxWidth)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedIconButton(
            onClick = onPrevious,
            enabled = canGoBack,
            modifier = Modifier.hoverScale(enabled = canGoBack),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Paso anterior",
            )
        }

        LessonProgressDots(
            currentStepIndex = currentStepIndex,
            stepCount = stepCount,
        )

        FilledIconButton(
            onClick = onNext,
            enabled = canGoForward,
            modifier = Modifier.hoverScale(enabled = canGoForward),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Paso siguiente",
            )
        }
    }
}

@Composable
private fun LessonProgressDots(
    currentStepIndex: Int,
    stepCount: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(stepCount) { index ->
            val isCurrent = index == currentStepIndex
            val dotColor by animateColorAsState(
                targetValue = if (isCurrent) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                animationSpec = tween(durationMillis = 240),
                label = "Lesson dot color",
            )
            val dotWidth by animateDpAsState(
                targetValue = if (isCurrent) 24.dp else 8.dp,
                animationSpec = tween(
                    durationMillis = 260,
                    easing = FastOutSlowInEasing,
                ),
                label = "Lesson dot width",
            )

            Box(
                modifier = Modifier
                    .size(height = 8.dp, width = dotWidth)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(dotColor),
            )
        }
    }
}
