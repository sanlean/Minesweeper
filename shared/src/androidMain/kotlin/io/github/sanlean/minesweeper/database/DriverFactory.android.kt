package io.github.sanlean.minesweeper.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.github.sanlean.minesweeper.Database

actual class DriverFactory(private val context: Context) {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema.synchronous(), context, "test.db")
    }
}