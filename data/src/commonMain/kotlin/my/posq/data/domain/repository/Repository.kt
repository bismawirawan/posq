package my.posq.data.domain.repository

import my.posq.data.local.database.model.PaymentEntity
import my.posq.data.local.database.model.PeriodEntity
import my.posq.data.local.database.model.TransactionEntity
import my.posq.data.local.database.model.UserEntity
import my.posq.data.network.api.Result
import my.posq.data.network.model.response.TokenResponse
import my.posq.data.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun login(identifier: String, password: String): Flow<Result<TokenResponse>>
    fun getLoginProfile(): Flow<Result<UserResponse>>
    fun getTransactions(
        periodId: Int? = null,
        status: String? = null,
        paymentId: Int? = null
    ): Flow<Result<List<TransactionEntity>>>

    fun getListUsers(): Flow<Result<List<UserEntity>>>
    fun getUser(userId: Int): Flow<Result<UserEntity>>
    fun getLocalUsers(): Flow<Result<List<UserEntity>>>
    fun registerNewUser(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        userType: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>>

    fun getLocalUser(userId: Int): Flow<Result<UserEntity>>
    fun updateMe(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        role: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>>

    fun updateUser(
        userId: Int,
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        role: String,
        imageProfile: ByteArray?
    ): Flow<Result<UserResponse>>

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<Result<Unit>>
}
