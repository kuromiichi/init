package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.ui.common.AppScreenContentMaxWidth
import dev.kuromiichi.damprep.ui.common.hoverScale

@Composable
internal fun LessonStepNavigation(
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
        modifier =
            Modifier
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
        modifier =
            Modifier.progressSemantics(
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
                targetValue =
                    if (isCurrent) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                animationSpec = tween(durationMillis = 240),
                label = "Lesson dot color",
            )
            val dotWidth by animateDpAsState(
                targetValue = if (isCurrent) 24.dp else 8.dp,
                animationSpec =
                    tween(
                        durationMillis = 260,
                        easing = FastOutSlowInEasing,
                    ),
                label = "Lesson dot width",
            )

            Box(
                modifier =
                    Modifier
                        .size(height = 8.dp, width = dotWidth)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(dotColor),
            )
        }
    }
}
