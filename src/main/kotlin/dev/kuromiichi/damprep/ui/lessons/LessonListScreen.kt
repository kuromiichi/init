package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.lessons.SubjectArea
import dev.kuromiichi.damprep.ui.common.AppScreen
import dev.kuromiichi.damprep.ui.common.AppScreenContentMaxWidth

@Composable
fun LessonListScreen(
    lessons: List<LessonListItemUiModel>,
    onBack: () -> Unit,
    onLessonSelected: (String) -> Unit,
) {
    AppScreen(onBack = onBack) {
        Column(
            modifier = Modifier
                .widthIn(max = AppScreenContentMaxWidth)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Todas las lecciones",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(24.dp))
            lessons.forEach { lesson ->
                LessonListItem(
                    lesson = lesson,
                    onClick = { onLessonSelected(lesson.id) },
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun LessonListItem(
    lesson: LessonListItemUiModel,
    onClick: () -> Unit,
) {
    val contentColor = if (lesson.isUnlocked) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val containerColor = if (lesson.isUnlocked) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (lesson.isUnlocked) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            ),
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SubjectTag(subjectArea = lesson.subjectArea)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Lección ${lesson.index + 1}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                )
            }
            Icon(
                imageVector = if (lesson.isUnlocked) Icons.Filled.PlayArrow else Icons.Filled.Lock,
                contentDescription = if (lesson.isUnlocked) "Abrir lección" else "Lección bloqueada",
                tint = if (lesson.isUnlocked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
            )
        }
    }
}

@Composable
private fun SubjectTag(subjectArea: SubjectArea) {
    val color = subjectArea.accentColor()

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color,
        contentColor = color.readableContentColor(),
    ) {
        Text(
            text = subjectArea.label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
        )
    }
}

private fun SubjectArea.accentColor(): Color =
    when (this) {
        SubjectArea.Mixed -> Color(0xFFF5BDE6)
        SubjectArea.Programming -> Color(0xFFA6DA95)
        SubjectArea.DevelopmentEnvironments -> Color(0xFF8AADF4)
    }

private fun Color.readableContentColor(): Color =
    if (luminance() > 0.5f) {
        Color(0xFF181926)
    } else {
        Color.White
    }
