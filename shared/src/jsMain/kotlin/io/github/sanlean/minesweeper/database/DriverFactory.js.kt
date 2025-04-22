package io.github.sanlean.minesweeper.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import io.github.sanlean.minesweeper.Database
import org.w3c.dom.Worker

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return WebWorkerDriver(
            Worker(
                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            )
        ).also { Database.Schema.create(it).await() }
    }
}