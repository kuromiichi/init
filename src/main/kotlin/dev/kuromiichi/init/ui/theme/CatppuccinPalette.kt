package dev.kuromiichi.init.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

data class CatppuccinFlavor(
    val rosewater: Color,
    val flamingo: Color,
    val pink: Color,
    val mauve: Color,
    val red: Color,
    val maroon: Color,
    val peach: Color,
    val yellow: Color,
    val green: Color,
    val teal: Color,
    val sky: Color,
    val sapphire: Color,
    val blue: Color,
    val lavender: Color,
    val text: Color,
    val subtext1: Color,
    val subtext0: Color,
    val overlay2: Color,
    val overlay1: Color,
    val overlay0: Color,
    val surface2: Color,
    val surface1: Color,
    val surface0: Color,
    val base: Color,
    val mantle: Color,
    val crust: Color,
)

val CatppuccinLatte =
    CatppuccinFlavor(
        rosewater = Color(0xFFDC8A78),
        flamingo = Color(0xFFDD7878),
        pink = Color(0xFFEA76CB),
        mauve = Color(0xFF8839EF),
        red = Color(0xFFD20F39),
        maroon = Color(0xFFE64553),
        peach = Color(0xFFFE640B),
        yellow = Color(0xFFDF8E1D),
        green = Color(0xFF40A02B),
        teal = Color(0xFF179299),
        sky = Color(0xFF04A5E5),
        sapphire = Color(0xFF209FB5),
        blue = Color(0xFF1E66F5),
        lavender = Color(0xFF7287FD),
        text = Color(0xFF4C4F69),
        subtext1 = Color(0xFF5C5F77),
        subtext0 = Color(0xFF6C6F85),
        overlay2 = Color(0xFF7C7F93),
        overlay1 = Color(0xFF8C8FA1),
        overlay0 = Color(0xFF9CA0B0),
        surface2 = Color(0xFFACB0BE),
        surface1 = Color(0xFFBCC0CC),
        surface0 = Color(0xFFCCD0DA),
        base = Color(0xFFEFF1F5),
        mantle = Color(0xFFE6E9EF),
        crust = Color(0xFFDCE0E8),
    )

val CatppuccinMacchiato =
    CatppuccinFlavor(
        rosewater = Color(0xFFF4DBD6),
        flamingo = Color(0xFFF0C6C6),
        pink = Color(0xFFF5BDE6),
        mauve = Color(0xFFC6A0F6),
        red = Color(0xFFED8796),
        maroon = Color(0xFFEE99A0),
        peach = Color(0xFFF5A97F),
        yellow = Color(0xFFEED49F),
        green = Color(0xFFA6DA95),
        teal = Color(0xFF8BD5CA),
        sky = Color(0xFF91D7E3),
        sapphire = Color(0xFF7DC4E4),
        blue = Color(0xFF8AADF4),
        lavender = Color(0xFFB7BDF8),
        text = Color(0xFFCAD3F5),
        subtext1 = Color(0xFFB8C0E0),
        subtext0 = Color(0xFFA5ADCB),
        overlay2 = Color(0xFF939AB7),
        overlay1 = Color(0xFF8087A2),
        overlay0 = Color(0xFF6E738D),
        surface2 = Color(0xFF5B6078),
        surface1 = Color(0xFF494D64),
        surface0 = Color(0xFF363A4F),
        base = Color(0xFF24273A),
        mantle = Color(0xFF1E2030),
        crust = Color(0xFF181926),
    )

/** Solo se usa en la terminal con tema oscuro, la «pantalla negra»; el resto de la app usa Macchiato. */
val CatppuccinMocha =
    CatppuccinFlavor(
        rosewater = Color(0xFFF5E0DC),
        flamingo = Color(0xFFF2CDCD),
        pink = Color(0xFFF5C2E7),
        mauve = Color(0xFFCBA6F7),
        red = Color(0xFFF38BA8),
        maroon = Color(0xFFEBA0AC),
        peach = Color(0xFFFAB387),
        yellow = Color(0xFFF9E2AF),
        green = Color(0xFFA6E3A1),
        teal = Color(0xFF94E2D5),
        sky = Color(0xFF89DCEB),
        sapphire = Color(0xFF74C7EC),
        blue = Color(0xFF89B4FA),
        lavender = Color(0xFFB4BEFE),
        text = Color(0xFFCDD6F4),
        subtext1 = Color(0xFFBAC2DE),
        subtext0 = Color(0xFFA6ADC8),
        overlay2 = Color(0xFF9399B2),
        overlay1 = Color(0xFF7F849C),
        overlay0 = Color(0xFF6C7086),
        surface2 = Color(0xFF585B70),
        surface1 = Color(0xFF45475A),
        surface0 = Color(0xFF313244),
        base = Color(0xFF1E1E2E),
        mantle = Color(0xFF181825),
        crust = Color(0xFF11111B),
    )

/**
 * Latte para la terminal clara. Su fondo es `crust`, más oscuro que el de los bloques de código, y con él el
 * verde de la entrada y los grises secundarios de Latte no llegan al contraste mínimo: el verde y el rojo se
 * acercan al texto y los grises secundarios suben a subtext1, sin salir de la paleta.
 */
internal val CatppuccinLatteTerminal =
    CatppuccinLatte.copy(
        green = lerp(CatppuccinLatte.green, CatppuccinLatte.text, 0.65f),
        red = lerp(CatppuccinLatte.red, CatppuccinLatte.text, 0.2f),
        subtext0 = CatppuccinLatte.subtext1,
        overlay1 = CatppuccinLatte.subtext1,
    )
