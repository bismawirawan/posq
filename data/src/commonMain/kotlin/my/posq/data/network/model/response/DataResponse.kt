package my.posq.data.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataResponse<T>(
    @SerialName("data") val data: T?,
    @SerialName("status") val status: Boolean,
    @SerialName("message") val message: String? = null,
)
