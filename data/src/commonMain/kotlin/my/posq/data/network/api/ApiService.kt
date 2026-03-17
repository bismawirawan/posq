package my.posq.data.network.api

import my.posq.data.network.model.request.ChangePasswordRequest
import my.posq.data.network.model.request.LoginRequest
import my.posq.data.network.model.response.DataResponse
import my.posq.data.network.model.response.PaymentResponse
import my.posq.data.network.model.response.PeriodeResponse
import my.posq.data.network.model.response.TokenResponse
import my.posq.data.network.model.response.TransactionResponse
import my.posq.data.network.model.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ApiService(private val httpClient: HttpClient) {

    private val authProvider = httpClient.authProvider<BearerAuthProvider>()

    suspend fun login(email: String, password: String): DataResponse<TokenResponse> {
        val loginRequest =
            LoginRequest(
                email = email,
                password = password
            )
        authProvider?.clearToken()
        return httpClient.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }

    suspend fun getLoginProfile(): DataResponse<UserResponse> {
        return httpClient.get("auth/profile").body()
    }

    suspend fun registerUser(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        role: String,
        imageProfile: ByteArray?
    ): DataResponse<UserResponse> {
        return httpClient.submitFormWithBinaryData(
            url = "auth/register",
            formData = formData {
                // Required Strings
                append("fullname", fullname)
                append("username", username)
                append("email", email)
                append("password", password)

                // Optional Strings (Only append if not null)
                phone?.let { append("phone_number", it) }
                append("user_type", role)

                // File Upload (image_profile)
                if (imageProfile != null) {
                    append(
                        key = "image_profile",
                        value = imageProfile,
                        headers = Headers.build {
                            append(
                                HttpHeaders.ContentType,
                                "image/jpg"
                            ) // Adjust mime-type if needed (e.g., image/png)
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"${username}_profile.jpg\""
                            )
                        }
                    )
                }
            }
        ).body()
    }

    suspend fun getListUsers(): DataResponse<List<UserResponse>> {
        return httpClient.get("users").body()
    }

    suspend fun getPeriods(): DataResponse<List<PeriodeResponse>> {
        return httpClient.get("periodes").body()
    }

    suspend fun getPayments(): DataResponse<List<PaymentResponse>> {
        return httpClient.get("payments").body()
    }

    suspend fun getTransactions(
        periodId: Int?,
        status: String?,
        paymentId: Int?
    ): DataResponse<List<TransactionResponse>> {
        return httpClient.get("transactions") {
            url {
                periodId?.let {
                    parameters.append("periode_id", periodId.toString())
                }
                status?.let {
                    parameters.append("status", status)
                }
                paymentId?.let {
                    parameters.append("payment_id", paymentId.toString())
                }
            }
        }.body()
    }

    suspend fun updateMe(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        role: String,
        imageProfile: ByteArray?
    ): DataResponse<UserResponse> {
        return httpClient.put(
            "users/me/update"
        ) {
            setBody(
                body = MultiPartFormDataContent(
                    parts = formData {
                        // Required Strings
                        append("fullname", fullname)
                        append("username", username)
                        append("email", email)
                        append("password", password)

                        // Optional Strings (Only append if not null)
                        phone?.let { append("phone_number", it) }
                        append("user_type", role)

                        // File Upload (image_profile)
                        if (imageProfile != null) {
                            append(
                                key = "image_profile",
                                value = imageProfile,
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        "image/jpg"
                                    ) // Adjust mime-type if needed (e.g., image/png)
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"${username}_profile.jpg\""
                                    )
                                }
                            )
                        }
                    }
                )
            )
        }.body()
    }

    suspend fun updateUser(
        userId: Int,
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        role: String,
        imageProfile: ByteArray?
    ): DataResponse<UserResponse> {
        return httpClient.put("users/$userId") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        // Required Strings
                        append("fullname", fullname)
                        append("username", username)
                        append("email", email)
                        append("password", password)

                        // Optional Strings (Only append if not null)
                        phone?.let { append("phone_number", it) }
                        append("user_type", role)

                        // File Upload (image_profile)
                        if (imageProfile != null) {
                            append(
                                key = "image_profile",
                                value = imageProfile,
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        "image/jpg"
                                    ) // Adjust mime-type if needed (e.g., image/png)
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"${username}_profile.jpg\""
                                    )
                                }
                            )
                        }
                    }
                )
            )
        }.body()
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): DataResponse<Unit> {
        val requestBody = ChangePasswordRequest(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmNewPassword = confirmNewPassword
        )
        return httpClient.post("users/me/change-password") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }
}
