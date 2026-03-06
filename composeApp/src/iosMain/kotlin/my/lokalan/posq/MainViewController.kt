package my.lokalan.posq

import androidx.compose.ui.window.ComposeUIViewController
import my.lokalan.posq.application.App
import my.lokalan.posq.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) {
    App()
}