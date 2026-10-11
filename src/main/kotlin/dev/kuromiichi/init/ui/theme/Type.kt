package dev.kuromiichi.init.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.kuromiichi.init.generated.resources.Res
import dev.kuromiichi.init.generated.resources.jetbrains_mono_italic
import dev.kuromiichi.init.generated.resources.jetbrains_mono_medium
import dev.kuromiichi.init.generated.resources.jetbrains_mono_regular
import dev.kuromiichi.init.generated.resources.jetbrains_mono_semi_bold
import org.jetbrains.compose.resources.Font

val InitTypography = Typography()

/**
 * Fuente del código y la consola: JetBrains Mono en su variante sin ligaduras (NL), para que cada símbolo
 * se vea tal como se escribe (`!=`, `->`, `>=`), y la misma en macOS, Windows y Linux.
 */
@Composable
fun jetBrainsMono(): FontFamily =
    FontFamily(
        Font(Res.font.jetbrains_mono_regular, FontWeight.Normal),
        Font(Res.font.jetbrains_mono_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.jetbrains_mono_medium, FontWeight.Medium),
        Font(Res.font.jetbrains_mono_semi_bold, FontWeight.SemiBold),
    )
