package my.lokalan.posq.di

import com.russhwolf.settings.Settings
import my.posq.data.domain.repository.Repository
import my.posq.data.local.session.Session
import my.posq.data.network.HttpClientFactory
import my.posq.data.network.RefreshTokenHandler
import my.posq.data.network.TokenManager
import my.posq.data.network.api.ApiService
import my.posq.data.repository.RepositoryImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<Json> {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
    single { Settings() }
    single { Session(get(), get()) }
    single { TokenManager() }
    singleOf(::RefreshTokenHandler)
    single { HttpClientFactory.create(get(), get(), get()) }
    single {
        ApiService(get(), get())
    }
    single<Repository> {
        RepositoryImpl(
            apiService = get(),
            session = get(),
            tokenManager = get(),
            databaseHelper = get(),
        )
    }
}
