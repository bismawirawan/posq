package my.posq.data.local.database.model

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val userId: Int,
    val userName: String,
    val fullname: String,
    val email: String,
    val phone: String,
    val role: String,
    val imageProfileUrl: String,
)
