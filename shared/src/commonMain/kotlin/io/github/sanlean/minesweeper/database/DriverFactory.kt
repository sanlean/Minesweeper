package io.github.sanlean.minesweeper.database

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
  suspend fun createDriver(): SqlDriver
}