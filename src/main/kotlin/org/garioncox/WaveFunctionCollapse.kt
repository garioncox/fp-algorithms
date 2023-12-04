package org.garioncox

import java.lang.IllegalStateException
import kotlin.math.min
import kotlin.random.Random

class WFC(val constraints: IConstraints) {
    fun toCells(board: Array<Array<Int>>): Array<Array<Cell>> {
        val dim = board.size
        val cellsToReturn =
            Array(board.size) { Array<Cell>(board[0].size) { Cell(BooleanArray(dim) { true }.toTypedArray()) } }

        for (row in 0..<dim) {
            for (col in 0..<dim) {

                // Get Neighbors
                val current = board[row][col]

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

        for (row in 0..<dim) {
            for (col in 0..<dim) {
                val current = board[row][col]
                if (current.isSolved) {
                    continue
                }

//                val neighbors = board.map { it[col] } + board[row] - current
                val neighbors = constraints.getNeighborsOf(row, col, board)
                for (n in neighbors) {
                    if (!n.isSolved) {
                        continue
                    }

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
        } catch (e: IllegalStateException) {
            return board
        }

        var toReturn: Array<Array<Cell>>
        while (true) {
            val randomCellDomain = randomCell.domain.copyOf()
            val validRandomCellDomain = randomCell.domain.indices.filter { randomCell.domain[it] }
            val updatedDomain = BooleanArray(board.size) { false }
            val choice = validRandomCellDomain.random()
            updatedDomain[choice] = true

            randomCell.domain = updatedDomain.toTypedArray()
            randomCell.isSolved = true

            try {
                toReturn = propagate(board)
            } catch (e: IllegalStateException) {
                // If failed to propagate, remove the choice from the options and try again
                randomCell.domain = randomCellDomain
                randomCell.domain[choice] = false
                randomCell.isSolved = false

                // If there is only one value in the domain, that is the solution
                if (randomCell.domain.indices.count { randomCell.domain[it] } == 1) {
                    toReturn = board
                    break
                }

                continue
            }

            break
        }

        return toReturn
    }

    fun isSolved(board: Array<Array<Cell>>): Boolean {
        for (row in 0..< board.size) {
            for (col in 0..< board[row].size) {
                if (!board[row][col].isSolved) { return false }
            }
        }

        return true
    }

    private fun getRandomCellWithLeastEntropy(board: Array<Array<Cell>>): Cell {
        val unsolvedCells = board.flatten().filter { cell -> !cell.isSolved }
        if (unsolvedCells.isEmpty()) {
            throw IllegalStateException()
        }

        val leastEntropy = unsolvedCells.minOf { cell -> cell.domain.count { it } }
        if (leastEntropy == 1) {
            throw IllegalStateException()
        } // Should never be 1. This means all cells are solved

        val cellsWithLeastEntropy = unsolvedCells.filter { cell -> cell.domain.count { it } == leastEntropy }
        return cellsWithLeastEntropy.random()
    }
}


class Cell(var domain: Array<Boolean>, var isSolved: Boolean = false) {
    fun isEqualTo(expected: Cell): Boolean {
        return expected.domain.contentEquals(this.domain) && expected.isSolved == this.isSolved
    }
}

interface IConstraints {
    fun getNeighborsOf(row: Int, col: Int, board: Array<Array<Cell>>): Collection<Cell>
}

class SudokuConstraints : IConstraints {
    override fun getNeighborsOf(row: Int, col: Int, board: Array<Array<Cell>>): Collection<Cell> {
        val currentCell = board[row][col]
        val rowCells = board[row].toSet()
        val colCells = board.map { it[col] }.toSet()
        val boxCells = getBoxCells(row, col, board)

        return colCells + rowCells + boxCells - currentCell
    }

    private fun getBoxCells(row: Int, col: Int, board: Array<Array<Cell>>): Set<Cell> {
        val box = mutableSetOf<Cell>()
        for (i in 0..<3) {
            for (j in 0..<3) {
                box.add(board[(row / 3) * 3 + i][(col / 3) * 3 + j])
            }
        }

        return box
    }
}