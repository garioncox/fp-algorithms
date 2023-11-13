package com.garioncox

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WaveFunctionCollapseTest {
    @Test
    fun `Testing Framework Is Installed Correctly`()
    {
        assertTrue(true);
    }

    @Test
    fun `4x4 Sudoku Collapses`() {
        val board =
            arrayOf(
                arrayOf(null, 4, null, null),
                arrayOf(null, 2, 3, null),
                arrayOf(null, null, null, 3),
                arrayOf(4, 3, null, 2)
            )
        val solvedBoard = Wave.collapse(board)

        assertTrue(solvedBoard.contentEquals(
            arrayOf(
                arrayOf(3, 4, 2, 1),
                arrayOf(1, 2, 3, 4),
                arrayOf(2, 1, 4, 3),
                arrayOf(4, 3, 1, 2)
            )))
    }

    @Test
    fun `Get Domain of Cell when Board is Empty`() {
        val board =
            arrayOf(
                Array<Int?>(4) { null },
                Array<Int?>(4) { null },
                Array<Int?>(4) { null },
                Array<Int?>(4) { null }
            )

        val domain = Wave.getDomain(2, 2, board)

        assertEquals(listOf(1, 2, 3, 4), domain.toList())
    }

    @Test
    fun `Get Domain of Cell when Board has Values`() {
        val board =
            arrayOf(
                arrayOf(null, 1, null, null),
                arrayOf(2, null, 3, null),
                Array<Int?>(4) { null },
                Array<Int?>(4) { null }
            )

        val domain = Wave.getDomain(2, 2, board)

        assertEquals(listOf(4), domain.toList())
    }
}