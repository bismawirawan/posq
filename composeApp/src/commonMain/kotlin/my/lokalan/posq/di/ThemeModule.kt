package my.lokalan.posq.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import my.lokalan.posq.ui.theme.ThemeManager
import my.lokalan.posq.ui.theme.ThemePreference
import org.koin.dsl.module

val themeModule = module {
    single<ObservableSettings> { Settings() as ObservableSettings }   // not FlowSettings
    single { ThemePreference(get()) }
    single { ThemeManager(get()) }
}
