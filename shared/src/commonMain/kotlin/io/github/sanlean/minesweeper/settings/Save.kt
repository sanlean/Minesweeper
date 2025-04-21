package io.github.sanlean.minesweeper.settings

class Save internal constructor(
    private val adapter: SaveAdapter
) {
    fun saveHighScore(score: Score) = adapter.saveHighScore(score)
    fun clearHighScore() = adapter.clearHighScore()
    fun retrieveHighScore(boardWidth: Int, boardHeight: Int, mines: Int) =
        adapter.retrieveHighScore(boardWidth, boardHeight, mines)
    fun removeHighScore(score: Score) = adapter.removeHighScore(score)
    fun retrieveAllHighScores(): Map<String, List<Score>> = adapter.retrieveAllHighScores()
}