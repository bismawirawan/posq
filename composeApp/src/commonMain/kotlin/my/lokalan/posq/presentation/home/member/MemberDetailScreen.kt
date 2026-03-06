@file:OptIn(ExperimentalMaterial3Api::class)

package my.lokalan.posq.presentation.home.member

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import my.posq.shared.Background
import my.posq.shared.PosqTypography
import my.lokalan.posq.presentation.home.SectionState
import my.lokalan.posq.presentation.home.member.MemberDetailViewModel
import my.lokalan.posq.presentation.home.section.HomeInfoTransactionSection
import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import my.lokalan.posq.presentation.user.model.UserUIData
import my.lokalan.posq.ui.component.BasicImage
import my.lokalan.posq.ui.component.ImageViewerManager
import my.lokalan.posq.ui.component.TalangragaScaffold
import my.lokalan.posq.ui.theme.PosqTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemberDetailScreen(
    navHostController: NavHostController,
    userId: Int,
    viewModel: MemberDetailViewModel = koinViewModel()
) {

    val transactionState by viewModel.transactionState.collectAsStateWithLifecycle()
    val userData by viewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.getUser(userId)
    }

    HomeMemberContent(
        user = userData,
        transactionState = transactionState,
        onBackClick = { navHostController.popBackStack() }
    )
}

@Composable
fun HomeMemberContent(
    user: UserUIData?,
    transactionState: SectionState<List<TransactionUiData>>,
    onBackClick: () -> Unit
) {
    TalangragaScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profil Anggota",
                        style = PosqTypography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    ImageViewerManager.show(user?.imageProfileUrl)
                                }
                                .size(100.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicImage(
                                model = user?.imageProfileUrl.orEmpty(),
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 4.dp, end = 4.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Color.LightGray, CircleShape)
                                .clickable { /* Pick Image */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Change Photo",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )
                        }
                    }

                    Text(
                        text = user?.fullname.orEmpty(),
                        style = PosqTypography.titleLarge,
                        modifier = Modifier
                    )

                    val infoLabel = "@${user?.username.orEmpty()} | ${user?.domicile.orEmpty()}"
                    Text(
                        text = infoLabel,
                        style = PosqTypography.bodyMedium,
                        modifier = Modifier
                    )
                }
            }

            item {
                HomeInfoTransactionSection(
                    modifier = Modifier.background(color = Background).padding(16.dp),
                    isHomeAdminDashboard = false,
                    state = transactionState,
                    onAddTransaction = {},
                    onClickSeeMore = { }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeMemberContent() {
    PosqTheme {
        HomeMemberContent(
            user = UserUIData(
                id = 1,
                username = "iqbalwork",
                fullname = "Iqbal Fauzi",
                email = "work.iqbalfauzi",
                phone = "087822882668",
                domicile = "Bandung",
                userType = "member",
                imageProfileUrl = "",
                isActive = true
            ),
            transactionState = SectionState.Success(
                listOf(
                    TransactionUiData(
                        transactionId = 123,
                        amount = 100000,
                        transactionDate = "2025-08-29T22:15:00.000Z",
                        statusTransaksi = "",
                        reportedDate = "2025-08-29T22:15:00.000Z",
                        reportedBy = "Iqbal Fauzi",
                        confirmedBy = "Iqbal Fauzi",
                        buktiTransferUrl = "",
                        paymentType = "Transfer Bank",
                        paymentName = "BCA"
                    )
                )
            ),
            onBackClick = { }
        )
    }
}
