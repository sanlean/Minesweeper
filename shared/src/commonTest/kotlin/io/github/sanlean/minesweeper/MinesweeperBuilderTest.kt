package io.github.sanlean.minesweeper

import io.github.sanlean.minesweeper.exception.InvalidMineCountException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MinesweeperBuilderTest {

    @Test
    fun `should build Minesweeper with valid dimensions mine count and random mine positions`() {
        val builder = Minesweeper.Builder().apply {
            width(20)
            height(15)
            totalOfMines(45)
            randomMinesPositions()
        }
        val minesweeper = builder.build()
        assertNotNull(minesweeper)
    }

    @Test
    fun `should throw InvalidMineCountException when mines list is empty`() {
        val builder = Minesweeper.Builder().apply {
            width(20)
            height(15)
            totalOfMines(45)
        }
        assertFailsWith<InvalidMineCountException> { builder.build() }
    }

    @Test
    fun `should throw InvalidMineCountException when totalOfMines does not match mines list size`() {
        val builder = Minesweeper.Builder().apply {
            width(20)
            height(15)
            totalOfMines(45)
            minesPositions(listOf(Position(0, 0), Position(1, 1))) // Only 2 mines provided
        }
        assertFailsWith<InvalidMineCountException> { builder.build() }
    }

    @Test
    fun `should build Minesweeper with manually set mine positions`() {
        val builder = Minesweeper.Builder().apply {
            width(10)
            height(10)
            totalOfMines(10)
            minesPositions(
                listOf(
                    Position(0, 0),
                    Position(1, 1),
                    Position(2, 2),
                    Position(3, 3),
                    Position(4, 4),
                    Position(5, 5),
                    Position(6, 6),
                    Position(7, 7),
                    Position(8, 8),
                    Position(9, 9)
                )
            )
        }
        val minesweeper = builder.build()
        assertNotNull(minesweeper)
    }

    @Test
    fun `should throw InvalidMineCountException when randomMinesPositions generates incorrect count`() {
        val builder = Minesweeper.Builder().apply {
            width(10)
            height(10)
            totalOfMines(105)
            randomMinesPositions()
        }
        assertFailsWith<InvalidMineCountException> { builder.build() }
    }

    @Test
    fun `should throw InvalidMineCountException when totalOfMines is less than mines list size`() {
        val builder = Minesweeper.Builder().apply {
            width(10)
            height(10)
            totalOfMines(3) // Less than the size of the provided mines list
            minesPositions(
                listOf(
                    Position(0, 0),
                    Position(1, 1),
                    Position(2, 2),
                    Position(3, 3),
                    Position(4, 4)
                )
            )
        }
        assertFailsWith<InvalidMineCountException> { builder.build() }
    }

    @Test
    fun `should validate randomMinesPositions generates unique positions`() {
        val builder = Minesweeper.Builder().apply {
            width(10)
            height(10)
            totalOfMines(10)
            randomMinesPositions()
        }
        val minesweeper = builder.build()
        val uniquePositions = minesweeper.currentBoard.distinct()
        assertTrue("Random mine positions should be unique") { uniquePositions.size == minesweeper.currentBoard.size }
    }
}