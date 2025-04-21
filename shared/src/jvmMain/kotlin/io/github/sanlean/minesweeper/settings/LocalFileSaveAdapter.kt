package io.github.sanlean.minesweeper.settings

import kotlinx.serialization.json.Json
import java.io.File

class LocalFileSaveAdapter private constructor(private val storageDir: File): SaveAdapter {

    private val file: File by lazy {
        File(storageDir, "highscores.sav").apply {
            if (!exists()) {
                createNewFile()
                writeText("{}")
            }
        }
    }

    override fun saveHighScore(score: Score) {
        val key = generateKey(score.boardWidth, score.boardHeight, score.totalOfMines)
        val allHighScores = retrieveAllHighScores().toMutableMap()
        val highScores = allHighScores[key]?.toMutableList() ?: mutableListOf()
        highScores.add(score)
        val sortedHighScores = highScores.sortedBy { it.time }.take(10)
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
        highScores.removeIf { it == score }
        if (highScores.isEmpty()) {
            allHighScores.remove(key)
        } else {
            allHighScores[key] = highScores
        }
        saveToFile(allHighScores)
    }

    override fun retrieveAllHighScores(): Map<String, List<Score>> {
        return try {
            val json = file.readText()
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun saveToFile(data: Map<String, List<Score>>) {
        val json = Json.encodeToString(data)
        file.writeText(json)
    }

    private fun generateKey(boardWidth: Int, boardHeight: Int, mines: Int): String {
        return "$boardWidth-$boardHeight-$mines"
    }

    companion object{
        fun create(storagePath: File) = LocalFileSaveAdapter(storagePath)
    }
}