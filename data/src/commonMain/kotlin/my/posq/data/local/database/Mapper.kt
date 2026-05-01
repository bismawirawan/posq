package my.posq.data.local.database

import my.posq.PaymentData
import my.posq.PeriodData
import my.posq.TransactionData
import my.posq.UserData
import my.posq.data.local.database.model.PaymentEntity
import my.posq.data.local.database.model.PeriodEntity
import my.posq.data.local.database.model.TransactionEntity
import my.posq.data.local.database.model.UserEntity
import kotlin.text.toInt

fun PaymentData.toPaymentEntity(): PaymentEntity {
    return PaymentEntity(
        paymentId = paymentId.toInt(),
        paymentName = paymentName,
        paymentType = paymentType
    )
}

fun TransactionData.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = transactionId.toInt(),
        amount = amount.toInt(),
        reportedDate = reportedDate,
        transactionDate = transactionDate,
        statusTransaksi = statusTransaksi,
        buktiTransferUrl = buktiTransferUrl,
        paymentType = paymentType.orEmpty(),
        paymentName = paymentName.orEmpty(),
        reportedBy = reportedBy.orEmpty(),
        confirmedBy = confirmedBy.orEmpty(),
        userName = userName.orEmpty(),
        userId = (userId ?: 0L).toInt()
    )
}

fun PeriodData.toPeriodEntity(): PeriodEntity {
    return PeriodEntity(
        periodId = periodId.toInt(),
        periodeName = periodeName,
        startDate = startDate,
        endDate = endDate
    )
}

fun UserData.toUserEntity(): UserEntity {
    return UserEntity(
        userId = userId.toInt(),
        userName = username,
        fullname = fullname,
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        role = role,
        imageProfileUrl = imageProfileUrl.orEmpty()
    )
}
