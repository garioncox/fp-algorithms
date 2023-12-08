package org.garioncox

import kotlin.IllegalStateException

class WFC(private val neighborhood: INeighborhood) {
    fun toCells(board: Array<Array<Int?>>): Array<Array<Cell>> {
        val dim = board.size
        val cellsToReturn =
            Array(board.size) { Array<Cell>(board[0].size) { Cell(setOf()) } }

        for (row in 0..<dim) {
            for (col in 0..<dim) {

                // Get Neighbors
                val current = board[row][col]

                val domain: Set<Int>
                val isSolved: Boolean
                // If current is not solved, set domain to all possible values
                if (current == null) {
                    domain = (1.. board.size).toSet()
                    isSolved = false
                }
                // If current is solved, set the domain to the solved value
                else {
                    domain = setOf(current)
                    isSolved = true
                }

                cellsToReturn[row][col] = Cell(domain, isSolved)
            }
        }

        // Update the domain of all cells
        return propagate(cellsToReturn)!!
    }

    fun solve(input: Array<Array<Cell>>, i: Int = 0): Array<Array<Cell>>? {
        if (isSolved(input)) {
            return input
        }

        var board = input.deepCopy()

        val cells = getUnsolvedCells(board)
        for (cell in cells) {
            if (cell.isSolved) {
                continue
            }
            val domainSavepoint = cell.domain.toSet()

            // For value in possible values
            for (value in cell.domain) {
                val savepoint = board.deepCopy()

                // Set cell to that value and update neighbors
                cell.solveTo(value)
                val propagatedBoard = propagate(board)

                // If none of the neighbors are in an invalid state, recur
                if (propagatedBoard != null) {
                    val recurredBoard = solve(board, i + 1)

                    // If there is a path in the recursion, return
                    if (recurredBoard != null) {
                        return recurredBoard
                    }
                    // Otherwise, try the next value
                }
                // Otherwise, reset the board
                board = savepoint
            }

            // Revert Cell
            cell.isSolved = false
            cell.domain = domainSavepoint

            // Revert propagation
            board = input.copyOf()
        }

        // If no solutions are found for the possible values of this cell, return null
        return null
    }

    fun propagate(board: Array<Array<Cell>>): Array<Array<Cell>>? {
        val dim = board.size

        for (row in 0..<dim) {
            for (col in 0..<dim) {

                // If the current cell is solved, don't solve it again
                val current = board[row][col]
                if (current.isSolved) {
                    continue
                }

                val neighbors = neighborhood.getNeighborsOf(row, col, board)
                for (n in neighbors) {
                    // If the neighbor is not solved, doesn't affect the neighbors
                    if (!n.isSolved) {
                        continue
                    }

                    // Remove values in domain that appear in solved neighbors
                    current.domain - n.domain
                }

                // If the cell has only one value in its domain, set it to solved
                if (current.domain.size == 1) {
                    current.isSolved = true
                }

                // If the cell has no valid domain, don't return a board
                if (current.domain.isEmpty()) {
                    return null
                }
            }
        }

        return board
    }

    fun isSolved(board: Array<Array<Cell>>): Boolean {
        for (row in 0..< board.size) {
            for (col in 0..< board[row].size) {
                if (!board[row][col].isSolved) { return false }
            }
        }

        return true
    }

    fun getUnsolvedCells(board: Array<Array<Cell>>): List<Cell> {
        return board.flatten().filter { cell -> !cell.isSolved }
    }
}


class Cell(var domain: Set<Int>, var isSolved: Boolean = false) {
    fun isEqualTo(expected: Cell): Boolean {
        return expected.domain == this.domain && expected.isSolved == this.isSolved
    }

    fun solveTo(i: Int) {
        this.domain = setOf(i)
        this.isSolved = true
    }

    fun copy(): Cell {
        return Cell(this.domain, this.isSolved)
    }
}

fun Array<Array<Cell>>.deepCopy(): Array<Array<Cell>> {
    return this.map { row ->
        row.map { cell -> cell.copy() }.toTypedArray()
    }.toTypedArray()
}

interface INeighborhood {
    fun getNeighborsOf(row: Int, col: Int, board: Array<Array<Cell>>): Collection<Cell>
}

class SudokuNeighborhood : INeighborhood {
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