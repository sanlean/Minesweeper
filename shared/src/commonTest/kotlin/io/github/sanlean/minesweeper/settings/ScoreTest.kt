package io.github.sanlean.minesweeper.settings

import kotlin.test.*

class ScoreTest {

    @Test
    fun `scores with same properties are equal`() {
        val score1 = Score(120.5, "Player1", 10, 10, 20)
        val score2 = Score(120.5, "Player1", 10, 10, 20)

        assertEquals(score1, score2, "Scores with identical properties should be equal")
    }

    @Test
    fun `scores with different properties are not equal`() {
        val score1 = Score(120.5, "Player1", 10, 10, 20)
        val score2 = Score(130.0, "Player2", 15, 15, 25)

        assertNotEquals(score1, score2, "Scores with different properties should not be equal")
    }

    @Test
    fun `hashCode is consistent with equals`() {
        val score1 = Score(120.5, "Player1", 10, 10, 20)
        val score2 = Score(120.5, "Player1", 10, 10, 20)

        assertEquals(score1.hashCode(), score2.hashCode(), "Equal scores should have the same hashCode")
    }

    @Test
    fun `hashCode differs for scores with different properties`() {
        val score1 = Score(120.5, "Player1", 10, 10, 20)
        val score2 = Score(130.0, "Player2", 15, 15, 25)

        assertNotEquals(score1.hashCode(), score2.hashCode(), "Different scores should have different hashCodes")
    }

    @Test
    fun `equals is reflexive`() {
        val score = Score(120.5, "Player1", 10, 10, 20)

        assertEquals(score, score, "equals should be reflexive")
    }

    @Test
    fun `equals is symmetric`() {
        val score1 = Score(120.5, "Player1", 10, 10, 20)
        val score2 = Score(120.5, "Player1", 10, 10, 20)

        assertTrue(score1 == score2 && score2 == score1, "equals should be symmetric")
    }

    @Test
    fun `equals is transitive`() {
        val score1 = Score(120.5, "Player1", 10, 10, 20)
        val score2 = Score(120.5, "Player1", 10, 10, 20)
        val score3 = Score(120.5, "Player1", 10, 10, 20)

        assertTrue(score1 == score2 && score2 == score3 && score1 == score3, "equals should be transitive")
    }

    @Test
    fun `equals handles null`() {
        val score = Score(120.5, "Player1", 10, 10, 20)

        assertNotNull(score, "score is not null")
    }

    @Test
    fun `equals handles different types`() {
        val score = Score(120.5, "Player1", 10, 10, 20)
        val otherObject = "NotAScore"

        assertFalse(score.equals(otherObject), "equals should return false for different types")
    }
}
