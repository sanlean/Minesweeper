package io.github.sanlean.minesweeper.settings

import kotlinx.datetime.LocalDateTime

data class Score(
    val time: Long,
    val name: String,
    val boardWidth: Int,
    val boardHeight: Int,
    val totalOfMines: Int,
    val date: LocalDateTime
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Score) return false

        return time == other.time &&
                name == other.name &&
                boardWidth == other.boardWidth &&
                boardHeight == other.boardHeight &&
                totalOfMines == other.totalOfMines &&
                date == other.date
    }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + boardWidth
        result = 31 * result + boardHeight
        result = 31 * result + totalOfMines
        result = 31 * result + date.hashCode()
        return result
    }
}