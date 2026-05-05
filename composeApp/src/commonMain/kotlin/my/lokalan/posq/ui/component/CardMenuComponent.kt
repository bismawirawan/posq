package my.lokalan.posq.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import my.lokalan.posq.ui.utils.ImageSourceUtils
import my.posq.shared.PosqTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CardMenu(
    modifier: Modifier = Modifier,
    title: String,
    source: ImageSourceUtils,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            ),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (source) {
                is ImageSourceUtils.Remote -> {
                    AsyncImage(
                        model = source.url,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                is ImageSourceUtils.Local -> {
                    Image(
                        painter = painterResource(source.resId),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                is ImageSourceUtils.Icon -> {
                    Icon(
                        imageVector = source.icon,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Black
                    )
                }
            }

            Text(
                text = title,
                style = PosqTypography.titleMedium.copy(fontSize = 14.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 2.dp)
            )

        }

    }
}

@Preview
@Composable
fun CardMenuView() {
    CardMenu(
        title = "Data",
        source = ImageSourceUtils.Icon(Icons.Default.AccountCircle)
    )
}

