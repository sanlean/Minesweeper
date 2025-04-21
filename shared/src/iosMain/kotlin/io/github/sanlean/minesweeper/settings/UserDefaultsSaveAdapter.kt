package io.github.sanlean.minesweeper.settings

import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class UserDefaultsSaveAdapter private constructor(): SaveAdapter {

    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
    private val fileKey = "highscores.sav"

    override fun saveHighScore(score: Score) {
        val key = generateKey(score.boardWidth, score.boardHeight, score.totalOfMines)
        val allHighScores = retrieveAllHighScores().toMutableMap()
        val highScores = allHighScores[key]?.toMutableList() ?: mutableListOf()
        highScores.add(score)
        val sortedHighScores = highScores.sortedBy { it.time }.take(10) // Mantém apenas os 10 melhores
        allHighScores[key] = sortedHighScores
        saveToFile(allHighScores)
    }

    override fun clearHighScore() {
        saveToFile(emptyMap())
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
        saveToFile(allHighScores)
    }

    override fun retrieveAllHighScores(): Map<String, List<Score>> {
        val json = userDefaults.stringForKey(fileKey) ?: return emptyMap()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun saveToFile(data: Map<String, List<Score>>) {
        val json = Json.encodeToString(data)
        userDefaults.setObject(json, forKey = fileKey)
    }

    private fun generateKey(boardWidth: Int, boardHeight: Int, mines: Int): String {
        return "$boardWidth-$boardHeight-$mines"
    }

    companion object{
        fun create() = UserDefaultsSaveAdapter()
    }
}