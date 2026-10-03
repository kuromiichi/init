package dev.kuromiichi.damprep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dev.kuromiichi.damprep.ui.AppViewModel
import dev.kuromiichi.damprep.ui.common.LoadingScreen
import dev.kuromiichi.damprep.ui.navigation.AppNavHost
import dev.kuromiichi.damprep.ui.theme.DamPrepTheme
import dev.kuromiichi.damprep.ui.window.WindowSizeGuard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    titleBarInset: Dp = 0.dp,
    appViewModel: AppViewModel = koinViewModel(),
) {
    val navController = rememberNavController()
    val uiState by appViewModel.uiState.collectAsStateWithLifecycle()

    DamPrepTheme(themePreference = uiState.themePreference) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = titleBarInset),
        ) {
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
}
