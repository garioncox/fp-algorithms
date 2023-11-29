package org.garioncox

import kotlin.test.Test
import kotlin.test.assertTrue


class Tests {
    @Test
    fun `Convert Initial Array to Cells`() {
        val board = arrayOf(
            arrayOf(0, 0),
            arrayOf(0, 0)
        )

        val cells = WFC.toCells(board)

        for (row in cells) {
            for (cell in row) {
                assertTrue(cell.isEqualTo(Cell(arrayOf(true, true))))
            }
        }
    }

    @Test
    fun `Convert Initial Array to Cells when Values Filled In`() {
        val board = arrayOf(
            arrayOf(0, 0, 1),
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0)
        )

        val cells = WFC.toCells(board)

        val expected = arrayOf(
            arrayOf(Cell(arrayOf(false, true, true)), Cell(arrayOf(false, true, true)), Cell(arrayOf(true, false, false), true)),
            arrayOf(Cell(arrayOf(true, true, true)), Cell(arrayOf(true, true, true)), Cell(arrayOf(false, true, true))),
            arrayOf(Cell(arrayOf(true, true, true)), Cell(arrayOf(true, true, true)), Cell(arrayOf(false, true, true)))
        )

        assertTrue(cells.isEqualTo(expected))
    }

    @Test
    fun `Board Propagates Correctly when a Guess Is Made`() {
        val board = arrayOf(
            arrayOf(0, 0, 1),
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0)
        )

        val cells = WFC.toCells(board)

        // Make a guess
        cells[0][2] = Cell(arrayOf(true, false, false), true)
        WFC.propagate(cells)

        val expected = arrayOf(
            arrayOf(Cell(arrayOf(false, true, true)), Cell(arrayOf(false, true, true)), Cell(arrayOf(true, false, false), true)),
            arrayOf(Cell(arrayOf(true, true, true)), Cell(arrayOf(true, true, true)), Cell(arrayOf(false, true, true))),
            arrayOf(Cell(arrayOf(true, true, true)), Cell(arrayOf(true, true, true)), Cell(arrayOf(false, true, true)))
        )

        assertTrue(cells.isEqualTo(expected))
    }

    @Test
    fun `Board Manually Solves Correctly`() {
        val board = arrayOf(
            arrayOf(0, 0, 1),
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0)
        )

        val cells = WFC.toCells(board)

        // Minimal guesses to solve board
        cells[0][0] = Cell(arrayOf(false, true, false), true)
        cells[0][2] = Cell(arrayOf(true, false, false), true)
        cells[1][1] = Cell(arrayOf(true, false, false), true)
        cells[0][2] = Cell(arrayOf(true, false, false), true)
        WFC.propagate(cells)

        val expected = arrayOf(
            arrayOf(Cell(arrayOf(false, true, false), true), Cell(arrayOf(false, false, true), true), Cell(arrayOf(true, false, false), true)),
            arrayOf(Cell(arrayOf(false, false, true), true), Cell(arrayOf(true, false, false), true), Cell(arrayOf(false, true, false), true)),
            arrayOf(Cell(arrayOf(true, false, false), true), Cell(arrayOf(false, true, false), true), Cell(arrayOf(false, false, true), true))
        )

        assertTrue(cells.isEqualTo(expected))
    }

    @Test
    fun `Board Solves`() {
        val board = arrayOf(
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0)
        )

        val cells = WFC.toCells(board)

        for (i in 0..2) {
            WFC.guess(cells)
            WFC.propagate(cells)
        }

        for (i in 0..< cells.size) {
            for (j in 0..< cells[0].size) {
                assertTrue(cells[i][j].isSolved)
            }
        }
    }
}

private fun Array<Array<Cell>>.isEqualTo(expected: Array<Array<Cell>>): Boolean {
    for (i in 0..< this.size) {
        for (j in 0..< this[0].size) {
            if (!this[i][j].isEqualTo(expected[i][j])) { return false }
        }
    }

    return true
}