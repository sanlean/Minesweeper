package io.github.sanlean.minesweeper.settings

interface SaveAdapter {
    fun saveHighScore(score: Score)
    fun clearHighScore()
    fun retrieveHighScore(boardWidth: Int, boardHeight: Int, mines: Int): List<Score>
    fun removeHighScore(score: Score)
    fun  retrieveAllHighScores(): Map<String, List<Score>>
}