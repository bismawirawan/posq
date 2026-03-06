package my.lokalan.posq.presentation.home

import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import my.posq.data.local.database.model.PeriodEntity
import my.lokalan.posq.presentation.user.model.UserUIData

data class HomeUiState(
    val profile: SectionState<UserUIData>,
    val transactions: SectionState<List<TransactionUiData>>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class SectionState<out T> {
    object Loading : SectionState<Nothing>()
    data class Success<T>(val data: T) : SectionState<T>()
    data class Error(val message: String?) : SectionState<Nothing>()
}
