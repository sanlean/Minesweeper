package io.github.sanlean.minesweeper.database

import io.github.sanlean.minesweeper.Database
import io.github.sanlean.minesweeper.settings.SaveAdapter
import io.github.sanlean.minesweeper.settings.Score
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SQLSaveAdapter(
    private val database: Database
) : SaveAdapter {
    private val scope = CoroutineScope(Dispatchers.Default)

    private suspend fun getOrCreateCategory(
        boardWidth: Int,
        boardHeight: Int,
        totalOfMines: Int
    ): Category {
        val category = database.categoryQueries.selectByDimensions(
            boardWidth.toLong(),
            boardHeight.toLong(),
            totalOfMines.toLong()
        ).executeAsOneOrNull()
        if (category != null) {
            return category
        }
        database.categoryQueries.insert(
            boardWidth = boardWidth.toLong(),
            boardHeight = boardHeight.toLong(),
            totalOfMines = totalOfMines.toLong()
        )
        return database.categoryQueries.selectByDimensions(
            boardWidth.toLong(),
            boardHeight.toLong(),
            totalOfMines.toLong()
        ).executeAsOne()
    }

    override fun saveHighScore(score: Score) {
        val coroutine: suspend CoroutineScope.() -> Unit = {
            val category = getOrCreateCategory(
                score.boardWidth,
                score.boardHeight,
                score.totalOfMines
            )
            database.highScoresQueries.insert(
                categoryId = category.id,
                time = score.time,
                date = score.date,
                name = score.name
            )
        }
        scope.launch(block = coroutine)
    }

    override fun clearHighScore() {
        val coroutine: suspend CoroutineScope.() -> Unit = {
            database.highScoresQueries.deleteAll()
        }
        scope.launch(block = coroutine)
    }

    override fun retrieveHighScore(boardWidth: Int, boardHeight: Int, mines: Int): List<Score> {
        val category = database.categoryQueries.selectByDimensions(
            boardWidth.toLong(),
            boardHeight.toLong(),
            mines.toLong()
        ).executeAsOneOrNull()
        if (category == null) {
            return emptyList()
        }
        return database.highScoresQueries.selectByCatagoryId(category.id)
            .executeAsList()
            .map {
                Score(
                    time = it.time,
                    name = it.name,
                    boardWidth = boardWidth,
                    boardHeight = boardHeight,
                    totalOfMines = mines,
                    date = it.date
                )
            }
    }

    override fun removeHighScore(score: Score) {
        val coroutine: suspend CoroutineScope.() -> Unit = {
            val category = getOrCreateCategory(
                score.boardWidth,
                score.boardHeight,
                score.totalOfMines
            )

            val highScore = database.highScoresQueries.selectByCatagoryId(category.id)
                .executeAsList()
                .find {
                    it.time == score.time && it.name == score.name && it.date == score.date
                }

            highScore?.let {
                database.highScoresQueries.delete(it.id)
            }
        }
        scope.launch(block = coroutine)
    }

    override fun retrieveAllHighScores(): Map<String, List<Score>> {
        val categories = database.categoryQueries.selectAll().executeAsList()
        val highScores = database.highScoresQueries.selectAll().executeAsList()

        return categories.associate { category ->
            val scores = highScores.filter { it.categoryId == category.id }
                .map {
                    Score(
                        time = it.time,
                        name = it.name,
                        boardWidth = category.boardWidth.toInt(),
                        boardHeight = category.boardHeight.toInt(),
                        totalOfMines = category.totalOfMines.toInt(),
                        date = it.date
                    )
                }
            "${category.boardWidth}x${category.boardHeight} - ${category.totalOfMines} mines" to scores
        }
    }
}