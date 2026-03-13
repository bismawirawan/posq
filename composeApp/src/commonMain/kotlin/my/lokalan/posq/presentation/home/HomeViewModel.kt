package my.lokalan.posq.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import my.posq.data.domain.repository.Repository
import my.posq.data.local.database.model.PeriodEntity
import my.posq.data.local.session.Session
import my.posq.data.network.TokenManager
import my.posq.data.network.api.Result
import my.posq.shared.currentDate
import my.posq.shared.isDateInRange
import my.lokalan.posq.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import my.lokalan.posq.presentation.utils.toUIData

class HomeViewModel(
    val session: Session,
    private val tokenManager: TokenManager,
    private val repository: Repository,
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionUiData>>(emptyList())
    val transactions = _transactions.asStateFlow()

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType.asStateFlow()

    val selectedPeriod = mutableStateOf<PeriodEntity?>(null)

    private val _uiState = MutableStateFlow(
        HomeUiState(
            profile = SectionState.Loading,
            transactions = SectionState.Loading,
            isLoading = true
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getProfile()
    }

    fun setSelectedPeriod(period: PeriodEntity?) {
        selectedPeriod.value = period
    }

    fun setUserType(type: String) {
        _userType.update { type }
    }

    fun getProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        repository.getLoginProfile()
            .onEach { response ->
                when (response) {
                    is Result.Error -> {
                        _uiState.update { it.copy(profile = SectionState.Error(response.t.message)) }
                        _errorMessage.update { it }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(profile = SectionState.Success(response.data.toUiData())) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getLocalProfile() {
        _uiState.update { it.copy(profile = SectionState.Loading) }
        viewModelScope.launch {
            session.userProfile.value?.let { userResponse ->
                _uiState.update {
                    it.copy(
                        profile = SectionState.Success(userResponse.toUiData())
                    )
                }
                if (_userType.value.isNullOrBlank()) {
                    _userType.update { userResponse.role }
                }
            }
        }
    }

    fun getTransactions(periodId: Int? = null) {
        _uiState.update { it.copy(transactions = SectionState.Loading) }
        repository.getTransactions(periodId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        val data = result.data.map { it.toUIData() }
                        _transactions.update { data }
                        _uiState.update { it.copy(transactions = SectionState.Success(data)) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun clearSession() {
        viewModelScope.launch {
            session.clear()
            tokenManager.clearToken()
            _errorMessage.update { null }
        }
    }

}
