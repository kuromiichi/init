package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.lessons.LessonCheckpoint
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem

@Composable
internal fun LessonStepContent(
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
            val visualMaxWidth =
                when {
                    isScreenshotVisual -> LessonScreenshotVisualMaxWidth
                    hasRichVisual -> LessonRichVisualMaxWidth
                    else -> LessonVisualMaxWidth
                }
            val compactVisualMaxWidth =
                when {
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
                        modifier =
                            Modifier
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
                        modifier =
                            Modifier.weight(
                                when {
                                    isScreenshotVisual -> 0.85f
                                    hasRichVisual -> 1f
                                    else -> 1.25f
                                },
                            ),
                    )
                    Box(
                        modifier =
                            Modifier.weight(
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
                            modifier =
                                Modifier
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
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (checked) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                contentColor =
                    if (checked) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            ),
    ) {
        Row(
            modifier =
                Modifier
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
                    text =
                        if (isLessonCompletion) {
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
