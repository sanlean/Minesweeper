package io.github.sanlean.minesweeper.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameStateTest {

    @Test
    fun `GameState should contain all expected values`() {
        val expectedValues = listOf(
            GameState.RUNNING,
            GameState.LOOSE,
            GameState.WIN
        )

        assertEquals(expectedValues.size, GameState.values().size, "Unexpected number of enum values")
        assertTrue(GameState.values().toList().containsAll(expectedValues), "Enum values mismatch")
    }

    @Test
    fun `GameState has unique ordinal values`() {
        val ordinals = GameState.values().map { it.ordinal }
        assertEquals(ordinals.size, ordinals.distinct().size, "Duplicate ordinal values found in GameState")
    }
}
