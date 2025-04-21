package io.github.sanlean.minesweeper

import io.github.sanlean.minesweeper.settings.Save
import io.github.sanlean.minesweeper.settings.SaveAdapter
import io.github.sanlean.minesweeper.state.CellState
import io.github.sanlean.minesweeper.state.GameState

internal class MinesweeperImpl(
    private val width: Int,
    private val height: Int,
    private val mines: List<Position>,
    saveAdapter: SaveAdapter
) : Minesweeper {
    private val board: Matrix<Pair<CellState, Boolean>>
    private val map: HashMap<Position, Int>
    override val currentBoard: Board
        get() = board.map { row -> row.map { it.first }.toTypedArray() }.toTypedArray()
    override val save = Save(saveAdapter)

    init {
        board = newBoard(width, height)
        map = HashMap()
        populateMinesToBoard()
        populateCountOfMinesInMap()
    }

    private fun newBoard(width: Int, height: Int) =
        Array(width) { Array(height) { CellState.HIDDEN to false } }

    private fun populateCountOfMinesInMap() {
        board.forEachIndex { i, j, cell ->
            val isMine = cell.second
            if (isMine){
                for (neighbourPosition in getPositionsAround(i, j)) {
                    map.increaseOneInPosition(neighbourPosition.first, neighbourPosition.second)
                }
            }
        }
    }

    private fun populateMinesToBoard() {
        for ((x, y) in mines) {
            board[x][y] = CellState.HIDDEN to true
        }
    }

    private fun getPositionsAround(x: Int, y: Int): List<Position> {
        return mutableListOf<Position>().apply {
            for (i in x - 1..x + 1) {
                for (j in y - 1..y + 1) {
                    val isInsideBoard = i >= 0 && j >= 0 && i < width && j < height
                    val isNotMine = mines.contains(i to j).not()
                    if (isInsideBoard && isNotMine) {
                        add(Position(i, j))
                    }
                }
            }
        }
    }

    private fun updateBoardAfterFinishingGame() {
        for ((x, y) in mines){
            val state = board[x][y].first
            if (state != CellState.BOMB_TRIGGERED){
                board[x][y] = CellState.BOMB_SAFE to true
            }
        }
    }

    private fun HashMap<Position, Int>.increaseOneInPosition(x: Int, y: Int) {
        val position = Position(x, y)
        put(position, minesAroundPosition(position) + 1)
    }

    private fun HashMap<Position, Int>.minesAroundPosition(position: Position): Int {
        return getOrElse(position) { 0 }
    }

    override fun click(x: Int, y: Int): Boolean {
        val state = board[x][y].first
        val isMine = board[x][y].second
        if (state == CellState.HIDDEN) {
            if (isMine) {
                board[x][y] = CellState.BOMB_TRIGGERED to true
            } else {
                updateEmptyCellsAroundPosition(x, y)
            }
            val gameStateAfterClick = isGameOver()
            if (gameStateAfterClick == GameState.LOOSE || gameStateAfterClick == GameState.WIN){
                updateBoardAfterFinishingGame()
            }
            return true
        }
        return false
    }

    override fun clickAsQuestioned(x: Int, y: Int): Boolean {
        val state = board[x][y].first
        val isMine = board[x][y].second
        if (state == CellState.HIDDEN || state == CellState.FLAG) {
            board[x][y] = CellState.QUESTIONED to isMine
            return true
        }else if (state == CellState.QUESTIONED) {
            board[x][y] = CellState.HIDDEN to isMine
            return true
        }
        return false
    }

    override fun clickAsFlagged(x: Int, y: Int): Boolean {
        val state = board[x][y].first
        val isMine = board[x][y].second
        if (state == CellState.HIDDEN || state == CellState.QUESTIONED) {
            board[x][y] = CellState.FLAG to isMine
            return true
        }else if (state == CellState.FLAG) {
            board[x][y] = CellState.HIDDEN to isMine
            return true
        }
        return false
    }

    override fun isGameOver(): GameState {
        for (line in board) {
            for ((state, isMine) in line) {
                if (state == CellState.BOMB_TRIGGERED) {
                    return GameState.LOOSE
                } else if (!isMine && state == CellState.HIDDEN) {
                    return GameState.RUNNING
                }
            }
        }
        return GameState.WIN
    }

    private fun updateEmptyCellsAroundPosition(x: Int, y: Int) {
        when (map.minesAroundPosition(Position(x, y))) {
            1 -> board[x][y] = CellState.ADJACENT_1 to false
            2 -> board[x][y] = CellState.ADJACENT_2 to false
            3 -> board[x][y] = CellState.ADJACENT_3 to false
            4 -> board[x][y] = CellState.ADJACENT_4 to false
            5 -> board[x][y] = CellState.ADJACENT_5 to false
            6 -> board[x][y] = CellState.ADJACENT_6 to false
            7 -> board[x][y] = CellState.ADJACENT_7 to false
            8 -> board[x][y] = CellState.ADJACENT_8 to false
            else -> {
                board[x][y] = CellState.SAFE to false
                for ((i, j) in getPositionsAround(x, y)) {
                    if (board[i][j].first == CellState.HIDDEN) {
                        updateEmptyCellsAroundPosition(i, j)
                    }
                }
            }
        }
    }

}