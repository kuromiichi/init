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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuromiichi.dam_prep.generated.resources.Res
import dev.kuromiichi.dam_prep.generated.resources.intellij_idea
import dev.kuromiichi.dam_prep.generated.resources.kotlin
import dev.kuromiichi.damprep.data.lessons.LessonContentCatalog
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.ui.common.AppScreen
import dev.kuromiichi.damprep.ui.common.AppScreenContentMaxWidth
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val LessonContentMaxWidth = 1240.dp
private val LessonCompactBreakpoint = 860.dp
private val LessonVisualMaxWidth = 420.dp
private val LessonCompactVisualMaxWidth = 440.dp
private const val KotlinIntellijVisualTitle = "Kotlin e IntelliJ IDEA"

@Composable
fun CurrentLessonScreen(onBack: () -> Unit) {
    val lessonContent = LessonContentCatalog.currentLesson
    var currentStepIndex by remember { mutableIntStateOf(0) }
    val isFirstStep = currentStepIndex == 0
    val isLastStep = currentStepIndex == lessonContent.steps.lastIndex

    AppScreen(
        onBack = onBack,
        contentMaxWidth = LessonContentMaxWidth,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = LessonContentMaxWidth)
                .fillMaxWidth()
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
                        targetState = currentStepIndex,
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
                currentStepIndex = currentStepIndex,
                stepCount = lessonContent.steps.size,
                canGoBack = !isFirstStep,
                canGoForward = !isLastStep,
                onPrevious = {
                    if (!isFirstStep) {
                        currentStepIndex -= 1
                    }
                },
                onNext = {
                    if (!isLastStep) {
                        currentStepIndex += 1
                    }
                },
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

            if (useCompactLayout) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    LessonText(step.body)
                    LessonVisual(
                        step = step,
                        modifier = Modifier
                            .widthIn(max = LessonCompactVisualMaxWidth)
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
                        modifier = Modifier.weight(1.25f),
                    )
                    Box(
                        modifier = Modifier.weight(0.75f),
                        contentAlignment = Alignment.Center,
                    ) {
                        LessonVisual(
                            step = step,
                            modifier = Modifier
                                .widthIn(max = LessonVisualMaxWidth)
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
    Surface(
        modifier = modifier.aspectRatio(1.25f),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        if (step.visualTitle == KotlinIntellijVisualTitle) {
            ToolLogosVisual()
            return@Surface
        }

        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier.size(76.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(38.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = step.visualTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = step.visualBody,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ToolLogosVisual() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ToolLogoCard(
                title = "Kotlin",
                subtitle = "Lenguaje",
                logo = Res.drawable.kotlin,
                modifier = Modifier.weight(1f),
            )
            ToolLogoCard(
                title = "IntelliJ IDEA",
                subtitle = "Entorno",
                logo = Res.drawable.intellij_idea,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ToolLogoCard(
    title: String,
    subtitle: String,
    logo: DrawableResource,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(logo),
                contentDescription = title,
                modifier = Modifier.size(72.dp),
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
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
