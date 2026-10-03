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
import dev.kuromiichi.damprep.ui.theme.LocalCatppuccinFlavor

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
private fun SubjectArea.accentColor(): Color {
    val flavor = LocalCatppuccinFlavor.current
    return when (this) {
        SubjectArea.Mixed -> flavor.mauve
        SubjectArea.Programming -> flavor.peach
        SubjectArea.DevelopmentEnvironments -> flavor.blue
        SubjectArea.ComputerSystems -> flavor.teal
        SubjectArea.MarkupLanguages -> flavor.pink
        SubjectArea.Databases -> flavor.green
    }
}
