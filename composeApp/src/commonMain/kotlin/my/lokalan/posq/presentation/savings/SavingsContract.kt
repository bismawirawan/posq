package my.lokalan.posq.presentation.savings

import my.lokalan.posq.presentation.home.SectionState
import my.lokalan.posq.presentation.savings.model.SavingsUiData
import my.posq.data.local.database.model.PeriodEntity

data class SavingsState(
    val savings: SectionState<List<SavingsUiData>> = SectionState.Loading,
    val periods: SectionState<List<PeriodEntity>> = SectionState.Loading,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface SavingsEvent {
    data class GetSavings(val periodId: Int? = null, val userId: Int? = null) : SavingsEvent
    data object ClearError : SavingsEvent
}

sealed interface SavingsEffect {
    data class ShowError(val message: String) : SavingsEffect
}
