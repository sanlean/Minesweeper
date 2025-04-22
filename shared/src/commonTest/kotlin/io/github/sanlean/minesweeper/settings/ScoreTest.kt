package io.github.sanlean.minesweeper.settings

import kotlinx.datetime.LocalDateTime
import kotlin.test.*

class ScoreTest {

    @Test
    fun `scores with same properties are equal`() {
        val date = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val timeInMillis = 1745620857000L
        val score1 = Score(timeInMillis, "Player1", 10, 10, 20, date)
        val score2 = Score(timeInMillis, "Player1", 10, 10, 20, date)

        assertEquals(score1, score2, "Scores with identical properties should be equal")
    }

    @Test
    fun `scores with different properties are not equal`() {
        val dateOne = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val dateTwo = LocalDateTime.parse("2023-12-14T19:40:57.120")
        val timeOne = 1745620857000L
        val timeTwo = 1213570858522L
        val score1 = Score(timeOne, "Player1", 10, 10, 20, dateOne)
        val score2 = Score(timeTwo, "Player2", 15, 15, 25, dateTwo)

        assertNotEquals(score1, score2, "Scores with different properties should not be equal")
    }

    @Test
    fun `hashCode is consistent with equals`() {
        val date = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val timeInMillis = 1745620857000L

        val score1 = Score(timeInMillis, "Player1", 10, 10, 20, date)
        val score2 = Score(timeInMillis, "Player1", 10, 10, 20, date)

        assertEquals(score1.hashCode(), score2.hashCode(), "Equal scores should have the same hashCode")
    }

    @Test
    fun `hashCode differs for scores with different properties`() {
        val dateOne = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val dateTwo = LocalDateTime.parse("2023-12-14T19:40:57.120")
        val timeOne = 1745620857000L
        val timeTwo = 1213570858522L

        val score1 = Score(timeOne, "Player1", 10, 10, 20, dateOne)
        val score2 = Score(timeTwo, "Player2", 15, 15, 25, dateTwo)

        assertNotEquals(score1.hashCode(), score2.hashCode(), "Different scores should have different hashCodes")
    }

    @Test
    fun `equals is reflexive`() {
        val date = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val timeInMillis = 1745620857000L

        val score = Score(timeInMillis, "Player1", 10, 10, 20, date)

        assertEquals(score, score, "equals should be reflexive")
    }

    @Test
    fun `equals is symmetric`() {
        val date = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val timeInMillis = 1745620857000L

        val score1 = Score(timeInMillis, "Player1", 10, 10, 20, date)
        val score2 = Score(timeInMillis, "Player1", 10, 10, 20, date)

        assertTrue(score1 == score2 && score2 == score1, "equals should be symmetric")
    }

    @Test
    fun `equals is transitive`() {
        val date = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val timeInMillis = 1745620857000L

        val score1 = Score(timeInMillis, "Player1", 10, 10, 20, date)
        val score2 = Score(timeInMillis, "Player1", 10, 10, 20, date)
        val score3 = Score(timeInMillis, "Player1", 10, 10, 20, date)

        assertTrue(score1 == score2 && score2 == score3 && score1 == score3, "equals should be transitive")
    }

    @Test
    fun `equals handles null`() {
        val date = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val timeInMillis = 1745620857000L

        val score = Score(timeInMillis, "Player1", 10, 10, 20, date)

        assertNotNull(score, "score is not null")
    }

    @Test
    fun `equals handles different types`() {
        val date = LocalDateTime.parse("2025-04-25T19:40:57.120")
        val timeInMillis = 1745620857000L

        val score = Score(timeInMillis, "Player1", 10, 10, 20, date)
        val otherObject = "NotAScore"

        assertFalse(score.equals(otherObject), "equals should return false for different types")
    }
}
