package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.lessons.SubjectArea

@Composable
internal fun SubjectAreaTag(subjectArea: SubjectArea) {
    val color = subjectArea.accentColor()

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.22f),
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Text(
            text = subjectArea.label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
        )
    }
}

@Composable
private fun SubjectArea.accentColor(): Color =
    when (this) {
        SubjectArea.Mixed -> MaterialTheme.colorScheme.secondary
        SubjectArea.Programming -> MaterialTheme.colorScheme.tertiary
        SubjectArea.DevelopmentEnvironments -> MaterialTheme.colorScheme.primary
    }
