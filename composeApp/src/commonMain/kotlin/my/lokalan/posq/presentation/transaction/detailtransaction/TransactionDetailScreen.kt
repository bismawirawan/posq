package my.lokalan.posq.presentation.transaction.detailtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.posq.shared.Background
import my.posq.shared.BorderColor
import my.posq.shared.PosqTypography
import my.posq.shared.TextSecondaryDark
import my.posq.shared.formatIsoTimestampToCustom
import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import my.lokalan.posq.ui.component.BasicImage
import my.lokalan.posq.ui.component.ImageViewerManager
import my.lokalan.posq.ui.component.PosqScaffold
import my.lokalan.posq.ui.theme.PosqTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import my.lokalan.posq.ui.utils.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transaction: TransactionUiData,
    onBackClick: () -> Unit
) {
    PosqScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Detail Transaksi", style = PosqTypography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bukti Transfer",
                style = PosqTypography.bodyMedium.copy(color = TextSecondaryDark),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                    .background(Background),
                contentAlignment = Alignment.Center
            ) {
                if (transaction.buktiTransferUrl.isNotBlank()) {
                    BasicImage(
                        model = transaction.buktiTransferUrl,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                ImageViewerManager.show(transaction.buktiTransferUrl)
                            }
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak ada bukti transfer",
                            style = PosqTypography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Rincian Transaksi",
                style = PosqTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = transaction.transactionDate.formatIsoTimestampToCustom(),
                style = PosqTypography.bodyMedium.copy(color = TextSecondaryDark),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Nominal Tabungan",
                        style = PosqTypography.bodySmall.copy(color = TextSecondaryDark)
                    )
                    Text(
                        text = transaction.amount.toDouble().formatCurrency(),
                        style = PosqTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = transaction.userName.ifEmpty { transaction.reportedBy },
                        style = PosqTypography.bodyMedium
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = 1.dp,
                        color = BorderColor
                    )

                    Text(
                        text = "Metode Pembayaran",
                        style = PosqTypography.bodySmall.copy(color = TextSecondaryDark)
                    )
                    Text(
                        text = "${transaction.paymentName} - ${transaction.paymentType}",
                        style = PosqTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Dilaporkan oleh",
                    style = PosqTypography.bodySmall.copy(color = TextSecondaryDark)
                )
                Text(
                    text = transaction.reportedBy,
                    style = PosqTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun TransactionDetailScreenPreview() {
    PosqTheme {
        TransactionDetailScreen(
            transaction = TransactionUiData(
                transactionId = 1,
                amount = 500000,
                transactionDate = "2025-08-29T22:15:00.000Z",
                statusTransaksi = "Berhasil",
                reportedDate = "2025-08-29T22:15:00.000Z",
                reportedBy = "admint",
                confirmedBy = "Admin",
                buktiTransferUrl = "",
                paymentType = "Bank Transfer",
                paymentName = "BCA",
                userName = "admint",
                userId = 1
            ),
            onBackClick = {}
        )
    }
}
