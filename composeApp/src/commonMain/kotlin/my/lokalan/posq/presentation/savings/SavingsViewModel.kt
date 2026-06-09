package my.lokalan.posq.presentation.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import my.lokalan.posq.presentation.home.SectionState
import my.lokalan.posq.presentation.utils.toUIData
import my.posq.data.domain.repository.Repository
import my.posq.data.network.api.Result
import kotlin.collections.filter

class SavingsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavingsState())
    val uiState: StateFlow<SavingsState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SavingsEffect>()
    val effect: SharedFlow<SavingsEffect> = _effect.asSharedFlow()

    init {
        onEvent(SavingsEvent.GetSavings())
        getPeriods()
    }

    fun onEvent(event: SavingsEvent) {
        when (event) {
            is SavingsEvent.GetSavings -> getSavings(event.userId)
            is SavingsEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun getPeriods() {
        repository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(periods = SectionState.Error(result.t.message)) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(periods = SectionState.Success(result.data)) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getSavings(userId: Int? = null) {
        _uiState.update { it.copy(savings = SectionState.Loading, isLoading = true) }
        repository.getSavings(userId = userId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(savings = SectionState.Error(result.t.message), isLoading = false) }
                    }
                    is Result.Success -> {
                        val allData = result.data.map { it.toUIData() }
                        val filteredData = if (userId != null) {
                            allData.filter { it.userId == userId }
                        } else {
                            allData
                        }
                        _uiState.update { it.copy(savings = SectionState.Success(filteredData), isLoading = false) }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
