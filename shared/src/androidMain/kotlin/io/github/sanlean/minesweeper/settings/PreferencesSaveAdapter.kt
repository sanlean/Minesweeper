package io.github.sanlean.minesweeper.settings

import android.content.Context
import android.content.SharedPreferences
import io.github.sanlean.minesweeper.settings.Score
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesSaveAdapter private constructor(private val context: Context): SaveAdapter {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("highscores", Context.MODE_PRIVATE)
    }

    override fun saveHighScore(score: Score) {
        val key = generateKey(score.boardWidth, score.boardHeight, score.totalOfMines)
        val highScores = retrieveHighScore(score.boardWidth, score.boardHeight, score.totalOfMines).toMutableList()
        highScores.add(score)
        val sortedHighScores = highScores.sortedBy { it.time }.take(10)
        sharedPreferences.edit().putString(key, Json.encodeToString(sortedHighScores)).apply()
    }

    override fun clearHighScore() {
        sharedPreferences.edit().clear().apply()
    }

    override fun retrieveHighScore(boardWidth: Int, boardHeight: Int, mines: Int): List<Score> {
        val key = generateKey(boardWidth, boardHeight, mines)
        val json = sharedPreferences.getString(key, null) ?: return emptyList()
        return Json.decodeFromString(json)
    }

    override fun removeHighScore(score: Score) {
        val key = generateKey(score.boardWidth, score.boardHeight, score.totalOfMines)
        val highScores = retrieveHighScore(score.boardWidth, score.boardHeight, score.totalOfMines).toMutableList()
        highScores.removeIf { it == score }
        sharedPreferences.edit().putString(key, Json.encodeToString(highScores)).apply()
    }

    override fun retrieveAllHighScores(): Map<String, List<Score>> {
        val allEntries = sharedPreferences.all
        val result = mutableMapOf<String, List<Score>>()
        for ((key, value) in allEntries) {
            if (value is String && isValidHighScoreKey(key)) {
                try {
                    val highScores = Json.decodeFromString<List<Score>>(value)
                    result[key] = highScores
                } catch (_: Exception) { }
            }
        }
        return result
    }

    private fun isValidHighScoreKey(key: String): Boolean {
        val parts = key.split("-")
        if (parts.size != 3) return false
        return parts.all { it.toIntOrNull() != null }
    }

    private fun generateKey(boardWidth: Int, boardHeight: Int, mines: Int): String {
        return "$boardWidth-$boardHeight-$mines"
    }

    companion object{
        fun create(context: Context) = PreferencesSaveAdapter(context)
    }
}