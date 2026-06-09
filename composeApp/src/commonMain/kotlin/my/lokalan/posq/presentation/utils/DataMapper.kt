package my.lokalan.posq.presentation.utils

import my.lokalan.posq.presentation.savings.model.SavingsUiData
import my.lokalan.posq.presentation.transaction.model.PaymentUIData
import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import my.posq.data.local.database.model.UserEntity
import my.posq.data.network.model.response.UserResponse
import my.lokalan.posq.presentation.user.model.UserUIData
import my.posq.data.local.database.model.PaymentEntity
import my.posq.data.local.database.model.SavingsEntity
import my.posq.data.local.database.model.TransactionEntity

fun UserResponse.toUiData(): UserUIData {
    return UserUIData(
        id = id,
        username = username.orEmpty(),
        fullname = fullname.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        role = role.orEmpty(),
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
        role = role,
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
        paymentName = this.paymentName,
        userName = this.userName,
        userId = this.userId
    )
}

fun PaymentEntity.toUIData(): PaymentUIData {
    return PaymentUIData(
        id = paymentId,
        paymentName = paymentName,
        paymentType = paymentType
    )
}

fun SavingsEntity.toUIData(): SavingsUiData {
    return SavingsUiData(
        savingsId = savingsId,
        amount = amount,
        savingsDate = this.savingsDate,
        note = this.note,
        savingsType = this.savingsType,
        userId = this.userId
    )
}