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
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kuromiichi.damprep.ui.common.AppScreen
import dev.kuromiichi.damprep.ui.common.LoadingScreen
import dev.kuromiichi.damprep.ui.common.LocalKeyboardFocusVisibility
import dev.kuromiichi.damprep.ui.common.OperatingSystemDropdown
import dev.kuromiichi.damprep.ui.common.verticalFadingEdges
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private val LessonContentMaxWidth = 1240.dp
internal val LessonCompactBreakpoint = 860.dp
internal val LessonVisualMaxWidth = 420.dp
internal val LessonCompactVisualMaxWidth = 440.dp
internal val LessonRichVisualMaxWidth = 560.dp
internal val LessonCompactRichVisualMaxWidth = 560.dp
internal val LessonScreenshotVisualMaxWidth = 720.dp
internal val LessonCompactScreenshotVisualMaxWidth = 640.dp
internal val LessonScrollFadeHeight = 48.dp
private val LessonArrowScrollStep = 96.dp
private val LessonPageScrollStep = 400.dp

/** Lo que tarda en animarse la pista que se acaba de mostrar antes de llevarla a la vista. */
private const val HINT_REVEAL_DELAY_MILLIS = 220L

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
    val coroutineScope = rememberCoroutineScope()
    val scrollState =
        key(
            lessonContent.lessonId,
            uiState.currentStepIndex,
            uiState.isCompletionVisible,
            targetOperatingSystem,
        ) {
            rememberScrollState()
        }

    val scrollController = remember { LessonStepScrollController() }
    val keyboardFocusVisibility = LocalKeyboardFocusVisibility.current
    val density = LocalDensity.current

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
                    if (event.isCtrlPressed || event.isMetaPressed || event.isAltPressed) {
                        return@onPreviewKeyEvent false
                    }
                    // Tras usar el ratón, la primera tecla que mueve la zona resaltada solo vuelve a mostrarla.
                    val movesHighlightedSection =
                        event.key in
                            setOf(Key.DirectionUp, Key.DirectionDown, Key.PageUp, Key.PageDown, Key.Spacebar, Key.Tab)
                    if (movesHighlightedSection && !keyboardFocusVisibility.reveal() && scrollController.focusedSection != null) {
                        return@onPreviewKeyEvent true
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

                        Key.DirectionUp, Key.DirectionDown, Key.PageUp, Key.PageDown -> {
                            val distance =
                                with(density) {
                                    if (event.key == Key.DirectionUp || event.key == Key.DirectionDown) {
                                        LessonArrowScrollStep.toPx()
                                    } else {
                                        LessonPageScrollStep.toPx()
                                    }
                                }
                            val forward = event.key == Key.DirectionDown || event.key == Key.PageDown
                            val target = scrollController.scrollTarget(scrollState)
                            if (target != null) {
                                coroutineScope.launch { target.animateScrollBy(if (forward) distance else -distance) }
                            }
                            true
                        }

                        // Tab solo alterna entre las zonas que se pueden desplazar; si no hay ninguna, no hace nada.
                        Key.Tab -> {
                            scrollController.cycle(backwards = event.isShiftPressed)
                            true
                        }

                        // Espacio baja hasta el final de la zona activa o, en los pasos con práctica, hasta su tarjeta.
                        Key.Spacebar -> {
                            val target = scrollController.scrollTarget(scrollState)
                            if (target != null) {
                                coroutineScope.launch { target.animateScrollTo(target.maxValue) }
                            }
                            true
                        }

                        Key.P -> {
                            val revealed = scrollController.revealNextHint()
                            if (revealed) {
                                coroutineScope.launch {
                                    delay(HINT_REVEAL_DELAY_MILLIS)
                                    scrollState.animateScrollTo(scrollState.maxValue)
                                }
                            }
                            revealed
                        }

                        Key.Enter, Key.NumPadEnter ->
                            when {
                                uiState.isCompletionVisible -> {
                                    viewModel.finishLesson()
                                    true
                                }

                                !uiState.isCurrentPracticeConfirmed -> {
                                    viewModel.setCurrentPracticeConfirmed(true)
                                    true
                                }

                                uiState.canShowCompletion -> {
                                    goToNextStep()
                                    true
                                }

                                else -> false
                            }

                        // Escape sale de la lección aunque se esté en la pantalla final.
                        Key.Escape -> {
                            onBack()
                            true
                        }

                        else -> false
                    }
                }.focusable(),
        takesFocus = false,
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
            BoxWithConstraints(
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
                // El alto visible del paso: en el diseño ancho, el texto y el visual no pasan de él y se desplazan por separado.
                val viewportHeight = (maxHeight - LessonScrollFadeHeight).coerceAtLeast(0.dp)
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
                                viewportHeight = viewportHeight,
                                scrollController = scrollController,
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
                    onShowPendingPractice =
                        if (uiState.isCurrentPracticeConfirmed) {
                            null
                        } else {
                            // La práctica cierra siempre el paso: al final queda su casilla de confirmación.
                            { coroutineScope.launch { scrollState.animateScrollTo(scrollState.maxValue) } }
                        },
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
