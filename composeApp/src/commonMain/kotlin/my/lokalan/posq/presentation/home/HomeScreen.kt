package my.lokalan.posq.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import my.posq.data.local.database.model.PeriodEntity
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.presentation.home.section.HomeInfoTransactionSection
import my.lokalan.posq.presentation.home.section.PeriodSection
import my.lokalan.posq.presentation.home.section.ProfileSection
import my.lokalan.posq.presentation.user.model.UserUIData
import my.lokalan.posq.presentation.utils.toUiData
import my.lokalan.posq.ui.component.ImageViewerManager
import my.lokalan.posq.ui.component.PosqScaffold
import my.lokalan.posq.ui.section.DialogUserType
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    rootNavHostController: NavHostController,
    onNavigateToTransaction: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userProfile by viewModel.session.userProfile.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    HomeContent(
        user = userProfile?.toUiData(),
        role = userProfile?.role.orEmpty(),
        onUserTypeChange = viewModel::setUserType,
        selectedPeriod = viewModel.selectedPeriod.value,
        uiState = uiState,
        errorMessage = errorMessage.orEmpty(),
        onPeriodChange = {
            viewModel.setSelectedPeriod(it)
            viewModel.getTransactions(it?.periodId)
        },
        onFetchProfile = viewModel::getProfile,
        onSeeMoreTransaction = onNavigateToTransaction,
        onAddTransaction = {
            rootNavHostController.navigate(Screen.AddTransactionRoute(false))
        },
        onFetchAllTransaction = {
            viewModel.getTransactions()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    user: UserUIData?,
    role: String,
    onUserTypeChange: (String) -> Unit,
    selectedPeriod: PeriodEntity?,
    uiState: HomeUiState,
    errorMessage: String,
    onPeriodChange: (PeriodEntity?) -> Unit,
    onSeeMoreTransaction: () -> Unit,
    onAddTransaction: () -> Unit,
    onFetchProfile: () -> Unit,
    onFetchAllTransaction: () -> Unit,
) {

    val refreshState = rememberPullToRefreshState()

    val userTypeSheetState = rememberModalBottomSheetState()
    val userTypeScope = rememberCoroutineScope()
    var userTypeShowBottomSheet by remember { mutableStateOf(false) }
    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var periodShowBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        scope.launch {
            if (errorMessage.isNotEmpty()) snackbarHostState.showSnackbar(errorMessage)
        }
    }

    PosqScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        if (userTypeShowBottomSheet) {
            DialogUserType(
                modifier = Modifier,
                sheetState = userTypeSheetState,
                scope = userTypeScope,
                onBottomSheetChange = { userTypeShowBottomSheet = it },
                onChooseUserType = onUserTypeChange
            )
        }

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = {
                onFetchProfile()
                if (selectedPeriod != null) {
                    onPeriodChange(selectedPeriod)
                } else {
                    onFetchAllTransaction()
                }
            },
            state = refreshState,
            modifier = Modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ProfileSection(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(top = paddingValues.calculateTopPadding()),
                        role = role,
                        user = user,
                        state = uiState.profile,
                        onRetry = onFetchProfile,
                        onClickImage = {
                            ImageViewerManager.show(it)
                        },
                    )
                }

                item {
                    PeriodSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        period = selectedPeriod,
                        onClickAll = {
                            onPeriodChange(null)
                            onFetchAllTransaction()
                        },
                        onShowPeriodSheet = { periodShowBottomSheet = true }
                    )
                }

                item {
                    HomeInfoTransactionSection(
                        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                        isHomeAdminDashboard = true,
                        state = uiState.transactions,
                        onAddTransaction = onAddTransaction,
                        onClickSeeMore = onSeeMoreTransaction
                    )
                }
            }
        }

    }
}


@Preview
@Composable
fun PreviewHomeContent() {
    HomeContent(
        user = UserUIData(
            id = 1,
            username = "iqbalf",
            fullname = "Iqbal Fauzi",
            email = "",
            phone = "",
            role = "Admin",
            imageProfileUrl = "",
            isActive = true
        ),
        uiState = HomeUiState(
            profile = SectionState.Success(
                UserUIData(
                    id = 1,
                    username = "iqbalf",
                    fullname = "Iqbal Fauzi",
                    email = "",
                    phone = "",
                    role = "Admin",
                    imageProfileUrl = "",
                    isActive = true
                )
            ),
            transactions = SectionState.Success(
                data = listOf()
            )
        ),
        errorMessage = "",
        onFetchProfile = {},
        onPeriodChange = {},
        role = "Admin",
        onUserTypeChange = { },
        onSeeMoreTransaction = { },
        onAddTransaction = { },
        onFetchAllTransaction = {},
        selectedPeriod = null,
    )
}
