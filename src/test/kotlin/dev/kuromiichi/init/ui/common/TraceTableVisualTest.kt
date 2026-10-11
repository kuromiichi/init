package dev.kuromiichi.init.ui.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TraceTableVisualTest {
    @Test
    fun `columns keep their natural width when everything fits`() {
        val table =
            LearningTraceTable(
                title = "Casos",
                columns = listOf("Caso", "n", "Esperado"),
                rows = listOf(listOf("CP1", "3", "Nota no válida")),
            )

        assertEquals(listOf(4, 4, 14), table.columnCharacterWidths(availableCharacters = 40))
    }

    @Test
    fun `short columns are never squeezed to make room for a long one`() {
        val table =
            LearningTraceTable(
                title = "Dos ejecuciones",
                columns = listOf("Dato", "¿catch?", "Salida"),
                rows =
                    listOf(
                        listOf("17", "No", "Dentro de un año tendrás 18"),
                        listOf("hola", "Sí", "Eso no es un número: For input string: \"hola\""),
                    ),
            )

        val widths = table.columnCharacterWidths(availableCharacters = 48)

        assertEquals(listOf(4, 7), widths.take(2))
        assertEquals(48, widths.sum())
    }

    @Test
    fun `no column is narrower than its longest word`() {
        val table =
            LearningTraceTable(
                title = "Palabras",
                columns = listOf("Variable", "Descripción"),
                rows = listOf(listOf("contador", "Cuenta las vueltas que lleva el bucle hasta ahora")),
            )

        val widths = table.columnCharacterWidths(availableCharacters = 12)

        assertTrue(widths[0] >= "Variable".length)
        assertTrue(widths[1] >= "Descripción".length)
    }
}
