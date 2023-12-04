package org.garioncox

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class Tests {
    private lateinit var wfc: WFC
    @BeforeTest
    fun before() {
        wfc = WFC(TestConstraints())
    }
    @Test
    fun `Convert Initial Array to Cells`() {
        val board = arrayOf(
            arrayOf(0, 0),
            arrayOf(0, 0)
        )

        val cells = wfc.toCells(board)

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

        val cells = wfc.toCells(board)

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

        val cells = wfc.toCells(board)

        // Make a guess
        cells[0][2] = Cell(arrayOf(true, false, false), true)
        wfc.propagate(cells)

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

        val cells = wfc.toCells(board)

        // Minimal guesses to solve board
        cells[0][0] = Cell(arrayOf(false, true, false), true)
        cells[0][2] = Cell(arrayOf(true, false, false), true)
        cells[1][1] = Cell(arrayOf(true, false, false), true)
        cells[0][2] = Cell(arrayOf(true, false, false), true)
        wfc.propagate(cells)

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

        val cells = wfc.toCells(board)

        for (i in 0..2) {
            wfc.guess(cells)
            wfc.propagate(cells)
        }

        for (i in 0..< cells.size) {
            for (j in 0..< cells[0].size) {
                assertTrue(cells[i][j].isSolved)
            }
        }
    }

    @Test
    fun `Sudoku Board Gets Neighbors with Constraints`() {
        wfc = WFC(SudokuConstraints())

        val input = ToSudokuCells(arrayOf(
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        ))

        val expected = arrayOf(
            input[0][3], input[1][3], input[2][3], input[4][3], input[5][3], input[6][3], input[7][3], input[8][3],
            input[3][0], input[3][1], input[3][2], input[3][4], input[3][5], input[3][6], input[3][7], input[3][8],
            input[4][4], input[4][5], input[5][4], input[5][5]
        )

        val neighbors = SudokuConstraints().getNeighborsOf(3, 3, input).toMutableList()

        for (i in 0..< neighbors.size) {
            assertTrue(neighbors[i].isEqualTo(expected[i]))
        }
    }

    @Test
    fun `Sudoku Board Solves`() {
        wfc = WFC(SudokuConstraints())

        val input = arrayOf(
            arrayOf(4, 8, 3, 9, 2, 1, 6, 5, 7),
            arrayOf(9, 6, 7, 3, 4, 5, 8, 2, 1),
            arrayOf(2, 5, 0, 8, 7, 6, 4, 9, 3),
            arrayOf(5, 4, 8, 1, 3, 2, 9, 7, 6),
            arrayOf(7, 2, 9, 5, 6, 4, 1, 3, 8),
            arrayOf(1, 3, 6, 7, 9, 8, 0, 4, 5),
            arrayOf(3, 7, 2, 6, 8, 9, 5, 1, 4),
            arrayOf(8, 1, 4, 0, 5, 3, 7, 6, 9),
            arrayOf(6, 9, 5, 4, 1, 7, 3, 8, 2)
        )

        val expected = ToSudokuCells(arrayOf(
            arrayOf(4, 8, 3, 9, 2, 1, 6, 5, 7),
            arrayOf(9, 6, 7, 3, 4, 5, 8, 2, 1),
            arrayOf(2, 5, 1, 8, 7, 6, 4, 9, 3),
            arrayOf(5, 4, 8, 1, 3, 2, 9, 7, 6),
            arrayOf(7, 2, 9, 5, 6, 4, 1, 3, 8),
            arrayOf(1, 3, 6, 7, 9, 8, 2, 4, 5),
            arrayOf(3, 7, 2, 6, 8, 9, 5, 1, 4),
            arrayOf(8, 1, 4, 2, 5, 3, 7, 6, 9),
            arrayOf(6, 9, 5, 4, 1, 7, 3, 8, 2)
        ))

        val cells = wfc.toCells(input)

        for (i in 0..2) {
            wfc.guess(cells)
            wfc.propagate(cells)
        }

        cells.isEqualTo(expected)
    }

    @Test
    fun `Sudoku Board Solves 2`() {
        wfc = WFC(SudokuConstraints())

        val input = arrayOf(
            arrayOf(0, 0, 3, 0, 2, 0, 6, 0, 0),
            arrayOf(9, 0, 0, 3, 0, 5, 0, 0, 1),
            arrayOf(0, 0, 1, 8, 0, 6, 4, 0, 0),
            arrayOf(0, 0, 8, 1, 0, 2, 9, 0, 0),
            arrayOf(7, 0, 0, 0, 0, 0, 0, 0, 8),
            arrayOf(0, 0, 6, 7, 0, 8, 2, 0, 0),
            arrayOf(0, 0, 2, 6, 0, 9, 5, 0, 0),
            arrayOf(8, 0, 0, 2, 0, 3, 0, 0, 9),
            arrayOf(0, 0, 5, 0, 1, 0, 3, 0, 0)
        )

        val expected = ToSudokuCells(arrayOf(
            arrayOf(4, 8, 3, 9, 2, 1, 6, 5, 7),
            arrayOf(9, 6, 7, 3, 4, 5, 8, 2, 1),
            arrayOf(2, 5, 1, 8, 7, 6, 4, 9, 3),
            arrayOf(5, 4, 8, 1, 3, 2, 9, 7, 6),
            arrayOf(7, 2, 9, 5, 6, 4, 1, 3, 8),
            arrayOf(1, 3, 6, 7, 9, 8, 2, 4, 5),
            arrayOf(3, 7, 2, 6, 8, 9, 5, 1, 4),
            arrayOf(8, 1, 4, 2, 5, 3, 7, 6, 9),
            arrayOf(6, 9, 5, 4, 1, 7, 3, 8, 2)
        ))

        val cells = wfc.toCells(input)

        while (!wfc.isSolved(cells)) {
            wfc.guess(cells)
            //wfc.propagate(cells)
        }

        cells.isEqualTo(expected)
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

private fun ToSudokuCells(inputBoard: Array<Array<Int>>): Array<Array<Cell>> {
    val one =   arrayOf(true, false, false, false, false, false, false, false, false)
    val two =   arrayOf(false, true, false, false, false, false, false, false, false)
    val three = arrayOf(false, false, true, false, false, false, false, false, false)
    val four =  arrayOf(false, false, false, true, false, false, false, false, false)
    val five =  arrayOf(false, false, false, false, true, false, false, false, false)
    val six =   arrayOf(false, false, false, false, false, true, false, false, false)
    val seven = arrayOf(false, false, false, false, false, false, true, false, false)
    val eight = arrayOf(false, false, false, false, false, false, false, true, false)
    val nine =  arrayOf(false, false, false, false, false, false, false, false, true)

    val inputCells = Array(9) { Array(9) { Cell(one) } }

    for (row in 0..< inputBoard.size) {
        for (col in 0..< inputBoard[row].size) {
            when (inputBoard[row][col]) {
                1 -> inputCells[row][col] = Cell(one)
                2 -> inputCells[row][col] = Cell(two)
                3 -> inputCells[row][col] = Cell(three)
                4 -> inputCells[row][col] = Cell(four)
                5 -> inputCells[row][col] = Cell(five)
                6 -> inputCells[row][col] = Cell(six)
                7 -> inputCells[row][col] = Cell(seven)
                8 -> inputCells[row][col] = Cell(eight)
                9 -> inputCells[row][col] = Cell(nine)
                else -> { inputCells[row][col] = Cell(arrayOf()) }
            }
        }
    }

    return inputCells
}

class TestConstraints : IConstraints {
    override fun getNeighborsOf(row: Int, col: Int, board: Array<Array<Cell>>): Collection<Cell> {
        val currentCell = board[row][col]
        val rowCells = board[row].toSet()
        val colCells = board.map { it[col] }.toSet()

        return colCells + rowCells - currentCell
    }
}