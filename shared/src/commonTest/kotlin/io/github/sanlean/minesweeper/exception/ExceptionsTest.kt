package io.github.sanlean.minesweeper.exception

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ExceptionsTest {

    @Test
    fun `InvalidBoardSizeException stores the message correctly`() {
        val message = "Invalid board size: width and height must be at least 1"
        val exception = InvalidBoardSizeException(message)
        val assertIs = assertIs<IllegalStateException>(exception)
        assertEquals(message, exception.message)
    }

    @Test
    fun `InvalidMineCountException stores the message correctly`() {
        val message = "Invalid mine count: must be between 1 and total cells"
        val exception = InvalidMineCountException(message)
        assertIs<IllegalStateException>(exception)
        assertEquals(message, exception.message)
    }
}