package org.garioncox

import java.lang.IllegalStateException
import kotlin.math.min
import kotlin.random.Random

class WFC {
    companion object {
        fun toCells(board: Array<Array<Int>>): Array<Array<Cell>> {
            val dim = board.size
            val cellsToReturn = Array(board.size) { Array<Cell>(board[0].size) { Cell(BooleanArray(dim) { true }.toTypedArray()) } }

            for (row in 0..<dim) {
                for (col in 0..<dim) {

                    // Get Neighbors
                    val current = board[row][col]
                    val neighbors = board.map { it[col] } + board[row] - current

                    val domain: BooleanArray
                    val isSolved: Boolean
                    // If current is not solved, set domain to all possible values
                    if (current == 0) {
                        domain = BooleanArray(dim) { true }
                        isSolved = false
                    }
                    // If current is solved, set the domain to the solved value
                    else {
                        domain = BooleanArray(dim) { false }
                        domain[current - 1] = true
                        isSolved = true
                    }

                    cellsToReturn[row][col] = Cell(domain.toTypedArray(), isSolved)
                }
            }

            // Update the domain of all cells
            return propagate(cellsToReturn)
        }

        fun propagate(board: Array<Array<Cell>>): Array<Array<Cell>> {
            val dim = board.size

            for (row in 0..< dim) {
                for (col in 0..< dim) {
                    val current = board[row][col]
                    if (current.isSolved) { continue }

                    val neighbors = board.map { it[col] } + board[row] - current
                    for (n in neighbors) {
                        if (!n.isSolved) { continue }

                        // Remove values in domain that appear in solved neighbors
                        current.domain[n.domain.indexOf(true)] = false
                    }

                    // If the cell has only one value in its domain, set it to solved
                    if (current.domain.count { it } == 1) {
                        current.isSolved = true
                    }

                    // If the cell has no valid domain, throw exception
                    if (current.domain.count { it } == 0) {
                        throw IllegalStateException()
                    }
                }
            }

            return board
        }

        fun guess(board: Array<Array<Cell>>): Array<Array<Cell>> {

            val randomCell: Cell
            try {
                randomCell = getRandomCellWithLeastEntropy(board)
            }
            catch (e: IllegalStateException) {
                return board
            }

            var toReturn: Array<Array<Cell>>
            while (true) {
                val randomCellDomain = randomCell.domain.indices.filter { randomCell.domain[it] }
                val updatedDomain = BooleanArray(board.size) { false }
                val choice = randomCellDomain.random()
                updatedDomain[choice] = true

                randomCell.domain = updatedDomain.toTypedArray()
                randomCell.isSolved = true

                try {
                   toReturn = propagate(board)
                }
                catch (e: IllegalStateException) {
                    // If failed to propagate, remove the choice from the options and try again
                    randomCell.domain[choice] = false
                    continue
                }

                break
            }

            return toReturn
        }

        private fun getRandomCellWithLeastEntropy(board: Array<Array<Cell>>): Cell {
            val unsolvedCells = board.flatten().filter { cell -> !cell.isSolved }
            if (unsolvedCells.isEmpty()) { throw IllegalStateException() }

            val leastEntropy = unsolvedCells.minOf {cell -> cell.domain.count { it } }
            if (leastEntropy == 1) { throw IllegalStateException() } // Should never be 1. This means all cells are solved

            val cellsWithLeastEntropy = unsolvedCells.filter { cell -> cell.domain.count { it } == leastEntropy }
            return cellsWithLeastEntropy.random()
        }
    }
}

class Cell(var domain: Array<Boolean>, var isSolved: Boolean = false) {
    fun isEqualTo(expected: Cell): Boolean {
        return expected.domain.contentEquals(this.domain) && expected.isSolved == this.isSolved
    }
}