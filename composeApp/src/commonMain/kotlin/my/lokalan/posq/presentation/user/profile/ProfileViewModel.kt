package my.lokalan.posq.presentation.user.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.posq.data.local.session.Session
import my.posq.data.network.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel(
    val session: Session,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val imageUrl = mutableStateOf(session.userProfile.value?.imageProfile)

    fun onImageChange(uri: String) {
        imageUrl.value = uri
    }

    fun clearSession() {
        viewModelScope.launch {
            session.clear()
            tokenManager.clearToken()
        }
    }

}
