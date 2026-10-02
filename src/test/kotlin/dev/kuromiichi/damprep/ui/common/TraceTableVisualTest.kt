package dev.kuromiichi.damprep.ui.common

import kotlin.test.Test
import kotlin.test.assertEquals

class TraceTableVisualTest {
    @Test
    fun `columns get width proportional to their longest text`() {
        val table =
            LearningTraceTable(
                title = "Casos",
                columns = listOf("Caso", "n", "Esperado"),
                rows = listOf(listOf("CP1", "3", "Nota no válida")),
            )

        assertEquals(listOf(4f, 4f, 14f), table.columnWeights())
    }
}
