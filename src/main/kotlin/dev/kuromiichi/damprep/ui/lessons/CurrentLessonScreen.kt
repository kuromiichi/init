package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kuromiichi.damprep.ui.common.AppScreen
import dev.kuromiichi.damprep.ui.common.LoadingScreen
import dev.kuromiichi.damprep.ui.common.OperatingSystemDropdown
import dev.kuromiichi.damprep.ui.common.verticalFadingEdges
import org.koin.compose.viewmodel.koinViewModel

private val LessonContentMaxWidth = 1240.dp
internal val LessonCompactBreakpoint = 860.dp
internal val LessonVisualMaxWidth = 420.dp
internal val LessonCompactVisualMaxWidth = 440.dp
internal val LessonRichVisualMaxWidth = 560.dp
internal val LessonCompactRichVisualMaxWidth = 560.dp
internal val LessonScreenshotVisualMaxWidth = 720.dp
internal val LessonCompactScreenshotVisualMaxWidth = 640.dp
private val LessonScrollFadeHeight = 48.dp

@Composable
fun CurrentLessonScreen(
    onBack: () -> Unit,
    onLessonFinished: () -> Unit,
    viewModel: CurrentLessonViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lessonContent = uiState.lessonContent
    val lessonTitle = uiState.lessonSummary?.title ?: "Lección"
    val targetOperatingSystem = uiState.targetOperatingSystem
    val goToPreviousStep = viewModel::goToPreviousStep
    val goToNextStep = viewModel::goToNextStep

    if (uiState.isMissing || lessonContent == null) {
        MissingLessonScreen(onBack = onBack)
        return
    }

    if (uiState.isLoading) {
        LoadingScreen()
        return
    }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) onLessonFinished()
    }

    val focusRequester = remember { FocusRequester() }
    val scrollState =
        key(
            lessonContent.lessonId,
            uiState.currentStepIndex,
            uiState.isCompletionVisible,
            targetOperatingSystem,
        ) {
            rememberScrollState()
        }

    LaunchedEffect(lessonContent.lessonId) {
        focusRequester.requestFocus()
    }

    AppScreen(
        modifier =
            Modifier
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { event ->
                    if (event.type != KeyEventType.KeyDown) {
                        return@onPreviewKeyEvent false
                    }

                    when (event.key) {
                        Key.DirectionLeft -> {
                            if (uiState.canGoBack) {
                                goToPreviousStep()
                                true
                            } else {
                                false
                            }
                        }

                        Key.DirectionRight -> {
                            if (uiState.canGoForward || uiState.canShowCompletion) {
                                goToNextStep()
                                true
                            } else {
                                false
                            }
                        }

                        else -> false
                    }
                }.focusable(),
        onBack =
            if (uiState.isCompletionVisible) {
                goToPreviousStep
            } else {
                onBack
            },
        contentMaxWidth = LessonContentMaxWidth,
        topBarTitle = lessonTitle,
        topBarActions = {
            if (!uiState.isCompletionVisible && uiState.currentStep?.isOperatingSystemSensitive == true) {
                OperatingSystemDropdown(
                    selectedOperatingSystem = targetOperatingSystem,
                    onOperatingSystemSelected = viewModel::setTargetOperatingSystem,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            uiState.lessonSummary?.subjectArea?.let { subjectArea ->
                SubjectAreaTag(subjectArea)
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .widthIn(max = LessonContentMaxWidth)
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
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
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedContent(
                        targetState = uiState.currentStepIndex to uiState.isCompletionVisible,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = LessonScrollFadeHeight),
                        transitionSpec = {
                            val direction =
                                if (
                                    targetState.second || targetState.first > initialState.first
                                ) {
                                    1
                                } else {
                                    -1
                                }

                            (
                                fadeIn(animationSpec = tween(durationMillis = 220)) +
                                    slideInHorizontally(
                                        animationSpec =
                                            tween(
                                                durationMillis = 260,
                                                easing = FastOutSlowInEasing,
                                            ),
                                        initialOffsetX = { it / 12 * direction },
                                    )
                            ).togetherWith(
                                fadeOut(animationSpec = tween(durationMillis = 160)) +
                                    slideOutHorizontally(
                                        animationSpec =
                                            tween(
                                                durationMillis = 180,
                                                easing = FastOutSlowInEasing,
                                            ),
                                        targetOffsetX = { -it / 12 * direction },
                                    ),
                            ).using(SizeTransform(clip = true))
                        },
                        label = "Lesson step",
                    ) { (animatedStepIndex, isCompletionVisible) ->
                        if (isCompletionVisible) {
                            LessonCompletion(
                                lessonTitle = lessonTitle,
                                onFinish = viewModel::finishLesson,
                            )
                        } else {
                            val step = lessonContent.steps[animatedStepIndex]
                            LessonStepContent(
                                step = step,
                                targetOperatingSystem = targetOperatingSystem,
                                isLastStep = animatedStepIndex == lessonContent.steps.lastIndex,
                                isPracticeConfirmed =
                                    step.practice
                                        ?.id
                                        ?.let(uiState.confirmedPracticeIds::contains)
                                        ?: true,
                                onPracticeConfirmationChanged =
                                    viewModel::setCurrentPracticeConfirmed,
                            )
                        }
                    }
                }
            }

            if (!uiState.isCompletionVisible) {
                LessonStepNavigation(
                    currentStepIndex = uiState.currentStepIndex,
                    stepCount = uiState.stepCount,
                    canGoBack = uiState.canGoBack,
                    canContinue = uiState.canGoForward || uiState.canShowCompletion,
                    onPrevious = goToPreviousStep,
                    onNext = goToNextStep,
                )
            }
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
            modifier =
                Modifier
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
