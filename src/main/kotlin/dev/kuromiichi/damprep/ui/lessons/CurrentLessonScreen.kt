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
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.dam_prep.generated.resources.Res
import dev.kuromiichi.dam_prep.generated.resources.git
import dev.kuromiichi.dam_prep.generated.resources.intellij_ide_workspace
import dev.kuromiichi.dam_prep.generated.resources.intellij_idea
import dev.kuromiichi.dam_prep.generated.resources.jdk
import dev.kuromiichi.dam_prep.generated.resources.kotlin
import dev.kuromiichi.damprep.data.lessons.LessonCodeLine
import dev.kuromiichi.damprep.data.lessons.LessonCodeToken
import dev.kuromiichi.damprep.data.lessons.LessonCodeTokenKind
import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonCheckpoint
import dev.kuromiichi.damprep.data.lessons.LessonExplanationBlock
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonRichText
import dev.kuromiichi.damprep.data.lessons.LessonScreenshot
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonTextSpanStyle
import dev.kuromiichi.damprep.data.lessons.LessonToolLogo
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem
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
import dev.kuromiichi.damprep.ui.common.OperatingSystemDropdown
import dev.kuromiichi.damprep.ui.common.ScreenshotLearningVisual
import dev.kuromiichi.damprep.ui.common.ToolLogoCards
import dev.kuromiichi.damprep.ui.common.hoverScale
import dev.kuromiichi.damprep.ui.common.verticalFadingEdges
import org.jetbrains.compose.resources.DrawableResource

private val LessonContentMaxWidth = 1240.dp
private val LessonCompactBreakpoint = 860.dp
private val LessonVisualMaxWidth = 420.dp
private val LessonCompactVisualMaxWidth = 440.dp
private val LessonRichVisualMaxWidth = 560.dp
private val LessonCompactRichVisualMaxWidth = 560.dp
private val LessonScreenshotVisualMaxWidth = 720.dp
private val LessonCompactScreenshotVisualMaxWidth = 640.dp
private val LessonScrollFadeHeight = 48.dp

@Composable
fun CurrentLessonScreen(
    lessonId: String,
    lessonTitle: String,
    targetOperatingSystem: TargetOperatingSystem?,
    onTargetOperatingSystemChanged: (TargetOperatingSystem) -> Unit,
    onBack: () -> Unit,
    onLessonFinished: () -> Unit,
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
    val scrollState = key(
        lessonId,
        uiState.currentStepIndex,
        targetOperatingSystem,
    ) {
        rememberScrollState()
    }

    LaunchedEffect(lessonContent.lessonId) {
        focusRequester.requestFocus()
    }

    AppScreen(
        onBack = onBack,
        contentMaxWidth = LessonContentMaxWidth,
        topBarTitle = lessonTitle,
        topBarActions = {
            if (uiState.currentStep?.isOperatingSystemSensitive == true) {
                OperatingSystemDropdown(
                    selectedOperatingSystem = targetOperatingSystem,
                    onOperatingSystemSelected = onTargetOperatingSystemChanged,
                )
            }
        },
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
                            when {
                                uiState.canGoForward -> {
                                    viewModel.goToNextStep()
                                    true
                                }

                                uiState.canFinish -> {
                                    onLessonFinished()
                                    true
                                }

                                else -> false
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
                    .weight(1f)
                    .verticalFadingEdges(
                        scrollState = scrollState,
                        color = MaterialTheme.colorScheme.background,
                        length = LessonScrollFadeHeight,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedContent(
                        targetState = uiState.currentStepIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = LessonScrollFadeHeight),
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
                        val step = lessonContent.steps[animatedStepIndex]
                        LessonStepContent(
                            step = step,
                            targetOperatingSystem = targetOperatingSystem,
                            isLastStep = animatedStepIndex == lessonContent.steps.lastIndex,
                            isCheckpointConfirmed = step.checkpoint?.id
                                ?.let(uiState.confirmedCheckpointIds::contains)
                                ?: true,
                            onCheckpointConfirmationChanged =
                                viewModel::setCurrentCheckpointConfirmed,
                        )
                    }
                }
            }

            LessonStepNavigation(
                currentStepIndex = uiState.currentStepIndex,
                stepCount = uiState.stepCount,
                canGoBack = uiState.canGoBack,
                canGoForward = uiState.canGoForward,
                isLastStep = uiState.isLastStep,
                canFinish = uiState.canFinish,
                onPrevious = viewModel::goToPreviousStep,
                onNext = viewModel::goToNextStep,
                onFinish = onLessonFinished,
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
private fun LessonStepContent(
    step: LessonStep,
    targetOperatingSystem: TargetOperatingSystem?,
    isLastStep: Boolean,
    isCheckpointConfirmed: Boolean,
    onCheckpointConfirmationChanged: (Boolean) -> Unit,
) {
    val content = step.contentFor(targetOperatingSystem)

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
            val hasRichVisual = content.visual.isRich
            val isScreenshotVisual = content.visual is LessonVisualContent.Screenshot
            val visualMaxWidth = when {
                isScreenshotVisual -> LessonScreenshotVisualMaxWidth
                hasRichVisual -> LessonRichVisualMaxWidth
                else -> LessonVisualMaxWidth
            }
            val compactVisualMaxWidth = when {
                isScreenshotVisual -> LessonCompactScreenshotVisualMaxWidth
                hasRichVisual -> LessonCompactRichVisualMaxWidth
                else -> LessonCompactVisualMaxWidth
            }

            if (useCompactLayout) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    LessonExplanation(
                        blocks = content.explanation,
                        links = content.links,
                    )
                    LessonVisual(
                        visual = content.visual,
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
                    LessonExplanation(
                        blocks = content.explanation,
                        links = content.links,
                        modifier = Modifier.weight(
                            when {
                                isScreenshotVisual -> 0.85f
                                hasRichVisual -> 1f
                                else -> 1.25f
                            },
                        ),
                    )
                    Box(
                        modifier = Modifier.weight(
                            when {
                                isScreenshotVisual -> 1.15f
                                hasRichVisual -> 1f
                                else -> 0.75f
                            },
                        ),
                        contentAlignment = Alignment.Center,
                    ) {
                        LessonVisual(
                            visual = content.visual,
                            modifier = Modifier
                                .widthIn(max = visualMaxWidth)
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }

        step.checkpoint?.let { checkpoint ->
            LessonCheckpointCard(
                checkpoint = checkpoint,
                checked = isCheckpointConfirmed,
                isLessonCompletion = isLastStep,
                onCheckedChange = onCheckpointConfirmationChanged,
            )
        }
    }
}

@Composable
private fun LessonCheckpointCard(
    checkpoint: LessonCheckpoint,
    checked: Boolean,
    isLessonCompletion: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(
        onClick = { onCheckedChange(!checked) },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (checked) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (checked) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (isLessonCompletion) {
                        "Para terminar la lección"
                    } else {
                        "Antes de continuar"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = checkpoint.prompt,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun LessonExplanation(
    blocks: List<LessonExplanationBlock>,
    links: List<LessonLink>,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        blocks.forEach { block ->
            when (block) {
                is LessonExplanationBlock.Callout -> LessonCallout(block)
                is LessonExplanationBlock.Code -> LessonExplanationCode(block)
                is LessonExplanationBlock.Paragraph -> LessonParagraph(block.text)
                is LessonExplanationBlock.Steps -> LessonSteps(block.items)
            }
        }

        links.forEach { link ->
            TextButton(
                onClick = { uriHandler.openUri(link.url) },
                modifier = Modifier.hoverScale(),
            ) {
                Text(link.label)
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = "Abrir enlace externo",
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun LessonParagraph(text: LessonRichText) {
    Text(
        text = text.toAnnotatedString(),
        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun LessonSteps(items: List<LessonRichText>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEachIndexed { index, item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Box(
                        modifier = Modifier.size(28.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
                Text(
                    text = item.toAnnotatedString(),
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun LessonCallout(callout: LessonExplanationBlock.Callout) {
    val containerColor = when (callout.tone) {
        LessonCalloutTone.Information -> MaterialTheme.colorScheme.primaryContainer
        LessonCalloutTone.Tip -> MaterialTheme.colorScheme.secondaryContainer
        LessonCalloutTone.Warning -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (callout.tone) {
        LessonCalloutTone.Information -> MaterialTheme.colorScheme.onPrimaryContainer
        LessonCalloutTone.Tip -> MaterialTheme.colorScheme.onSecondaryContainer
        LessonCalloutTone.Warning -> MaterialTheme.colorScheme.onErrorContainer
    }
    val icon = when (callout.tone) {
        LessonCalloutTone.Information -> Icons.Filled.Info
        LessonCalloutTone.Tip -> Icons.Filled.Lightbulb
        LessonCalloutTone.Warning -> Icons.Filled.Warning
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = callout.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = callout.text.toAnnotatedString(),
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                )
            }
        }
    }
}

@Composable
private fun LessonExplanationCode(code: LessonExplanationBlock.Code) {
    CodeExamplesVisual(
        examples = listOf(
            LearningCodeExample(
                title = code.title,
                lines = code.lines.map { line ->
                    LearningCodeLine(
                        tokens = listOf(LearningCodeToken(text = line)),
                    )
                },
                note = code.note,
            ),
        ),
    )
}

@Composable
private fun LessonRichText.toAnnotatedString() = buildAnnotatedString {
    spans.forEach { span ->
        val style = when (span.style) {
            LessonTextSpanStyle.Regular -> SpanStyle()
            LessonTextSpanStyle.Emphasis -> SpanStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            LessonTextSpanStyle.Highlight -> SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
            LessonTextSpanStyle.InlineCode -> SpanStyle(
                background = MaterialTheme.colorScheme.surfaceVariant,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace,
            )
        }

        withStyle(style) {
            append(span.text)
        }
    }
}

@Composable
private fun LessonVisual(
    visual: LessonVisualContent,
    modifier: Modifier = Modifier,
) {
    LearningVisualSurface(
        modifier = modifier,
        contentPadding = if (visual is LessonVisualContent.Screenshot) 12.dp else 28.dp,
    ) {
        when (visual) {
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

            is LessonVisualContent.Screenshot -> ScreenshotLearningVisual(
                image = visual.image.drawableResource,
                description = visual.description,
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
        this is LessonVisualContent.CodeExamples ||
        this is LessonVisualContent.Screenshot

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
        LessonToolLogo.Jdk -> Res.drawable.jdk
        LessonToolLogo.Git -> Res.drawable.git
    }

private val LessonScreenshot.drawableResource: DrawableResource
    get() = when (this) {
        LessonScreenshot.IntellijIdeWorkspace -> Res.drawable.intellij_ide_workspace
    }

@Composable
private fun LessonStepNavigation(
    currentStepIndex: Int,
    stepCount: Int,
    canGoBack: Boolean,
    canGoForward: Boolean,
    isLastStep: Boolean,
    canFinish: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
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

        if (isLastStep) {
            FilledIconButton(
                onClick = onFinish,
                enabled = canFinish,
                modifier = Modifier.hoverScale(enabled = canFinish),
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Finalizar lección",
                )
            }
        } else {
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
}

@Composable
private fun LessonProgressDots(
    currentStepIndex: Int,
    stepCount: Int,
) {
    Row(
        modifier = Modifier.progressSemantics(
            value = (currentStepIndex + 1).toFloat(),
            valueRange = 0f..stepCount.toFloat(),
            steps = (stepCount - 1).coerceAtLeast(0),
        ),
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
