package my.posq.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String? = null,
    @SerialName("fullname")
    val fullname: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("phone_number")
    val phone: String? = null,
    @SerialName("role")
    val role: String? = null,
    @SerialName("image_profile")
    val imageProfile: String? = null,
    @SerialName("is_active")
    val isActive: Boolean? = null
)
