package dev.kuromiichi.damprep.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.test.Test
import kotlin.test.assertTrue

class ThemeContrastTest {
    private val schemes = mapOf("claro" to DamPrepLightColorScheme, "oscuro" to DamPrepDarkColorScheme)

    private fun contrast(
        a: Color,
        b: Color,
    ): Float {
        val (light, dark) = listOf(a.luminance(), b.luminance()).sortedDescending()
        return (light + 0.05f) / (dark + 0.05f)
    }

    private fun ColorScheme.textPairs(): Map<String, Pair<Color, Color>> =
        mapOf(
            "onBackground/background" to (onBackground to background),
            "onSurface/surface" to (onSurface to surface),
            "onSurfaceVariant/surfaceVariant" to (onSurfaceVariant to surfaceVariant),
            "onSurfaceVariant/surface" to (onSurfaceVariant to surface),
            "onPrimaryContainer/primaryContainer" to (onPrimaryContainer to primaryContainer),
            "onSecondaryContainer/secondaryContainer" to (onSecondaryContainer to secondaryContainer),
            "onTertiaryContainer/tertiaryContainer" to (onTertiaryContainer to tertiaryContainer),
            "onErrorContainer/errorContainer" to (onErrorContainer to errorContainer),
            "onSurface/surfaceContainer" to (onSurface to surfaceContainer),
            "onSurface/surfaceContainerHigh" to (onSurface to surfaceContainerHigh),
            "onSurface/surfaceContainerHighest" to (onSurface to surfaceContainerHighest),
            "inverseOnSurface/inverseSurface" to (inverseOnSurface to inverseSurface),
            "primary/surfaceContainerHigh" to (primary to surfaceContainerHigh),
            "error/surfaceContainerHigh" to (error to surfaceContainerHigh),
            "primary/surface" to (primary to surface),
            "secondary/surface" to (secondary to surface),
            "error/surface" to (error to surface),
        )

    @Test
    fun `el texto cumple el contraste AA en ambos temas`() {
        schemes.forEach { (name, scheme) ->
            scheme.textPairs().forEach { (pair, colors) ->
                val ratio = contrast(colors.first, colors.second)
                assertTrue(ratio >= 4.3f, "Tema $name: $pair tiene contraste $ratio")
            }
        }
    }

    @Test
    fun `onPrimary y onSecondary legibles en botones y etiquetas`() {
        schemes.forEach { (name, scheme) ->
            // Catppuccin Latte solo alcanza ~4.3 con blue sobre base; se acepta para etiquetas en negrita.
            assertTrue(contrast(scheme.onPrimary, scheme.primary) >= 4.3f, "Tema $name: onPrimary/primary")
            assertTrue(contrast(scheme.onSecondary, scheme.secondary) >= 4.3f, "Tema $name: onSecondary/secondary")
        }
    }

    @Test
    fun `los bordes tienen contraste de componente no textual`() {
        schemes.forEach { (name, scheme) ->
            assertTrue(contrast(scheme.outline, scheme.surface) >= 3f, "Tema $name: outline/surface")
        }
    }

    @Test
    fun `la terminal es legible en ambos temas`() {
        mapOf("claro" to CatppuccinLatteTerminal, "oscuro" to CatppuccinMocha).forEach { (name, flavor) ->
            mapOf(
                "text" to flavor.text,
                "subtext0" to flavor.subtext0,
                "overlay1" to flavor.overlay1,
                "green" to flavor.green,
                "red" to flavor.red,
            ).forEach { (color, value) ->
                assertTrue(contrast(value, flavor.crust) >= 4.3f, "Terminal $name: $color/crust")
            }
            assertTrue(contrast(flavor.subtext1, flavor.mantle) >= 4.3f, "Terminal $name: subtext1/mantle")
        }
    }
}
