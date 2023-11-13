package com.garioncox

class Wave {
    companion object {
        fun collapse(board: Array<Array<Int?>>): Array<Array<Int>> {
            val rowCount = board.size
            val colCount = board[0].size

            val superimposedBoard = Array(rowCount) { arrayOf<Cell>() }

            for (row in 0..< rowCount) {
                val r = Array(colCount) { Cell(0, 0, listOf()) }

                for (col in 0..< colCount) {
                    val currentCell = board[row][col]
                    if (currentCell != null) {
                        r[col] = Cell(row, col, listOf(currentCell))
                    }
                    else {
                        r[col] = Cell(row, col, getDomain(row, col, board))
                    }



                }

                superimposedBoard[row] = r
            }

            return arrayOf(arrayOf())
        }

        fun getDomain(row: Int, col: Int, board: Array<Array<Int?>>): Collection<Int> {
            val colValues = board[row].filterNotNull()
            val rowCells = (board.map { it[col] }).filterNotNull()
            val defaultDomain = 1..board[0].size

            return defaultDomain.toSet() - (colValues.toSet() union rowCells.toSet())
        }

        fun propogate() {

        }
    }
}