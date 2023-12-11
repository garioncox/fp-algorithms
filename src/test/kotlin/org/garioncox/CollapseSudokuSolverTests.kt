package org.garioncox

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollapseSudokuSolverTests {
    @Test
    fun `Arrays Are Passed By Reference`() {
        val board = arrayOf(
            arrayOf(5, 3, 0),
            arrayOf(6, 0, 0),
            arrayOf(0, 9, 8),
        )

        // Expected the original board not to change
        val expected = arrayOf(
            arrayOf(5, 3, 0),
            arrayOf(6, 0, 0),
            arrayOf(0, 9, 8),
        )

        val changedBoard = changeWithoutCopy(board)

        assertFalse(board.isEqualTo(expected))
        assertFalse(changedBoard.isEqualTo(expected))
    }

    @Test
    fun `Can Keep Original Array If Copy Passed In Instead`() {
        val board = arrayOf(
            arrayOf(5, 3, 0),
            arrayOf(6, 0, 0),
            arrayOf(0, 9, 8),
        )

        // Expected the original board not to change
        val expected = arrayOf(
            arrayOf(5, 3, 0),
            arrayOf(6, 0, 0),
            arrayOf(0, 9, 8),
        )

        val changedBoard = changeWithCopy(board.deepCopyOf())

        assertTrue(board.isEqualTo(expected))
        assertFalse(changedBoard.isEqualTo(expected))
    }

    private fun changeWithoutCopy(board: Array<Array<Int>>): Array<Array<Int>> {
        board[0][0] = 1000
        return board
    }

    private fun changeWithCopy(board: Array<Array<Int>>): Array<Array<Int>> {
        val copy = board.deepCopyOf()
        copy[0][0] = 1000
        return copy
    }

    private fun Array<Array<Int>>.isEqualTo(expected: Array<Array<Int>>): Boolean {
        for (row in this.indices) {
            for (col in this[row].indices) {
                if (this[row][col] != expected[row][col]) {
                    return false
                }
            }
        }

        return true
    }

    private fun Array<Array<Int>>.deepCopyOf(): Array<Array<Int>> {
        val arrayOfArrays = Array(this.size) { Array(this[0].size) { 0 } }
        for (row in this.indices) {
            val r = Array(this[row].size) { 0 }

            for (col in this[row].indices) {
                r[col] = this[row][col]
            }

            arrayOfArrays[row] = r
        }

        return arrayOfArrays
    }
}