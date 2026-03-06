package my.lokalan.posq.presentation.utils

import androidx.compose.runtime.Composable

expect class SharedFileReader {
    suspend fun readBytes(uri: String): ByteArray?
}

// In commonMain
@Composable
expect fun rememberSharedFileReader(): SharedFileReader
