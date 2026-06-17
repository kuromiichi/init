package dev.kuromiichi.damprep

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import dev.kuromiichi.damprep.ui.navigation.AppNavHost
import dev.kuromiichi.damprep.ui.theme.DamPrepTheme
import dev.kuromiichi.damprep.ui.theme.ThemePreference

@Composable
fun App() {
    val navController = rememberNavController()
    var themePreference by remember { mutableStateOf(ThemePreference.System) }
    var studentName by remember { mutableStateOf("") }

    DamPrepTheme(themePreference = themePreference) {
        AppNavHost(
            navController = navController,
            studentName = studentName,
            themePreference = themePreference,
            onThemePreferenceChanged = { themePreference = it },
            onStudentNameReceived = { studentName = it },
        )
    }
}
