package dev.kuromiichi.init.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import dev.kuromiichi.init.data.preferences.ThemePreference

/** Proporción de acento mezclada sobre la base para los contenedores tintados. */
private const val CONTAINER_TINT = 0.18f

private fun CatppuccinFlavor.tinted(accent: Color): Color = lerp(base, accent, CONTAINER_TINT)

private fun latteColorScheme(flavor: CatppuccinFlavor): ColorScheme =
    lightColorScheme(
        primary = flavor.blue,
        onPrimary = flavor.base,
        primaryContainer = flavor.tinted(flavor.blue),
        onPrimaryContainer = flavor.text,
        secondary = flavor.mauve,
        onSecondary = flavor.base,
        secondaryContainer = flavor.tinted(flavor.mauve),
        onSecondaryContainer = flavor.text,
        tertiary = flavor.peach,
        onTertiary = flavor.text,
        tertiaryContainer = flavor.tinted(flavor.peach),
        onTertiaryContainer = flavor.text,
        error = flavor.red,
        onError = flavor.base,
        errorContainer = flavor.tinted(flavor.red),
        onErrorContainer = flavor.text,
        background = flavor.base,
        onBackground = flavor.text,
        surface = flavor.base,
        onSurface = flavor.text,
        surfaceVariant = flavor.mantle,
        onSurfaceVariant = flavor.subtext1,
        outline = flavor.overlay2,
        outlineVariant = flavor.surface1,
        inverseSurface = flavor.text,
        inverseOnSurface = flavor.base,
        inversePrimary = flavor.lavender,
        surfaceTint = flavor.blue,
        scrim = flavor.crust,
        surfaceBright = flavor.base,
        surfaceDim = flavor.crust,
        // Los contenedores altos se quedan claros: el azul y el rojo de los botones de texto
        // de los diálogos solo alcanzan un buen contraste sobre base o mantle.
        surfaceContainerLowest = flavor.base,
        surfaceContainerLow = flavor.mantle,
        surfaceContainer = flavor.mantle,
        surfaceContainerHigh = flavor.base,
        surfaceContainerHighest = flavor.mantle,
    )

private fun macchiatoColorScheme(flavor: CatppuccinFlavor): ColorScheme =
    darkColorScheme(
        primary = flavor.blue,
        onPrimary = flavor.crust,
        primaryContainer = flavor.tinted(flavor.blue),
        onPrimaryContainer = flavor.text,
        secondary = flavor.mauve,
        onSecondary = flavor.crust,
        secondaryContainer = flavor.tinted(flavor.mauve),
        onSecondaryContainer = flavor.text,
        tertiary = flavor.peach,
        onTertiary = flavor.crust,
        tertiaryContainer = flavor.tinted(flavor.peach),
        onTertiaryContainer = flavor.text,
        error = flavor.red,
        onError = flavor.crust,
        errorContainer = flavor.tinted(flavor.red),
        onErrorContainer = flavor.text,
        background = flavor.base,
        onBackground = flavor.text,
        surface = flavor.base,
        onSurface = flavor.text,
        surfaceVariant = flavor.surface0,
        onSurfaceVariant = flavor.subtext0,
        outline = flavor.overlay0,
        outlineVariant = flavor.surface1,
        inverseSurface = flavor.text,
        inverseOnSurface = flavor.base,
        inversePrimary = flavor.blue,
        surfaceTint = flavor.blue,
        scrim = flavor.crust,
        surfaceBright = flavor.surface1,
        surfaceDim = flavor.crust,
        surfaceContainerLowest = flavor.crust,
        surfaceContainerLow = flavor.mantle,
        surfaceContainer = flavor.base,
        surfaceContainerHigh = flavor.surface0,
        surfaceContainerHighest = flavor.surface1,
    )

internal val InitLightColorScheme: ColorScheme = latteColorScheme(CatppuccinLatte)
internal val InitDarkColorScheme: ColorScheme = macchiatoColorScheme(CatppuccinMacchiato)

/** Paleta Catppuccin del tema activo, para colores de acento que Material 3 no modela (código, etiquetas, capturas). */
val LocalCatppuccinFlavor = staticCompositionLocalOf { CatppuccinMacchiato }

@Composable
fun InitTheme(
    themePreference: ThemePreference,
    content: @Composable () -> Unit,
) {
    val useDarkTheme =
        when (themePreference) {
            ThemePreference.System -> isSystemInDarkTheme()
            ThemePreference.Light -> false
            ThemePreference.Dark -> true
        }

    CompositionLocalProvider(
        LocalCatppuccinFlavor provides if (useDarkTheme) CatppuccinMacchiato else CatppuccinLatte,
    ) {
        MaterialTheme(
            colorScheme = if (useDarkTheme) InitDarkColorScheme else InitLightColorScheme,
            typography = InitTypography,
            content = content,
        )
    }
}
