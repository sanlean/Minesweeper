package io.github.sanlean.minesweeper.settings

class MockSaveAdapter: SaveAdapter {
    override fun saveHighScore(score: Score) = Unit

    override fun clearHighScore() = Unit

    override fun retrieveHighScore(boardWidth: Int, boardHeight: Int, mines: Int): List<Score> = emptyList()

    override fun removeHighScore(score: Score) = Unit

    override fun retrieveAllHighScores(): Map<String, List<Score>> = HashMap()
}