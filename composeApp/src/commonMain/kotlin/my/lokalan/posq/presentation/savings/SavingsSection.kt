package my.lokalan.posq.presentation.savings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.lokalan.posq.presentation.savings.model.SavingsUiData
import my.lokalan.posq.ui.component.SavingsItem
import my.posq.shared.PosqTypography

@Composable
fun SavingsSection(
    modifier: Modifier = Modifier,
    showAllSavings: Boolean = false,
    savings: List<SavingsUiData>,
    onAddSavings: () -> Unit,
    onClickSeeMore: () -> Unit,
    onSavingsClick: (SavingsUiData) -> Unit = {}
) {

    val displaySavings = if (showAllSavings) savings else savings.take(3)

    LazyColumn(
    modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(displaySavings) { saving ->
            SavingsItem(
                modifier = Modifier.fillMaxWidth(),
                savingsDate = saving.savingsDate,
                savingsType = saving.savingsType,
                amount = saving.amount,
                note = saving.note,
                onClick = { onSavingsClick(saving) }
            )
        }
        if (!showAllSavings) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClickSeeMore()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Semua tabungan", style = PosqTypography.bodySmall)
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun EmptySavings(modifier: Modifier = Modifier, onAddTransaction: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = "Belum ada data tabungan",
            style = PosqTypography.titleLarge.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Semua tabungan akan muncul disini.",
            style = PosqTypography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onAddTransaction,
            modifier = Modifier
        ) {
            Text(
                text = "Tambah Tabungan",
            )
        }
    }
}
