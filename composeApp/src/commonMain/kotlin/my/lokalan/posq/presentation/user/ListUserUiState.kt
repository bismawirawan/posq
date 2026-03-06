package my.lokalan.posq.presentation.user

import my.lokalan.posq.presentation.user.model.UserUIData

sealed class ListUserUiState {
    object Loading : ListUserUiState()
    object EmptyData : ListUserUiState()
    data class Success(val users: List<UserUIData>) : ListUserUiState()
}
