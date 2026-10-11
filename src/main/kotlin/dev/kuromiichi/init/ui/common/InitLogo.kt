package dev.kuromiichi.init.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import dev.kuromiichi.init.ui.theme.jetBrainsMono

/** Logotipo de init: el `>` de un prompt de terminal, en mauve, seguido del nombre en JetBrains Mono. */
@Composable
fun InitLogo(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.displayMedium,
) {
    Text(
        text =
            buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append(">")
                }
                append(" init")
            },
        style = style.copy(fontFamily = jetBrainsMono(), fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onBackground,
        // Los lectores de pantalla leen el nombre, no el símbolo del prompt.
        modifier =
            modifier.clearAndSetSemantics {
                contentDescription = "init"
                heading()
            },
    )
}
