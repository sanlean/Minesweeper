package io.github.sanlean.minesweeper.database

import io.github.sanlean.minesweeper.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseFactory(
    private val driverFactory: DriverFactory
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun loadDatabase(loaded: (Database) -> Unit){
        val launch: suspend CoroutineScope.() -> Unit = {
            val database = Database(
                driver = driverFactory.createDriver(),
                HighScoreAdapter = HighScore.Adapter(
                    dateAdapter = localDateTimeAdapter
                )
            )
            loaded.invoke(database)
        }
        scope.launch(block = launch)
    }
}