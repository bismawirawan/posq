package my.lokalan.posq.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import my.posq.shared.BorderColor
import my.posq.shared.Sage
import my.posq.shared.PosqTypography
import my.posq.shared.Red
import my.posq.shared.TextSecondaryDark
import my.posq.shared.formatIsoTimestampToCustom
import my.posq.shared.formatToIDR
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SavingsItem(
    modifier: Modifier = Modifier,
    savingsDate: String,
    savingsType: String,
    note: String,
    amount: Int,
    onClick: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        val (dataRef, amountRef) = createRefs()

        Column(
            modifier = Modifier.constrainAs(dataRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = note,
                style = PosqTypography.bodySmall,
                modifier = Modifier
            )

            Text(
                text = savingsDate.formatIsoTimestampToCustom(),
                style = PosqTypography.bodySmall.copy(color = TextSecondaryDark),
                modifier = Modifier
            )
        }

        Text(
            text = if (savingsType == "savings") amount.formatToIDR() else "- ${amount.formatToIDR()}",
            style = if (savingsType == "savings") PosqTypography.titleLarge.copy(color = Sage) else PosqTypography.titleLarge.copy(color = Red),
            modifier = Modifier.constrainAs(amountRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}

@Preview
@Composable
fun SavingsItemPreview() {
    SavingsItem(
        savingsType = "savings",
        amount = 100000,
        note = "nabung",
        savingsDate = "2023-01-01T12:00:00Z"
    )
}

@Preview
@Composable
fun SavingsItemMinusPreview() {
    SavingsItem(
        savingsType = "expenses",
        amount = 100000,
        note = "jajan",
        savingsDate = "2023-01-01T12:00:00Z"
    )
}
