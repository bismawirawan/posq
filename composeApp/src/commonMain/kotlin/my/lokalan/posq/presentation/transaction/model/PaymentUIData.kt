package my.lokalan.posq.presentation.transaction.model
data class PaymentUIData(
    val id: Int,
    val paymentName: String,
    val paymentType: String
)

data class PaymentGroupUIData(
    val paymentType: String,
    val paymentList: List<PaymentUIData>
)
