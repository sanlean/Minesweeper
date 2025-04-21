package io.github.sanlean.minesweeper.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CellStateTest {

    @Test
    fun `CellState should contain all expected values`() {
        val expectedValues = listOf(
            CellState.HIDDEN,
            CellState.FLAG,
            CellState.QUESTIONED,
            CellState.SAFE,
            CellState.ADJACENT_1,
            CellState.ADJACENT_2,
            CellState.ADJACENT_3,
            CellState.ADJACENT_4,
            CellState.ADJACENT_5,
            CellState.ADJACENT_6,
            CellState.ADJACENT_7,
            CellState.ADJACENT_8,
            CellState.BOMB_SAFE,
            CellState.BOMB_TRIGGERED
        )
        assertEquals(expectedValues.size, CellState.values().size, "Unexpected number of enum values")
        assertTrue(CellState.values().toList().containsAll(expectedValues), "Enum values mismatch")
    }

    @Test
    fun `CellState has unique ordinal values`() {
        val ordinals = CellState.values().map { it.ordinal }
        assertEquals(ordinals.size, ordinals.distinct().size, "Duplicate ordinal values found in CellState")
    }
}
