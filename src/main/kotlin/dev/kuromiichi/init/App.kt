package dev.kuromiichi.init

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dev.kuromiichi.init.ui.AppViewModel
import dev.kuromiichi.init.ui.common.LoadingScreen
import dev.kuromiichi.init.ui.navigation.AppNavHost
import dev.kuromiichi.init.ui.theme.InitTheme
import dev.kuromiichi.init.ui.window.WindowSizeGuard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    titleBarInset: Dp = 0.dp,
    onExitRequest: () -> Unit = {},
    appViewModel: AppViewModel = koinViewModel(),
) {
    val navController = rememberNavController()
    val uiState by appViewModel.uiState.collectAsStateWithLifecycle()
    val isRestartRequested by appViewModel.isRestartRequested.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { appViewModel.onAppStarted() }

    // El instalador de la actualización espera a que la app se cierre para sustituirla y volver a abrirla.
    LaunchedEffect(isRestartRequested) {
        if (isRestartRequested) onExitRequest()
    }

    InitTheme(themePreference = uiState.themePreference) {
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
