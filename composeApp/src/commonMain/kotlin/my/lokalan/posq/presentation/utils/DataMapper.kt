package my.lokalan.posq.presentation.utils

import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import my.posq.data.local.database.model.UserEntity
import my.posq.data.network.model.response.UserResponse
import my.lokalan.posq.presentation.user.model.UserUIData
import my.posq.data.local.database.model.TransactionEntity

fun UserResponse.toUiData(): UserUIData {
    return UserUIData(
        id = id,
        username = username.orEmpty(),
        fullname = fullname.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        domicile = domisili.orEmpty(),
        userType = userType.orEmpty(),
        imageProfileUrl = imageProfile.orEmpty(),
        isActive = isActive ?: false,
    )
}

fun UserEntity.toUiData(): UserUIData {
    return UserUIData(
        id = userId,
        username = userName,
        fullname = fullname,
        email = email,
        phone = phone,
        domicile = domisili,
        userType = userType,
        imageProfileUrl = imageProfileUrl,
        isActive = true,
    )
}

fun TransactionEntity.toUIData(): TransactionUiData {
    return TransactionUiData(
        transactionId = transactionId,
        amount = amount,
        transactionDate = this.transactionDate,
        statusTransaksi = this.statusTransaksi,
        reportedDate = this.reportedDate,
        buktiTransferUrl = this.buktiTransferUrl,
        reportedBy = this.reportedBy,
        confirmedBy = this.confirmedBy,
        paymentType = this.paymentType,
        paymentName = this.paymentName
    )
}
