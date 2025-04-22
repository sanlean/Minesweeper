package io.github.sanlean.minesweeper.database

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDateTime

val localDateTimeAdapter = object : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String): LocalDateTime {
        return LocalDateTime.parse(databaseValue)
    }

    override fun encode(value: LocalDateTime): String {
        return value.toString()
    }
}