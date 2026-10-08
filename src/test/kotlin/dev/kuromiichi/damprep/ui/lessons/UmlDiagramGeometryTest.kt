package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UmlDiagramGeometryTest {
    @Test
    fun `columns take the width of their widest node and rows the height of their tallest`() {
        val rects =
            umlGridRects(
                items =
                    listOf(
                        UmlGridItem(row = 0, column = 0, columnSpan = 1, size = Size(100f, 40f)),
                        UmlGridItem(row = 0, column = 1, columnSpan = 1, size = Size(60f, 80f)),
                        UmlGridItem(row = 1, column = 0, columnSpan = 1, size = Size(140f, 20f)),
                    ),
                columnGap = 10f,
                rowGap = 5f,
            )

        // La columna 0 mide 140: el primer nodo se centra en ella. La fila 0 mide 80: se centra también en alto.
        assertEquals(Rect(Offset(20f, 20f), Size(100f, 40f)), rects[0])
        assertEquals(Rect(Offset(150f, 0f), Size(60f, 80f)), rects[1])
        assertEquals(Rect(Offset(0f, 85f), Size(140f, 20f)), rects[2])
    }

    @Test
    fun `a node spanning columns is centred over them`() {
        val rects =
            umlGridRects(
                items =
                    listOf(
                        UmlGridItem(row = 0, column = 0, columnSpan = 2, size = Size(50f, 10f)),
                        UmlGridItem(row = 1, column = 0, columnSpan = 1, size = Size(100f, 10f)),
                        UmlGridItem(row = 1, column = 1, columnSpan = 1, size = Size(100f, 10f)),
                    ),
                columnGap = 20f,
                rowGap = 0f,
            )

        assertEquals(rects[1].left + (rects[2].right - rects[1].left) / 2f, rects[0].center.x)
    }

    @Test
    fun `a spanning node wider than its columns widens them evenly`() {
        val rects =
            umlGridRects(
                items =
                    listOf(
                        UmlGridItem(row = 0, column = 0, columnSpan = 2, size = Size(300f, 10f)),
                        UmlGridItem(row = 1, column = 0, columnSpan = 1, size = Size(100f, 10f)),
                        UmlGridItem(row = 1, column = 1, columnSpan = 1, size = Size(100f, 10f)),
                    ),
                columnGap = 20f,
                rowGap = 0f,
            )

        // Cada columna pasa de 100 a 140 y las hijas siguen centradas en la suya.
        assertEquals(0f, rects[0].left)
        assertEquals(70f, rects[1].center.x)
        assertEquals(230f, rects[2].center.x)
    }

    @Test
    fun `lines leave a box through the side facing the target`() {
        val box = Rect(Offset(0f, 0f), Size(100f, 40f))

        assertEquals(Offset(100f, 20f), box.boundaryPointToward(Offset(300f, 20f)))
        assertEquals(Offset(50f, 0f), box.boundaryPointToward(Offset(50f, -200f)))
        assertEquals(Offset(70f, 40f), box.boundaryPointToward(Offset(90f, 60f)))
    }

    @Test
    fun `end labels sit beside the line, clear of the node`() {
        val labelSize = Size(20f, 10f)
        // Línea horizontal hacia la derecha que sale del borde de un nodo en (100, 50).
        val center = endLabelCenter(Offset(100f, 50f), Offset(1f, 0f), labelSize, along = 4f, aside = 2f)

        assertEquals(Offset(114f, 43f), center)
        assertTrue(center.x - labelSize.width / 2f > 100f)
        assertTrue(center.y + labelSize.height / 2f < 50f)
    }

    @Test
    fun `closed heads end exactly at the tip`() {
        val (left, right) = arrowHeadBase(Offset(0f, 0f), Offset(0f, -1f), length = 10f, halfWidth = 4f)
        val diamond = diamondPoints(Offset(0f, 0f), Offset(1f, 0f), length = 20f, halfWidth = 5f)

        assertEquals(setOf(Offset(-4f, 10f), Offset(4f, 10f)), setOf(left, right))
        assertEquals(listOf(Offset(0f, 0f), Offset(-10f, -5f), Offset(-20f, 0f), Offset(-10f, 5f)), diamond)
    }
}
