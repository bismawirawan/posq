package my.lokalan.posq.presentation.home.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import my.posq.data.domain.repository.Repository
import my.posq.data.network.api.Result
import my.lokalan.posq.presentation.home.SectionState
import my.lokalan.posq.presentation.user.model.UserUIData
import my.lokalan.posq.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import my.lokalan.posq.presentation.transaction.model.TransactionUiData

class MemberDetailViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _transactionState = MutableStateFlow<SectionState<List<TransactionUiData>>>(
        SectionState.Loading
    )
    val transactionState = _transactionState.asStateFlow()

    private val _user = MutableStateFlow<UserUIData?>(null)
    val user = _user.asStateFlow()

    fun getUser(userId: Int) {
        repository.getUser(userId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        val data = result.data.toUiData()
                        _user.update { data }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
