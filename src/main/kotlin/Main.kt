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

fun main() {
    val sudokuBoard = arrayOf(
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
//
//    val sudokuBoard = arrayOf(
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
//        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
//    )

    if (SudokuSolver.solveSudoku(sudokuBoard)) {
        println("Sudoku Solved:")

        for (row in 0..< sudokuBoard.size) {
            if (((row) % 3 == 0)) {
                printHR(sudokuBoard.size + 3)
            }

            for (col in 0..< sudokuBoard[row].size) {
                if (((col) % 3 == 0)) {
                    print("| ")
                }
                print(sudokuBoard[row][col].toString() + " ")
                if (col == sudokuBoard[row].size - 1) {
                    print("| ")
                }
            }

            println()
            if (row == sudokuBoard[row].size - 1) {
                printHR(sudokuBoard.size + 3)
            }
        }
    }
    else {
        println("No solution exists.")
    }
}

fun printHR(end: Int) {
    for (i in 0..end) {
        print("- ")
    }
    println()
}