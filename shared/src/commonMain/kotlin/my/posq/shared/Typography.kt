package my.posq.shared

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import posq.shared.generated.resources.Res
import posq.shared.generated.resources.pjs_regular
import posq.shared.generated.resources.pjs_medium
import posq.shared.generated.resources.pjs_bold

val PjsFont
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.pjs_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(resource = Res.font.pjs_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.pjs_bold, weight = FontWeight.Bold),
    )

val PosqTypography
    @Composable get() = Typography(
        bodyLarge = TextStyle(
            fontFamily = PjsFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = PjsFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = PjsFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 20.sp
        ),
    )