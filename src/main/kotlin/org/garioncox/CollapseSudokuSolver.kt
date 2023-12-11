package org.garioncox

import org.garioncox.CollapseSudokuSolver.Companion.deepCopyOf

class CollapseSudokuSolver {
    companion object {
        fun solveSudoku(board: Array<Array<MutableSet<Int>>>): Boolean {
            val bCopy = board.deepCopyOf() // This branch's board

            for (row in bCopy.indices) {
                for (col in bCopy[0].indices) {
                    if (bCopy[row][col].size != 1) { // If the cell is not yet solved
                        val domainCopy = board[row][col].toMutableSet()
                        for (num in domainCopy) { // Make a copy of the domain so that we can iterate through it
                            board[row][col] = mutableSetOf(num)
                            if (solveSudoku(propagate(bCopy))) { // If there is a valid solution in this branch, return it
                                return true
                            }
                            bCopy[row][col] = domainCopy // Backtrack if no valid solution found
                        }
                        return false
                    }
                }
            }
            return true // All cells are filled
        }

        fun solveSudoku(inputBoard: Array<Array<Int>>): Boolean {
            val sBoard = getPossibleCellValues(inputBoard)
            if (solveSudoku(sBoard)) {
                return true
            }

            return false
        }

        fun propagate(inputBoard: Array<Array<MutableSet<Int>>>): Array<Array<MutableSet<Int>>> {
            val bCopy = inputBoard.deepCopyOf() // This branch's board

            for (row in bCopy.indices) {
                for (col in bCopy[0].indices) {
                    if (bCopy[row][col].size == 1) {
                        updateDomainFor(row, col, bCopy)
                    }
                }
            }

            return bCopy
        }

        fun updateDomainFor(row: Int, col: Int, board: Array<Array<MutableSet<Int>>>) {
            val currentValue = board[row][col].first()

            val rowSets = board[row]
            val colSets = board.map { it[col] }
            val boxSets = getBoxSets(row, col, board)

            val setsToUpdate = rowSets + colSets + boxSets
            for (s in setsToUpdate) {
                if (s.contains(currentValue)) {
                    s -= currentValue
                }
            }
        }

        fun getPossibleCellValues(board: Array<Array<Int>>): Array<Array<MutableSet<Int>>> {
            val arrayOfArrays = Array(board.size) { Array(board[0].size) { mutableSetOf<Int>() } }
            for (row in board.indices) {
                val r = Array(board[row].size) { mutableSetOf<Int>() }

                for (col in board[row].indices) {
                    r[col] = getPossibleValuesOf(row, col, board)
                }

                arrayOfArrays[row] = r
            }

            return arrayOfArrays
        }

        fun getPossibleValuesOf(row: Int, col: Int, board: Array<Array<Int>>): MutableSet<Int> {
            val currentValue = board[row][col]
            if (currentValue != 0) { return(mutableSetOf(currentValue)) }

            val rowValues = board[row]
            val colValues = board.map { it[col] }
            val boxValues = getBoxNums(row, col, board)
            val currentValuesTaken = (rowValues + colValues + boxValues).filter { c -> c != 0 }.toMutableSet()

            return ((0..9).toMutableSet() - currentValuesTaken).toMutableSet()
        }

        fun getBoxNums(row: Int, col: Int, board: Array<Array<Int>>): MutableSet<Int> {
            val boxNums = mutableSetOf<Int>()
            for (i in 0..< 3) {
                for (j in 0..< 3) {
                    boxNums.add(board[(row / 3) * 3 + i][(col / 3) * 3 + j])
                }
            }

            return boxNums
        }

        fun getBoxSets(row: Int, col: Int, board: Array<Array<MutableSet<Int>>>): MutableList<MutableSet<Int>> {
            val toReturn = mutableListOf<MutableSet<Int>>()
            for (i in 0..< 3) {
                for (j in 0..< 3) {
                    toReturn.add(board[(row / 3) * 3 + i][(col / 3) * 3 + j])
                }
            }

            return toReturn
        }

        private fun Array<Array<MutableSet<Int>>>.deepCopyOf(): Array<Array<MutableSet<Int>>> {
            val arrayOfArrays = Array(this.size) { Array(this[0].size) { mutableSetOf<Int>() } }
            for (row in this.indices) {
                val r = Array(this[row].size) { mutableSetOf<Int>() }

                for (col in this[row].indices) {
                    r[col] = this[row][col]
                }

                arrayOfArrays[row] = r
            }

            return arrayOfArrays
        }
    }
}