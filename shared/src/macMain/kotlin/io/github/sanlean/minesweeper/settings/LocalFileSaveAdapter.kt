package io.github.sanlean.minesweeper.settings

import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

class LocalFileSaveAdapter private constructor(private val storageDir: String): SaveAdapter {

    private val fileSystem = FileSystem.SYSTEM
    private val file: Path by lazy {
        val path = "$storageDir/highscores.sav".toPath()
        if (!fileSystem.exists(path)) {
            fileSystem.write(path) {
                writeUtf8("{}") // Inicializa o arquivo com um JSON vazio
            }
        }
        path
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
        val updatedHighScores = highScores.filter { it != score }
        if (updatedHighScores.isEmpty()) {
            allHighScores.remove(key)
        } else {
            allHighScores[key] = updatedHighScores
        }
        saveToFile(allHighScores)
    }

    override fun retrieveAllHighScores(): Map<String, List<Score>> {
        return try {
            val json = fileSystem.read(file) {
                readUtf8()
            }
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun saveToFile(data: Map<String, List<Score>>) {
        val json = Json.encodeToString(data)
        fileSystem.write(file) {
            writeUtf8(json)
        }
    }

    private fun generateKey(boardWidth: Int, boardHeight: Int, mines: Int): String {
        return "$boardWidth-$boardHeight-$mines"
    }

    companion object{
        fun create(storagePath: String) = LocalFileSaveAdapter(storagePath)
    }
}
