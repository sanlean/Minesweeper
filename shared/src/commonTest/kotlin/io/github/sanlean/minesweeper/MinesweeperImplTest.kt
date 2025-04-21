package io.github.sanlean.minesweeper

import io.github.sanlean.minesweeper.settings.MockSaveAdapter
import io.github.sanlean.minesweeper.settings.SaveAdapter
import io.github.sanlean.minesweeper.state.CellState
import io.github.sanlean.minesweeper.state.GameState
import kotlin.test.*

class MinesweeperImplTest {
    private val saveAdapter: SaveAdapter = MockSaveAdapter()

    @Test
    fun `should initialize board with correct dimensions`() {
        val minesweeper = MinesweeperImpl(10, 10, listOf(), saveAdapter)
        assertEquals(10, minesweeper.currentBoard.size)
        assertEquals(10, minesweeper.currentBoard[0].size)
    }

    @Test
    fun `should place mines correctly on the board`() {
        val mines = listOf(Position(0, 0), Position(1, 1), Position(2, 2))
        val minesweeper = MinesweeperImpl(5, 5, mines, saveAdapter)

        for (mine in mines) {
            assertEquals(minesweeper.currentBoard[mine.first][mine.second], CellState.HIDDEN)
        }
    }

    @Test
    fun `should return RUNNING state at the start`() {
        val minesweeper = MinesweeperImpl(10, 10, listOf(), saveAdapter)
        assertEquals(GameState.RUNNING, minesweeper.isGameOver())
    }

    @Test
    fun `should return LOOSE state when a mine is clicked`() {
        val mines = listOf(Position(0, 0))
        val minesweeper = MinesweeperImpl(5, 5, mines, saveAdapter)

        minesweeper.click(0, 0)
        assertEquals(GameState.LOOSE, minesweeper.isGameOver())
    }

    @Test
    fun `should return WIN state when all safe cells are clicked`() {
        val mines = listOf(Position(0, 0))
        val minesweeper = MinesweeperImpl(2, 2, mines, saveAdapter)

        minesweeper.click(0, 1)
        minesweeper.click(1, 0)
        minesweeper.click(1, 1)

        assertEquals(GameState.WIN, minesweeper.isGameOver())
    }

    @Test
    fun `should reveal empty cells recursively when clicked`() {
        val mines = listOf(Position(0, 0))
        val minesweeper = MinesweeperImpl(3, 3, mines, saveAdapter)

        minesweeper.click(2, 2)

        assertEquals(CellState.BOMB_SAFE, minesweeper.currentBoard[0][0])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[0][1])
        assertEquals(CellState.SAFE, minesweeper.currentBoard[0][2])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[1][0])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[1][1])
        assertEquals(CellState.SAFE, minesweeper.currentBoard[1][2])
        assertEquals(CellState.SAFE, minesweeper.currentBoard[2][0])
        assertEquals(CellState.SAFE, minesweeper.currentBoard[2][1])
        assertEquals(CellState.SAFE, minesweeper.currentBoard[2][2])
    }

    @Test
    fun `should mark cell as flagged`() {
        val minesweeper = MinesweeperImpl(5, 5, listOf(), saveAdapter)

        val result = minesweeper.clickAsFlagged(2, 2)
        assertTrue(result)
        assertEquals(CellState.FLAG, minesweeper.currentBoard[2][2])
    }

    @Test
    fun `should mark cell as questioned`() {
        val minesweeper = MinesweeperImpl(5, 5, listOf(), saveAdapter)

        val result = minesweeper.clickAsQuestioned(2, 2)
        assertTrue(result)
        assertEquals(CellState.QUESTIONED, minesweeper.currentBoard[2][2])
    }

    @Test
    fun `should not allow clicking flagged cell`() {
        val minesweeper = MinesweeperImpl(5, 5, listOf(), saveAdapter)

        minesweeper.clickAsFlagged(2, 2)
        val result = minesweeper.click(2, 2)

        assertFalse(result)
        assertEquals(CellState.FLAG, minesweeper.currentBoard[2][2])
    }

    @Test
    fun `should calculate numbers around mines correctly`() {
        val mines = listOf(Position(1, 1))
        val minesweeper = MinesweeperImpl(3, 3, mines, saveAdapter)

        minesweeper.click(0, 0)
        minesweeper.click(0, 1)
        minesweeper.click(0, 2)
        minesweeper.click(1, 0)
        minesweeper.click(1, 2)
        minesweeper.click(2, 0)
        minesweeper.click(2, 1)
        minesweeper.click(2, 2)

        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[0][0])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[0][1])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[0][2])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[1][0])
        assertEquals(CellState.BOMB_SAFE, minesweeper.currentBoard[1][1])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[1][2])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[2][0])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[2][1])
        assertEquals(CellState.ADJACENT_1, minesweeper.currentBoard[2][2])
    }

    @Test
    fun `should not allow clicking outside the board`() {
        val minesweeper = MinesweeperImpl(5, 5, listOf(), saveAdapter)

        assertFails{
            minesweeper.click(5, 5)
        }
    }

    @Test
    fun `should not allow marking outside the board`() {
        val minesweeper = MinesweeperImpl(5, 5, listOf(), saveAdapter)

        assertFails{
            minesweeper.clickAsFlagged(5, 5)
        }
    }

    @Test
    fun `should not allow questioning outside the board`() {
        val minesweeper = MinesweeperImpl(5, 5, listOf(), saveAdapter)

        assertFails{
            minesweeper.clickAsQuestioned(5, 5)
        }
    }

    @Test
    fun `should not allow clicking already revealed cell`() {
        val minesweeper = MinesweeperImpl(5, 5, listOf(), saveAdapter)

        minesweeper.click(2, 2)
        val result = minesweeper.click(2, 2)

        assertFalse(result)
    }
}