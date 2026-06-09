package my.posq.data.local.database.model

data class SavingsEntity(
    val savingsId: Int,
    val amount: Int,
    val note: String,
    val savingsDate: String,
    val savingsType: String,
    val userId: Int
)