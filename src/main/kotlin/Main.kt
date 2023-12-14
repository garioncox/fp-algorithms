import org.garioncox.CollapseSudokuSolver
import org.garioncox.SudokuSolver

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