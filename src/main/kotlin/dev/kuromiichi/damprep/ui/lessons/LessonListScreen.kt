package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.lessons.LessonCatalog
import dev.kuromiichi.damprep.ui.common.AppScreen
import dev.kuromiichi.damprep.ui.common.AppScreenContentMaxWidth

@Composable
fun LessonListScreen(onBack: () -> Unit) {
    AppScreen(onBack = onBack) {
        Column(
            modifier = Modifier
                .widthIn(max = AppScreenContentMaxWidth)
                .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Todas las lecciones",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(24.dp))
            LessonCatalog.lessons.forEachIndexed { index, lesson ->
                Text(
                    text = "${index + 1}. ${lesson.title}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
