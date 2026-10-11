package dev.kuromiichi.init.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class LearningToolCard(
    val title: String,
    val subtitle: String,
    val logo: DrawableResource,
)

data class LearningCodeExample(
    /** Nombre del archivo que muestra la pestaña del editor. */
    val fileName: String,
    /** Leyenda opcional sobre el código, p. ej. la ruta del archivo o qué ilustra el ejemplo. */
    val title: String?,
    val lines: List<LearningCodeLine>,
    val note: AnnotatedString,
    /** Texto que copia el botón del ejemplo; si es nulo, el ejemplo no se puede copiar. */
    val copyText: String? = null,
)

data class LearningTraceTable(
    val title: String,
    val columns: List<String>,
    val rows: List<List<String>>,
)

data class LearningConsoleRun(
    val lines: List<LearningConsoleLine>,
)

data class LearningConsoleLine(
    val text: String,
    val isInput: Boolean = false,
    val isError: Boolean = false,
)

data class LearningCodeLine(
    val tokens: List<LearningCodeToken>,
)

data class LearningCodeToken(
    val text: String,
    val kind: LearningCodeTokenKind = LearningCodeTokenKind.Plain,
    val hasErrorUnderline: Boolean = false,
)

enum class LearningCodeTokenKind {
    Plain,
    Keyword,
    Function,
    Number,
    Operator,
    Punctuation,
    StringLiteral,
    Interpolation,
    Comment,
    ClassName,
    Parameter,
    Constant,
    MutableVariable,
    LambdaDelimiter,
    Bracket0,
    Bracket1,
    Bracket2,
    Bracket3,
    Bracket4,
}

/** Relleno interior del panel de los visuales de las lecciones. */
val LearningVisualSurfacePadding = 28.dp

@Composable
fun LearningVisualSurface(
    modifier: Modifier = Modifier,
    contentPadding: Dp = LearningVisualSurfacePadding,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
            contentAlignment = Alignment.Center,
            content = content,
        )
    }
}

@Composable
fun IconTextLearningVisual(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.widthIn(max = 340.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(76.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.AutoStories,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Captura con el marco ajustado a su proporción, para que borde y esquinas sigan a la imagen.
 * Un clic la amplía; el visor tampoco reescala más allá de la resolución real de la captura.
 */
@Composable
fun ScreenshotLearningVisual(
    image: DrawableResource,
    description: String,
    modifier: Modifier = Modifier,
) {
    var isZoomed by remember { mutableStateOf(false) }
    val painter = painterResource(image)
    val aspectRatio = painter.intrinsicSize.width / painter.intrinsicSize.height
    // Nunca por encima de la resolución real: ampliar una captura la emborrona y descuadra su texto.
    val nativeSize = with(LocalDensity.current) { painter.intrinsicSize.toDpSize() }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier =
                Modifier
                    .sizeIn(maxWidth = nativeSize.width, maxHeight = minOf(nativeSize.height, ScreenshotMaxHeight))
                    .aspectRatio(aspectRatio)
                    .screenshotFrame()
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClickLabel = "Ampliar captura",
                        role = Role.Button,
                    ) { isZoomed = true },
        )
    }

    if (isZoomed) {
        Dialog(
            onDismissRequest = { isZoomed = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .clickable(interactionSource = null, indication = null) { isZoomed = false },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painter,
                    contentDescription = description,
                    contentScale = ContentScale.Fit,
                    modifier =
                        Modifier
                            .sizeIn(maxWidth = nativeSize.width, maxHeight = nativeSize.height)
                            .aspectRatio(aspectRatio)
                            .screenshotFrame(),
                )
            }
        }
    }
}

private val ScreenshotMaxHeight = 460.dp

@Composable
private fun Modifier.screenshotFrame(): Modifier {
    val shape = RoundedCornerShape(10.dp)
    return clip(shape).border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
}
