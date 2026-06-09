package my.lokalan.posq.presentation.savings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import my.lokalan.posq.presentation.home.SectionState
import my.lokalan.posq.presentation.savings.model.SavingsUiData
import my.lokalan.posq.ui.component.PosqScaffold
import my.lokalan.posq.ui.component.TextButton
import my.lokalan.posq.ui.component.TextButtonOption
import my.lokalan.posq.ui.section.PeriodsSheet
import my.lokalan.posq.ui.theme.PosqTheme
import my.posq.data.local.database.model.PeriodEntity
import my.posq.shared.BgColorScreen
import my.posq.shared.PosqTypography
import my.posq.shared.extractMonthNumber
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SavingsScreen(
    navHostController: NavHostController,
    viewModel: SavingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val transactionsList = (uiState.savings as? SectionState.Success)?.data ?: emptyList()
    val periodsList = (uiState.periods as? SectionState.Success)?.data ?: emptyList()

    SavingsContent(
        isLoading = uiState.isLoading,
        onBackClick = { navHostController.popBackStack() },
        onRefresh = {
            viewModel.onEvent(SavingsEvent.GetSavings())
        },
        savings = transactionsList,
        periods = periodsList,
        onFetchAllSavings = { viewModel.onEvent(SavingsEvent.GetSavings()) },
        onSavingsClick = { savings ->
//            val savingsJson = Json.encodeToString(savings)
//            navHostController.navigate(Screen.TransactionDetailRoute(savingsJson))
        },
        onAddSavings = {
//            navHostController.navigate(Screen.AddTransactionRoute(isCollective = false))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsContent(
    isLoading: Boolean = false,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit = {},
    savings: List<SavingsUiData>,
    periods: List<PeriodEntity> = emptyList(),
    onFetchAllSavings: () -> Unit,
    onAddSavings: () -> Unit = {},
    onSavingsClick: (SavingsUiData) -> Unit = {}
) {

    var selectedPeriod by remember { mutableStateOf<PeriodEntity?>(null) }

    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var showPeriodBottom by remember { mutableStateOf(false) }

    val refreshState = rememberPullToRefreshState()

    if (showPeriodBottom) {
        PeriodsSheet(
            modifier = Modifier,
            sheetState = periodSheetState,
            scope = periodScope,
            periods = periods,
            onBottomSheetChange = { showPeriodBottom = it },
            onChoosePeriod = {
                selectedPeriod = it
            }
        )
    }

    PosqScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Riwayat Tabungan", style = PosqTypography.titleLarge)
                },
                modifier = Modifier,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BgColorScreen)
                        .padding(16.dp)
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
                            selectedPeriod = null
                        }
                        val monthNumber = if (selectedPeriod != null) {
                            extractMonthNumber(selectedPeriod!!.startDate)
                        } else ""
                        TextButtonOption(
                            text = if (selectedPeriod != null) monthNumber else "Pilih Bulan",
                            placeholder = "Pilih Bulan",
                            trailingIcon = Icons.Default.ArrowDropDown,
                            modifier = Modifier.weight(1f),
                        ) {
                            showPeriodBottom = true
                        }
                    }

                    AnimatedVisibility(
                        visible = savings.isEmpty(),
                        modifier = Modifier.constrainAs(emptyRef) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        EmptySavings(modifier = Modifier, onAddTransaction = onAddSavings)
                    }

                    AnimatedVisibility(
                        visible = savings.isNotEmpty(),
                        modifier = Modifier.constrainAs(listTransactionRef) {
                            top.linkTo(chooseUserRef.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        }
                    ) {
                        SavingsSection(
                            modifier = Modifier,
                            showAllSavings = true,
                            savings = savings,
                            onAddSavings = onAddSavings,
                            onClickSeeMore = {

                            },
                            onSavingsClick = onSavingsClick
                        )
                    }
                }
            }

            if (savings.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddSavings,
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
        SavingsContent(
            savings = emptyList(),
            periods = emptyList(),
            onBackClick = {},
            onFetchAllSavings = { },
            onAddSavings = { },
            onSavingsClick = { }
        )
    }
}
