package org.garioncox

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SudokuSolverTests {
    private lateinit var board: Array<Array<Int>>
    @BeforeTest
    fun before() {
        board = arrayOf(
            arrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            arrayOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            arrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            arrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            arrayOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            arrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            arrayOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            arrayOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            arrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
    }

    @Test
    fun `Board Solves`() {
        assertTrue(SudokuSolver.solveSudoku(board))
    }

    @Test
    fun `Valid Cell is Valid`() {
        assertTrue(SudokuSolver.isValid(board, 0, 2, 4))
    }

    @Test
    fun `Invalid Cell is Invalid`() {
        assertTrue(!SudokuSolver.isValid(board, 0, 2, 8))
    }

    @Test
    fun `Get Numbers From 3x3 Box In Board`() {
        board = Array(9) { row -> Array(9) { col -> 0 } }
        for (i in 0..< 9) {
            for (j in 0..< 9) {
                board[i][j] = 10*i + j + 1
            }
        }

        val expected = setOf(37, 38, 39, 47, 48, 49, 57, 58, 59)

        val boxNums = SudokuSolver.getBoxNums(board, 4, 8)

        assertEquals(expected, boxNums)
    }
}