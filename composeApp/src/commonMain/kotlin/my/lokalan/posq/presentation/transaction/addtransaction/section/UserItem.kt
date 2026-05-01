package my.lokalan.posq.presentation.transaction.addtransaction.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import my.posq.shared.Background
import my.posq.shared.Sage
import my.posq.shared.PosqTypography
import my.lokalan.posq.presentation.user.model.UserUIData
import my.lokalan.posq.ui.component.BasicImage

@Composable
fun UserItem(
    user: UserUIData, onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Background),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar Placeholder
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (user.imageProfileUrl.isNotBlank()) {
                    BasicImage(
                        model = user.imageProfileUrl,
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Sage
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullname,
                    style = PosqTypography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = user.phone,
                    style = PosqTypography.bodySmall.copy(color = Color.Gray)
                )
            }

            val backgroundColor = if (user.role == "admin")
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)

            val textColor = if (user.role == "admin")
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.secondary

            Box(
                modifier = Modifier
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(width = 1.dp, color = textColor, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = user.role.replaceFirstChar { it.uppercase() },
                    style = PosqTypography.bodySmall.copy(color = textColor)
                )
            }
        }
    }
}
