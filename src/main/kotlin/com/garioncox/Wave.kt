package com.garioncox

import kotlin.random.Random

class Wave {
    companion object {
        fun collapse(board: Array<Array<Int?>>): Array<Array<Int>> {
            val rowCount = board.size
            val colCount = board[0].size
            var boardCells = toCells(board)
            var toSolve = getCellsToSolve(boardCells)

            while (toSolve.isNotEmpty()) {
                val currentCell = toSolve[Random.nextInt(toSolve.size)]
                boardCells = propagate(currentCell, boardCells)

                toSolve = getCellsToSolve(boardCells)
            }


            // Convert back to Int array
            val boardToReturn = Array(board.size-1) { arrayOf<Int>() }

            for (row in 0..< rowCount) {
                val r = Array(colCount) { 0 }

                for (col in 0..< colCount) {
                    r[col] = boardCells[row][col].domain.first()
                }

                boardToReturn[row] = r
            }

            return boardToReturn
        }

        private fun propagate(currentCell: Cell, board: Array<Array<Cell>>): Array<Array<Cell>> {

            // While we have not propagated
            var mutation: Array<Array<Cell>> = board.copyOf()
            while (!currentCell.isSolved) {
                mutation = board.copyOf()
                val guess = Random.nextInt(currentCell.domain.toList().size)

                // update domain of neighbors
                for (n in getNeighbors(currentCell, mutation)) {
                    if (!n.isSolved) {
                        n.domain - guess
                    }

                    // If we have invalid domain, remove guess from domain and try again
                    if (n.domain.isEmpty()) {
                        currentCell.domain - guess
                        continue
                    }
                }

                currentCell.isSolved = true
            }

            // if any neighbors are not solved, propagate(neighbor)
            for (n in getNeighbors(currentCell, mutation)) {
                if (n.domain.size == 1 && !n.isSolved) {
                    mutation = propagate(n, mutation)
                }
            }

            return mutation
        }

        fun getCellsToSolve(superimposedBoard: Array<Array<Cell>>): List<Cell> {
            val rowCount = superimposedBoard.size
            val colCount = superimposedBoard[0].size

            val cellsToSolve = mutableListOf<Cell>()
            for (row in 0..< rowCount) {
                for (col in 0..< colCount) {
                    val currentCell = superimposedBoard[row][col]
                    if (!currentCell.isSolved) {
                        cellsToSolve += Cell(row, col, currentCell.domain.toSet())
                    }
                }
            }

            return cellsToSolve
        }

        fun getNeighbors(cell: Cell, board: Array<Array<Cell>>): List<Cell> {
            val colCells = board[cell.row].toList()
            val rowCells = (board.map { it[cell.col] })

            return rowCells + colCells - listOf(cell)
        }

        fun toCells(board: Array<Array<Int?>>): Array<Array<Cell>> {
            val rowCount = board.size
            val colCount = board[0].size

            val superpositions = Array(rowCount) { arrayOf<Cell>() }

            for (row in 0..< rowCount) {
                val r = Array(colCount) { Cell(0, 0, setOf()) }

                for (col in 0..< colCount) {
                    val currentCell = board[row][col]
                    if (currentCell != null) {
                        r[col] = Cell(row, col, setOf(currentCell))
                    }
                    else {
                        r[col] = Cell(row, col, getDomain(row, col, board).toSet())
                    }
                }

                superpositions[row] = r
            }

            return superpositions
        }

        fun getDomain(row: Int, col: Int, board: Array<Array<Int?>>): Set<Int> {
            val colValues = board[row].filterNotNull()
            val rowCells = (board.map { it[col] }).filterNotNull()
            val defaultDomain = 1..board[0].size

            return defaultDomain.toSet() - (colValues.toSet() union rowCells.toSet())
        }
    }
}