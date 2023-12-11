package org.garioncox

class SudokuSolver {
    companion object {
        fun solveSudoku(board: Array<Array<Int>>): Boolean {
            for (row in board.indices) {
                for (col in board[0].indices) {
                    if (board[row][col] == 0) {
                        for (num in 1..9) {
                            if (isValid(board, row, col, num)) {
                                board[row][col] = num
                                if (solveSudoku(board)) { // If there is a valid solution in this branch, return it
                                    return true
                                }
                                board[row][col] = 0 // Backtrack if no valid solution found
                            }
                        }
                        return false
                    }
                }
            }
            return true // All cells are filled
        }

        fun isValid(board: Array<Array<Int>>, row: Int, col: Int, num: Int): Boolean {
            val rowNums = board[row].toSet()
            val colNums = board.map { it[col] }.toSet()
            val boxNums = getBoxNums(board, row, col)

            return num !in (rowNums + colNums + boxNums)
        }

        fun getBoxNums(board: Array<Array<Int>>, row: Int, col: Int): Set<Int> {
            val boxNums = mutableSetOf<Int>()
            for (i in 0..< 3) {
                for (j in 0..< 3) {
                    boxNums.add(board[(row / 3) * 3 + i][(col / 3) * 3 + j])
                }
            }

            return boxNums
        }
    }
}