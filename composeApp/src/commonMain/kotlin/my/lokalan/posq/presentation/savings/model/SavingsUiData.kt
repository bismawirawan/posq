package my.lokalan.posq.presentation.savings.model

import kotlinx.serialization.Serializable

@Serializable
data class SavingsUiData(
    val savingsId: Int,
    val amount: Int,
    val note: String,
    val savingsDate: String,
    val savingsType: String,
    val userId: Int
)