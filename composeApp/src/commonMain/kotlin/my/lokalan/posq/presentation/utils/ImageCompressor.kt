@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package my.lokalan.posq.presentation.utils

expect class ImageCompressor() {
    suspend fun compress(byteArray: ByteArray, maxSizeBytes: Long): ByteArray
}