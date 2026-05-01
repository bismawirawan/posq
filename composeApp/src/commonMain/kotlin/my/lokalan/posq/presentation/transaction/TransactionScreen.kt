package my.lokalan.posq.presentation.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import my.posq.data.local.database.model.PeriodEntity
import my.posq.shared.Background
import my.posq.shared.BorderColor
import my.posq.shared.INDONESIA_TRIMMED
import my.posq.shared.PosqTypography
import my.posq.shared.TextSecondaryDark
import my.posq.shared.formatDateRange
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.presentation.home.SectionState
import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import my.lokalan.posq.presentation.user.model.UserUIData
import my.lokalan.posq.ui.component.PosqScaffold
import my.lokalan.posq.ui.component.TextButton
import my.lokalan.posq.ui.component.TextButtonOption
import my.lokalan.posq.ui.section.ListUserSheet
import my.lokalan.posq.ui.section.PeriodsSheet
import my.lokalan.posq.ui.theme.PosqTheme
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionScreen(
    rootNavController: NavHostController,
    navHostController: NavHostController,
    viewModel: TransactionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val transactionsList = (uiState.transactions as? SectionState.Success)?.data ?: emptyList()
    val periodsList = (uiState.periods as? SectionState.Success)?.data ?: emptyList()

    TransactionContent(
        isLoading = uiState.isLoading,
        onRefresh = {
            viewModel.onEvent(TransactionEvent.GetPeriods)
            viewModel.onEvent(TransactionEvent.GetUsers)
            viewModel.onEvent(TransactionEvent.GetTransactions(uiState.selectedPeriod?.periodId, uiState.selectedUser?.id))
        },
        selectedPeriod = uiState.selectedPeriod,
        onPeriodChange = { viewModel.onEvent(TransactionEvent.SelectPeriod(it)) },
        periods = periodsList,
        transactions = transactionsList,
        onFetchAllTransaction = { viewModel.onEvent(TransactionEvent.GetTransactions()) },
        selectedUser = uiState.selectedUser,
        users = uiState.users,
        onSelectUser = { viewModel.onEvent(TransactionEvent.SelectUser(it)) },
        onTransactionClick = { transaction ->
            val transactionJson = Json.encodeToString(transaction)
            rootNavController.navigate(Screen.TransactionDetailRoute(transactionJson))
        },
        onAddTransaction = {
            navHostController.navigate(Screen.AddTransactionRoute(isCollective = false))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionContent(
    isLoading: Boolean = false,
    onRefresh: () -> Unit = {},
    selectedUser: UserUIData?,
    users: List<UserUIData>,
    onSelectUser: (UserUIData?) -> Unit,
    selectedPeriod: PeriodEntity?,
    onPeriodChange: (PeriodEntity?) -> Unit,
    periods: List<PeriodEntity>,
    transactions: List<TransactionUiData>,
    onFetchAllTransaction: () -> Unit,
    onAddTransaction: () -> Unit,
    onTransactionClick: (TransactionUiData) -> Unit = {}
) {

    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var showPeriodBottom by remember { mutableStateOf(false) }

    val userSheetState = rememberModalBottomSheetState()
    val userScope = rememberCoroutineScope()
    var showUserSheet by remember { mutableStateOf(false) }

    val refreshState = rememberPullToRefreshState()

    if (showPeriodBottom) {
        PeriodsSheet(
            modifier = Modifier,
            sheetState = periodSheetState,
            scope = periodScope,
            periods = periods,
            onBottomSheetChange = { showPeriodBottom = it },
            onChoosePeriod = {
                onPeriodChange(it)
            }
        )
    }

    if (showUserSheet) {
        ListUserSheet(
            modifier = Modifier,
            sheetState = userSheetState,
            scope = userScope,
            data = users,
            onBottomSheetChange = { showUserSheet = it },
            onSelectUser = {
                onSelectUser(it)
            }
        )
    }

    PosqScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Riwayat Transaksi", style = PosqTypography.titleLarge)
                },
                modifier = Modifier,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = onRefresh,
                state = refreshState,
                modifier = Modifier.padding(paddingValues).fillMaxSize()
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(16.dp).fillMaxSize()
                ) {
                    val (filterRef, chooseUserRef, listTransactionRef, emptyRef) = createRefs()

                    Row(
                        modifier = Modifier.constrainAs(filterRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            text = "Semua",
                            isSelected = selectedPeriod == null,
                            modifier = Modifier
                        ) {
                            onPeriodChange(null)
                        }
                        val bulan = if (selectedPeriod != null) {
                            formatDateRange(
                                startDateString = selectedPeriod.startDate,
                                endDateString = selectedPeriod.endDate,
                                monthFormat = INDONESIA_TRIMMED
                            )
                        } else ""
                        TextButtonOption(
                            text = if (selectedPeriod != null) "${selectedPeriod.periodeName}: $bulan" else "Pilih Bulan",
                            placeholder = "Pilih Bulan",
                            trailingIcon = Icons.Default.ArrowDropDown,
                            modifier = Modifier.weight(1f),
                        ) {
                            showPeriodBottom = true
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = TextSecondaryDark,
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.dp, BorderColor, CircleShape)
                                .clickable {
                                    showUserSheet = true
                                }
                                .background(color = Background)
                                .padding(8.dp)
                        )
                    }

                    TextButtonOption(
                        text = if (selectedUser != null) selectedUser.fullname else "Semua Pengguna",
                        placeholder = "Pilih Pengguna",
                        modifier = Modifier.constrainAs(chooseUserRef) {
                            top.linkTo(filterRef.bottom, 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    ) {
                        showUserSheet = true
                    }

                    AnimatedVisibility(
                        visible = transactions.isEmpty(),
                        modifier = Modifier.constrainAs(emptyRef) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        EmptyTransaction(modifier = Modifier, onAddTransaction = onAddTransaction)
                    }

                    AnimatedVisibility(
                        visible = transactions.isNotEmpty(),
                        modifier = Modifier.constrainAs(listTransactionRef) {
                            top.linkTo(chooseUserRef.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        }
                    ) {
                        TransactionSection(
                            modifier = Modifier,
                            showAllTransaction = true,
                            transactions = transactions,
                            onAddTransaction = onAddTransaction,
                            onClickSeeMore = {

                            },
                            onTransactionClick = onTransactionClick
                        )
                    }
                }
            }

            if (transactions.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddTransaction,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TransactionContentPreview() {
    PosqTheme(useDynamicColor = false) {
        TransactionContent(
            transactions = emptyList(),
            onFetchAllTransaction = { },
            selectedPeriod = null,
            onPeriodChange = { },
            periods = emptyList(),
            selectedUser = null,
            users = emptyList(),
            onSelectUser = { },
            onAddTransaction = { },
            onTransactionClick = { }
        )
    }
}
