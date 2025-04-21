package io.github.sanlean.minesweeper.settings

import kotlinx.browser.localStorage
import kotlinx.serialization.json.Json

class LocalStorageSaveAdapter private constructor(): SaveAdapter {

    private val storageKey = "highscores"

    override fun saveHighScore(score: Score) {
        val key = generateKey(score.boardWidth, score.boardHeight, score.totalOfMines)
        val allHighScores = retrieveAllHighScores().toMutableMap()
        val highScores = allHighScores[key]?.toMutableList() ?: mutableListOf()
        highScores.add(score)
        val sortedHighScores = highScores.sortedBy { it.time }.take(10)
        allHighScores[key] = sortedHighScores
        saveToStorage(allHighScores)
    }

    override fun clearHighScore() {
        saveToStorage(emptyMap())
    }

    override fun retrieveHighScore(boardWidth: Int, boardHeight: Int, mines: Int): List<Score> {
        val key = generateKey(boardWidth, boardHeight, mines)
        return retrieveAllHighScores()[key] ?: emptyList()
    }

    override fun removeHighScore(score: Score) {
        val key = generateKey(score.boardWidth, score.boardHeight, score.totalOfMines)
        val allHighScores = retrieveAllHighScores().toMutableMap()
        val highScores = allHighScores[key]?.toMutableList() ?: return
        val updatedHighScores = highScores.filter { it != score }
        if (updatedHighScores.isEmpty()) {
            allHighScores.remove(key)
        } else {
            allHighScores[key] = updatedHighScores
        }
        saveToStorage(allHighScores)
    }

    override fun retrieveAllHighScores(): Map<String, List<Score>> {
        val json = localStorage.getItem(storageKey) ?: return emptyMap()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun saveToStorage(data: Map<String, List<Score>>) {
        val json = Json.encodeToString(data)
        localStorage.setItem(storageKey, json)
    }

    private fun generateKey(boardWidth: Int, boardHeight: Int, mines: Int): String {
        return "$boardWidth-$boardHeight-$mines"
    }

    companion object{
        fun create() = LocalStorageSaveAdapter()
    }
}
