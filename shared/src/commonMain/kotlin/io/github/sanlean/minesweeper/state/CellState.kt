package io.github.sanlean.minesweeper.state

enum class CellState {
    HIDDEN,
    FLAG,
    QUESTIONED,
    SAFE,
    ADJACENT_1,
    ADJACENT_2,
    ADJACENT_3,
    ADJACENT_4,
    ADJACENT_5,
    ADJACENT_6,
    ADJACENT_7,
    ADJACENT_8,
    BOMB_TRIGGERED,
    BOMB_SAFE
}