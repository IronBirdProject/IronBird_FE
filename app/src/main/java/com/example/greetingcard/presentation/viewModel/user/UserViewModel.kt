package com.example.greetingcard.presentation.viewModel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.model.response.UserInfo
import com.example.greetingcard.data.source.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    val userInfo: StateFlow<UserInfo?> = sessionManager.userInfoFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    // UserViewModel.kt
    val isLoggedIn: StateFlow<Boolean> = sessionManager.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val accessToken: StateFlow<String?> = sessionManager.accessTokenFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun clearSession(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                sessionManager.clearSession()
                onSuccess()
            } catch (e: Exception) {
                onFailure("유저 정보 초기화 실패")
            }

        }

    }
}
