package my.lokalan.posq.ui.utils

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

sealed class ImageSourceUtils {
    data class Remote(val url: String) : ImageSourceUtils()
    data class Local(val resId: DrawableResource) : ImageSourceUtils()
    data class Icon(val icon: ImageVector) : ImageSourceUtils()
}