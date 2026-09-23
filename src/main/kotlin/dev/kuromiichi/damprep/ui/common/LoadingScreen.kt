package dev.kuromiichi.damprep.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Fondo vacío mientras llegan los datos locales, que tardan milisegundos. */
@Composable
fun LoadingScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {}
}
