package my.lokalan.posq.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import my.posq.shared.Background
import my.posq.shared.Linen
import my.posq.shared.LinenDark
import my.posq.shared.MediumPurple
import my.posq.shared.Porcelain
import my.posq.shared.PorcelainDark
import my.posq.shared.RosePink
import my.posq.shared.SageDark
import my.posq.shared.Sandstone
import my.posq.shared.SandstoneDark
import my.posq.shared.PosqTypography
import my.posq.shared.TextOnColor
import my.posq.shared.TextSecondaryDark
import my.posq.shared.Sage

// 🌞 Light color scheme
private val LightColors = lightColorScheme(
    primary = Sage,
    onPrimary = White,

    secondary = Sandstone,
    onSecondary = White,

    tertiary = MediumPurple,
    onTertiary = White,

    background = Background,
    onBackground = Color(0xFF1A1A1A),

    surface = Porcelain,
    onSurface = Color(0xFF1A1A1A),

    surfaceVariant = Linen,
    onSurfaceVariant = Color.DarkGray,

    outline = Sage,
    error = RosePink
)

// 🌙 Dark color scheme
private val DarkColors = darkColorScheme(
    primary = SageDark,
    onPrimary = Color.Black,

    secondary = SandstoneDark,
    onSecondary = Color.Black,

    tertiary = MediumPurple,
    onTertiary = Color.Black,

    background = PorcelainDark,
    onBackground = TextOnColor,

    surface = LinenDark,
    onSurface = TextOnColor,

    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = TextSecondaryDark,

    outline = SageDark,
    error = RosePink
)

@Composable
expect fun dynamicColorScheme(darkTheme: Boolean): ColorScheme?

@Composable
fun animateColorSchemeAsState(targetColorScheme: ColorScheme): ColorScheme {
    val primary by animateColorAsState(targetColorScheme.primary)
    val onPrimary by animateColorAsState(targetColorScheme.onPrimary)
    val secondary by animateColorAsState(targetColorScheme.secondary)
    val onSecondary by animateColorAsState(targetColorScheme.onSecondary)
    val background by animateColorAsState(targetColorScheme.background)
    val onBackground by animateColorAsState(targetColorScheme.onBackground)
    val surface by animateColorAsState(targetColorScheme.surface)
    val onSurface by animateColorAsState(targetColorScheme.onSurface)
    val error by animateColorAsState(targetColorScheme.error)
    val onError by animateColorAsState(targetColorScheme.onError)

    return targetColorScheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        error = error,
        onError = onError,
    )
}


// 🌐 App Theme (works across KMP targets)
@Composable
fun PosqTheme(
    darkTheme: Boolean = false,
    useDynamicColor: Boolean = true,
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {

    // 1️⃣ Determine the target color scheme
    val targetColorScheme = when {
        colorScheme != null -> colorScheme
        useDynamicColor -> dynamicColorScheme(darkTheme)
            ?: if (darkTheme) DarkColors else LightColors

        else -> if (darkTheme) DarkColors else LightColors
    }

    // 2️⃣ Smoothly animate color transitions
    val animatedColorScheme = animateColorSchemeAsState(targetColorScheme)

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = PosqTypography,
        content = content
    )
}
