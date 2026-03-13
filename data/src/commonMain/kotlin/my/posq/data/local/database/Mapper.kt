package my.posq.data.local.database

import my.posq.TransactionData
import my.posq.UserData
import my.posq.data.local.database.model.TransactionEntity
import my.posq.data.local.database.model.UserEntity

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
        confirmedBy = confirmedBy.orEmpty()
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
