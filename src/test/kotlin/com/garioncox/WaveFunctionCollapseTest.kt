package com.garioncox

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WaveFunctionCollapseTest {
    @Test
    fun `Testing Framework Is Installed Correctly`() {
        assertTrue(true);
    }

    @Test
    fun `4x4 Sudoku Collapses`() {
        val board =
            arrayOf(
                arrayOf(null, 4,    null, null),
                arrayOf(null, 2,    3,    null),
                arrayOf(null, null, null, 3),
                arrayOf(4,    3,    null, 2)
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
    fun `Get Domain of Cell when Board has Row Values`() {
        val board =
            arrayOf(
                Array<Int?>(4) { null },
                arrayOf(1, null, 3, 4),
                Array<Int?>(4) { null },
                Array<Int?>(4) { null },
                )

        val domain = Wave.getDomain(1, 1, board)
        assertEquals(listOf(2), domain.toList())
    }

    @Test
    fun `Get Domain of Cell when Board has Col Values`() {
        val board =
            arrayOf(
                arrayOf(null, 1, null, null),
                Array<Int?>(4) { null },
                arrayOf(null, 3, null, null),
                arrayOf(null, 4, null, null),
            )

        val domain = Wave.getDomain(1, 1, board)
        assertEquals(listOf(2), domain.toList())
    }

    @Test
    fun `Get Domain of Cell when Board has Row and Col Values`() {
        val board =
            arrayOf(
                arrayOf(null, 1, null, null),
                arrayOf(null, null, 3, null),
                Array<Int?>(4) { null },
                arrayOf(null, 4, null, null),
            )

        val domain = Wave.getDomain(1, 1, board)
        assertEquals(listOf(2), domain.toList())
    }

    @Test
    fun `Get Domain of Cell when Board Has Row, Col Values and Domain Size Should Be Greater Than 1`() {
        val board =
            arrayOf(
                arrayOf(null, 1, null, null),
                arrayOf(null, null, 4, null),
                Array<Int?>(4) { null },
                arrayOf(null, 4, null, null),
            )

        val domain = Wave.getDomain(1, 1, board)
        assertEquals(listOf(2, 3), domain.toList())
    }

    @Test
    fun `Get Cells To Solve`() {
        val cells =
            arrayOf(
                arrayOf(Cell(0, 0, setOf(1), true), Cell(0, 1, setOf(3), true), Cell(0,2, setOf(2, 3))),
                arrayOf(Cell(1, 0, setOf(3), true), Cell(1, 1, setOf(2), true), Cell(1, 2, setOf(1, 3))),
                arrayOf(Cell(2, 0, setOf(2, 3)), Cell(2, 1, setOf(1, 3)), Cell(2, 2, (setOf(1, 2, 3)))),
            )

        val expected = listOf(
            Cell(0,2, setOf(2, 3)),
            Cell(1, 2, setOf(1, 3)),
            Cell(2, 0, setOf(2, 3)),
            Cell(2, 1, setOf(1, 3)),
            Cell(2, 2, (setOf(1, 2, 3)))
        )

        val toSolve = Wave.getCellsToSolve(cells)

        for(i in toSolve.indices) {
            assertEquals(expected[i].domain, toSolve[i].domain)
        }
    }

    @Test
    fun `Get Neighbors from Center Cell`() {
        val cells =
            arrayOf(
                arrayOf(Cell(0, 0, setOf(1)), Cell(0, 1, setOf(3)), Cell(0,2, setOf(2, 3))),
                arrayOf(Cell(1, 0, setOf(3)), Cell(1, 1, setOf(2)), Cell(1, 2, setOf(1, 3))),
                arrayOf(Cell(2, 0, setOf(2, 3)), Cell(2, 1, setOf(1, 3)), Cell(2, 2, (setOf(1, 2, 3)))),
            )

        val expected = listOf(
            Cell(0, 1, setOf(3)),
            Cell(1, 2, setOf(1, 3)),
            Cell(1, 0, setOf(3)),
            Cell(2, 1, setOf(1, 3))
        )

        val neighbors = Wave.getNeighbors(cells[1][1], cells)

        for(i in neighbors.indices) {
            assertEquals(expected[i].domain, neighbors[i].domain)
        }
    }

    @Test
    fun `Get Neighbors from Corner Cell`() {
        val cells =
            arrayOf(
                arrayOf(Cell(0, 0, setOf(1)), Cell(0, 1, setOf(3)), Cell(0,2, setOf(2, 3))),
                arrayOf(Cell(1, 0, setOf(3)), Cell(1, 1, setOf(2)), Cell(1, 2, setOf(1, 3))),
                arrayOf(Cell(2, 0, setOf(2, 3)), Cell(2, 1, setOf(1, 3)), Cell(2, 2, (setOf(1, 2, 3)))),
            )

        val expected = listOf(
            Cell(1, 0, setOf(3)),
            Cell(2, 0, setOf(2, 3)),
            Cell(0, 1, setOf(3)),
            Cell(0,2, setOf(2, 3))
        )

        val neighbors = Wave.getNeighbors(cells[0][0], cells)

        for(i in neighbors.indices) {
            assertEquals(expected[i].domain, neighbors[i].domain)
        }
    }

    @Test
    fun `Get Neighbors from Edge Cell`() {
        val cells =
            arrayOf(
                arrayOf(Cell(0, 0, setOf(1)), Cell(0, 1, setOf(3)), Cell(0,2, setOf(2, 3))),
                arrayOf(Cell(1, 0, setOf(3)), Cell(1, 1, setOf(2)), Cell(1, 2, setOf(1, 3))),
                arrayOf(Cell(2, 0, setOf(2, 3)), Cell(2, 1, setOf(1, 3)), Cell(2, 2, (setOf(1, 2, 3)))),
            )

        val expected = listOf(
            Cell(0, 0, setOf(1)),
            Cell(2, 0, setOf(2, 3)),
            Cell(1, 1, setOf(2)),
            Cell(1, 2, setOf(1, 3))
        )

        val neighbors = Wave.getNeighbors(cells[1][0], cells)

        for(i in neighbors.indices) {
            assertEquals(expected[i].domain, neighbors[i].domain)
        }
    }

    private fun assertAreExpectedCells(cells: Array<Array<Cell>>, expected: Array<Array<Cell>>) {
        assertEquals(expected.size, cells.size)
        for (row in cells.indices) {
            for (col in 0..< cells[0].size) {
                assertEquals(cells[row][col].domain, expected[row][col].domain)
            }
        }
    }
}