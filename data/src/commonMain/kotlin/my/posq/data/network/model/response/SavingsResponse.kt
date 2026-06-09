package my.posq.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavingsResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("amount")
    val amount: Double,
    @SerialName("savings_date")
    val savingsDate: String,
    @SerialName("note")
    val note: String,
    @SerialName("savings_type")
    val savingsType: String,
    @SerialName("user_id")
    val userId: Int,
)