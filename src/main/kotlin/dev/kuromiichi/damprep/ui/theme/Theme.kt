package dev.kuromiichi.damprep.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DamPrepLightColorScheme: ColorScheme = lightColorScheme(
    primary = CatppuccinLatte.blue,
    onPrimary = CatppuccinLatte.base,
    primaryContainer = CatppuccinLatte.surface0,
    onPrimaryContainer = CatppuccinLatte.blue,
    secondary = CatppuccinLatte.mauve,
    onSecondary = CatppuccinLatte.base,
    secondaryContainer = CatppuccinLatte.surface1,
    onSecondaryContainer = CatppuccinLatte.mauve,
    tertiary = CatppuccinLatte.peach,
    onTertiary = CatppuccinLatte.base,
    tertiaryContainer = CatppuccinLatte.surface1,
    onTertiaryContainer = CatppuccinLatte.peach,
    error = CatppuccinLatte.red,
    onError = CatppuccinLatte.base,
    errorContainer = CatppuccinLatte.flamingo,
    onErrorContainer = CatppuccinLatte.text,
    background = CatppuccinLatte.base,
    onBackground = CatppuccinLatte.text,
    surface = CatppuccinLatte.base,
    onSurface = CatppuccinLatte.text,
    surfaceVariant = CatppuccinLatte.mantle,
    onSurfaceVariant = CatppuccinLatte.subtext0,
    outline = CatppuccinLatte.overlay0,
    outlineVariant = CatppuccinLatte.surface1,
    inverseSurface = CatppuccinLatte.text,
    inverseOnSurface = CatppuccinLatte.base,
    inversePrimary = CatppuccinLatte.lavender,
    surfaceTint = CatppuccinLatte.blue,
)

private val DamPrepDarkColorScheme: ColorScheme = darkColorScheme(
    primary = CatppuccinMacchiato.blue,
    onPrimary = CatppuccinMacchiato.crust,
    primaryContainer = CatppuccinMacchiato.surface0,
    onPrimaryContainer = CatppuccinMacchiato.blue,
    secondary = CatppuccinMacchiato.mauve,
    onSecondary = CatppuccinMacchiato.crust,
    secondaryContainer = CatppuccinMacchiato.surface0,
    onSecondaryContainer = CatppuccinMacchiato.mauve,
    tertiary = CatppuccinMacchiato.peach,
    onTertiary = CatppuccinMacchiato.crust,
    tertiaryContainer = CatppuccinMacchiato.surface0,
    onTertiaryContainer = CatppuccinMacchiato.peach,
    error = CatppuccinMacchiato.red,
    onError = CatppuccinMacchiato.crust,
    errorContainer = CatppuccinMacchiato.surface0,
    onErrorContainer = CatppuccinMacchiato.red,
    background = CatppuccinMacchiato.base,
    onBackground = CatppuccinMacchiato.text,
    surface = CatppuccinMacchiato.base,
    onSurface = CatppuccinMacchiato.text,
    surfaceVariant = CatppuccinMacchiato.mantle,
    onSurfaceVariant = CatppuccinMacchiato.subtext0,
    outline = CatppuccinMacchiato.overlay0,
    outlineVariant = CatppuccinMacchiato.surface1,
    inverseSurface = CatppuccinMacchiato.text,
    inverseOnSurface = CatppuccinMacchiato.base,
    inversePrimary = CatppuccinMacchiato.blue,
    surfaceTint = CatppuccinMacchiato.blue,
)

@Composable
fun DamPrepTheme(
    themePreference: ThemePreference,
    content: @Composable () -> Unit,
) {
    val useDarkTheme = when (themePreference) {
        ThemePreference.System -> isSystemInDarkTheme()
        ThemePreference.Light -> false
        ThemePreference.Dark -> true
    }

    MaterialTheme(
        colorScheme = if (useDarkTheme) DamPrepDarkColorScheme else DamPrepLightColorScheme,
        typography = DamPrepTypography,
        content = content,
    )
}
