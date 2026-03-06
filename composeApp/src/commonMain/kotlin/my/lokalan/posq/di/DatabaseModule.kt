package my.lokalan.posq.di

import my.posq.data.local.database.DatabaseHelper
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseHelper(get()) }
}
