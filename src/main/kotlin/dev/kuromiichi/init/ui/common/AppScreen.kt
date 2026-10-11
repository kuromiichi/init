package dev.kuromiichi.init.ui.common

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val AppScreenContentMaxWidth = 760.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    contentMaxWidth: Dp = AppScreenContentMaxWidth,
    topBarTitle: String? = null,
    topBarActions: @Composable RowScope.() -> Unit = {},
    takesFocus: Boolean = true,
    arrowKeyNavigation: Boolean = false,
    horizontalArrowKeys: Boolean = true,
    isEditingText: () -> Boolean = { false },
    content: @Composable ColumnScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val rootFocusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var isRootFocused by remember { mutableStateOf(false) }
    if (takesFocus) {
        // Sin nada enfocado las teclas no llegan a la pantalla: cuando nada lo tiene (al entrar, o al salir de un campo
        // con Escape), la pantalla lo recupera. Si ya ha enfocado algo suyo, como el primer botón de un menú, se respeta.
        LaunchedEffect(hasFocus) {
            if (!hasFocus) {
                // Hasta que la pantalla se ha colocado el elemento aún no admite foco.
                withFrameNanos { }
                if (!hasFocus) runCatching { rootFocusRequester.requestFocus() }
            }
        }
    }

    Surface(
        modifier =
            modifier
                .onPreviewKeyEvent { event ->
                    // Escape vuelve a la pantalla anterior; antes se suelta el foco para que un campo en edición se guarde.
                    if (event.type == KeyEventType.KeyDown && event.key == Key.Escape) {
                        focusManager.clearFocus()
                        onBack()
                        true
                    } else {
                        false
                    }
                }.then(
                    if (arrowKeyNavigation) {
                        Modifier.arrowKeyNavigation(
                            horizontal = horizontalArrowKeys,
                            isRootFocused = { isRootFocused },
                            isEditingText = isEditingText,
                        )
                    } else {
                        Modifier
                    },
                ).onFocusChanged {
                    hasFocus = it.hasFocus
                    isRootFocused = it.isFocused
                }.then(if (takesFocus) Modifier.focusRequester(rootFocusRequester).focusable() else Modifier)
                .fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                modifier =
                    Modifier
                        .widthIn(max = contentMaxWidth)
                        .fillMaxWidth(),
                title = {
                    if (topBarTitle != null) {
                        Text(
                            text = topBarTitle,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        // Con teclado se vuelve con Escape; así las flechas y Tab empiezan en el contenido.
                        modifier = Modifier.focusProperties { canFocus = false }.hoverScale(),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
                actions = topBarActions,
                expandedHeight = 48.dp,
                windowInsets = WindowInsets(0.dp),
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            )

            content()
        }
    }
}
