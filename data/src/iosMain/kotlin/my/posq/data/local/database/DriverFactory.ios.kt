package my.posq.data.local.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import my.posq.PosQDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(PosQDatabase.Schema, "posq.db")
}
