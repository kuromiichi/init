package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.lessons.LessonCatalog

@Composable
fun LessonListScreen(onBack: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .widthIn(max = 920.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                TextButton(onClick = onBack) {
                    Text("Volver")
                }
            }

            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
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
}
