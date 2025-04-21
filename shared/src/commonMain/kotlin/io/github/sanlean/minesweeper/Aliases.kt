package io.github.sanlean.minesweeper

import io.github.sanlean.minesweeper.state.CellState

typealias Matrix<T> = Array<Array<T>>
typealias Board = Matrix<CellState>
typealias Position = Pair<Int, Int>

fun <T> Matrix<T>.forEachIndex(action: (i: Int, j: Int, value: T) -> Unit) {
    for (i in indices) {
        for (j in this[i].indices) {
            action(i, j, this[i][j])
        }
    }
}