package com.example.greetingcard.presentation.viewModel.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.model.dto.user.LoginDto
import com.example.greetingcard.data.model.dto.user.RegisterDto
import com.example.greetingcard.data.model.response.LoginResponse
import com.example.greetingcard.data.repository.user.AuthRepository
import com.example.greetingcard.data.source.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {


    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val loginResult: MutableState<LoginResponse?> = mutableStateOf(null)

    fun login(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Log.d("LoginViewModel", "로그인 시도: $username, $password")
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try {
                // Retrofit 클라이언트를 통해 API 호출
                val response = authRepository.login(
                    LoginDto.from(
                        userName = username,
                        password = password
                    )
                )
                if (response.isSuccessful) {
                    loginResult.value = response.body()
                    loginResult.value?.let { sessionManager.saveLoginSession(it) }
                    Log.d("LoginViewModel", "loginResult: ${loginResult.value}")
                    onSuccess()
                } else {
                    val error = "로그인 실패: ${response.errorBody()}"
                    Log.e("LoginViewModel", "Login failed: $error")
                    errorMessage.value = error
                    onFailure(error)
                }
            } catch (e: Exception) {
                val error = "에러 발생: ${e.message}"
                errorMessage.value = error
                Log.e("LoginViewModel", error, e)
                onFailure(error)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        defaultProfilePic: String = "",
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try {
                val dto = RegisterDto.from(
                    email = email,
                    password = password,
                    name = name,
                    defaultProfilePic = defaultProfilePic
                )
                val response = authRepository.register(dto)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val error = "회원가입 실패: ${response.errorBody()?.string()}"
                    errorMessage.value = error
                    Log.e("AuthViewModel", error)
                    onFailure(error)
                }
            } catch (e: Exception) {
                val error = "회원가입 중 오류 발생: ${e.message}"
                errorMessage.value = error
                Log.e("AuthViewModel", error, e)
                onFailure(error)
            } finally {
                isLoading.value = false
            }
        }
    }


//    fun join(registerDto: RegisterDto) {
//        viewModelScope.launch {
//            isLoading.value = true
//            try {
//                // 회원가입 API 호출
//                val response = authRepository.join(registerDto)
//
//                if (response.isSuccessful) {
//                    // 회원가입 성공
//                    loginResult.value = response.body()
//                    Log.d("AuthViewModel", "회원가입 성공: ${response.body()}")
//                } else {
//                    // 회원가입 실패
//                    errorMessage.value = "회원가입 실패: ${response.errorBody()?.string()}"
//                    Log.e("AuthViewModel", "회원가입 실패: ${response.errorBody()?.string()}")
//                }
//            } catch (e: Exception) {
//                errorMessage.value = "Error: ${e.message}"
//            } finally {
//                isLoading.value = false
//            }
//        }
//    }
//
//    fun loginTest(registerDto: RegisterDto) {
//        Log.d("userName", registerDto.toString())
//        isLoading.value = true
//        viewModelScope.launch {
//            try {
//                // loginTest 호출 (UserDTO를 전달)
//                val loginResultTest = LoginClient.apiService.loginTest(registerDto)
//
//                Log.d("LoginTest", "Response: $loginResultTest")
//
//                // 응답이 성공적일 경우
//                if (loginResultTest.isSuccessful) {
//                    // 성공한 경우 로그인 결과 처리
//                    loginResult.value = "Login Success: ${loginResultTest.body()?.toString()}"
//                    Log.d("LoginTest", "Login successful: ${loginResultTest.body()}")
//                } else {
//                    // 실패한 경우 에러 처리
//                    errorMessage.value = "Login Failed"
//                    Log.e("LoginTest", "Login failed with code: ${loginResultTest.code()}")
//                }
//            } catch (e: Exception) {
//                // 예외 발생 시 처리
//                errorMessage.value = "Error: ${e.message}"
//                Log.e("LoginTest", "Error occurred", e) // 예외 로그 출력
//            } finally {
//                // 로딩 상태 종료
//                isLoading.value = false
//                Log.d("LoginTest", "Loading finished") // 로딩 완료 로그 출력
//            }
//        }
//    }


}
