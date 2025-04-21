package io.github.sanlean.minesweeper

import io.github.sanlean.minesweeper.exception.InvalidBoardSizeException
import io.github.sanlean.minesweeper.exception.InvalidMineCountException
import io.github.sanlean.minesweeper.settings.DefaultNotSaveAdapter
import io.github.sanlean.minesweeper.settings.Save
import io.github.sanlean.minesweeper.settings.SaveAdapter
import io.github.sanlean.minesweeper.state.GameState
import kotlin.random.Random

interface Minesweeper {
    fun click(x: Int, y: Int): Boolean
    fun clickAsQuestioned(x: Int, y: Int): Boolean
    fun clickAsFlagged(x: Int, y: Int): Boolean
    fun isGameOver(): GameState
    val currentBoard: Board
    val save: Save

    class Builder {
        private var width: Int = 0
        private var height: Int = 0
        private var totalOfMines: Int = 0
        private var mines: List<Position> = listOf()
        private var randomMines = false
        private var saveAdapter: SaveAdapter = DefaultNotSaveAdapter()

        fun width(width: Int) {
            this.width = width
        }

        fun height(height: Int) {
            this.height = height
        }

        fun totalOfMines(mines: Int) {
            this.totalOfMines = mines
        }

        fun randomMinesPositions() {
            randomMines = true
        }

        fun minesPositions(list: List<Position>){
            mines = list
        }

        fun saveStorage(saveAdapter: SaveAdapter){
            this.saveAdapter = saveAdapter
        }

        private fun createMinesPositions() {
            mines = mutableSetOf<Position>().apply {
                while (size < totalOfMines) {
                    val x = Random.nextInt(width)
                    val y = Random.nextInt(height)
                    add(Position(x, y))
                }
            }.toList()
        }

        fun build(): Minesweeper {
            validateBoardSize()
            validateMinesSize()
            return MinesweeperImpl(width, height, mines, saveAdapter)
        }

        private fun validateBoardSize(){
            if (width < MINIMUM_WIDTH || width > MAXIMUM_WIDTH) {
                throw InvalidBoardSizeException(ERROR_WIDTH)
            }
            if (height < MINIMUM_HEIGHT || height > MAXIMUM_HEIGHT) {
                throw InvalidBoardSizeException(ERROR_HEIGHT)
            }
        }

        private fun validateMinesSize(){
            val minMines = (width * height) * MINIMUM_MINES_PERCENTAGE
            val maxMines = (width * height) * MAXIMUM_MINES_PERCENTAGE
            if (mines.isEmpty() && randomMines.not()) {
                throw InvalidMineCountException("Mines list is empty")
            }
            if (totalOfMines < minMines || totalOfMines > maxMines) {
                throw InvalidMineCountException("Total of mines $totalOfMines must be between $minMines and $maxMines")
            }
            if (randomMines){
                createMinesPositions()
            }
            if (totalOfMines != mines.size) {
                throw InvalidMineCountException("Total of mines $totalOfMines diver from mines list size: ${mines.size}")
            }
        }

        companion object {
            const val MINIMUM_WIDTH = 10
            const val MAXIMUM_WIDTH = 30
            const val MINIMUM_HEIGHT = 10
            const val MAXIMUM_HEIGHT = 20
            const val MINIMUM_MINES_PERCENTAGE = 0.10
            const val MAXIMUM_MINES_PERCENTAGE = 0.25
            private const val ERROR_WIDTH =
                "Width must be between $MINIMUM_WIDTH and $MAXIMUM_WIDTH"
            private const val ERROR_HEIGHT =
                "Height must be beteen $MINIMUM_HEIGHT and $MAXIMUM_HEIGHT"
        }
    }
}