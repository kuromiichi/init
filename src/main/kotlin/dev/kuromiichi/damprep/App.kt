package dev.kuromiichi.damprep

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dev.kuromiichi.damprep.ui.AppViewModel
import dev.kuromiichi.damprep.ui.common.LoadingScreen
import dev.kuromiichi.damprep.ui.navigation.AppNavHost
import dev.kuromiichi.damprep.ui.theme.DamPrepTheme
import dev.kuromiichi.damprep.ui.window.WindowSizeGuard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(appViewModel: AppViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val uiState by appViewModel.uiState.collectAsStateWithLifecycle()

    DamPrepTheme(themePreference = uiState.themePreference) {
        WindowSizeGuard {
            if (uiState.isLoading) {
                LoadingScreen()
            } else {
                AppNavHost(
                    navController = navController,
                    startDestination = uiState.startDestination,
                )
            }
        }
    }
}
