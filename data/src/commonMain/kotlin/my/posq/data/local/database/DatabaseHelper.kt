package my.posq.data.local.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import my.posq.PosQDatabase
import my.posq.data.local.database.model.TransactionEntity
import my.posq.data.local.database.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseHelper(factory: DriverFactory) {

    private val database = PosQDatabase(factory.createDriver())
    private val transactionsQueries = database.transactionsQueries
    private val usersQueries = database.userQueries

    fun insertTransactions(list: List<TransactionEntity>) {
        list.forEach { (transactionId, amount, reportedDate, transactionDate, statusTransaksi, buktiTransferUrl, paymentType, paymentName, reportedBy, confirmedBy) ->
            transactionsQueries.insertTransactionData(
                transactionId = transactionId.toLong(),
                amount = amount.toLong(),
                reportedDate = reportedDate,
                transactionDate = transactionDate,
                statusTransaksi = statusTransaksi,
                buktiTransferUrl = buktiTransferUrl,
                paymentType = paymentType,
                paymentName = paymentName,
                reportedBy = reportedBy,
                confirmedBy = confirmedBy
            )
        }
    }

    fun clearTransactions() = transactionsQueries.deleteAllTransactionData()

    fun deleteTransactionById(id: Long) {
        transactionsQueries.deleteTransactionDataById(id)
    }

    fun deleteTransactionByIds(ids: List<Int>) {
        ids.forEach { deleteTransactionById(it.toLong()) }
    }

    fun getAllTransactions() = transactionsQueries.selectAllTransactionData().executeAsList()
        .map { it.toTransactionEntity() }

    fun getAllTransactionsAsFlow(): Flow<List<TransactionEntity>> =
        transactionsQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map { it.toTransactionEntity() }
            }

    fun insertUsers(list: List<UserEntity>) {
        list.forEach { (userId, userName, fullname, email, phone, role, imageProfileUrl) ->
            usersQueries.insertUserData(
                userId = userId.toLong(),
                username = userName,
                fullname = fullname,
                email = email,
                phone = phone,
                role = role,
                imageProfileUrl = imageProfileUrl
            )
        }
    }

    fun clearUsers() = usersQueries.deleteAllUserData()

    fun deleteUserById(userId: Long) = usersQueries.deleteUserDataById(userId = userId)

    fun deleteUserByIds(userIds: List<Int>) {
        userIds.forEach { deleteUserById(it.toLong()) }
    }

    fun getAllUsers() = usersQueries.selectAllUserData().executeAsList().map { it.toUserEntity() }

    fun getAllUsersAsFlow(): Flow<List<UserEntity>> =
        usersQueries.selectAllUserData()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map { it.toUserEntity() }
            }

    fun getUserById(userId: Long): Flow<List<UserEntity>> {
        return usersQueries.selectUser(userId).asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map { it.toUserEntity() }
            }
    }

}
