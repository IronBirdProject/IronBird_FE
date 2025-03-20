package com.example.greetingcard.presentation.viewModel.login.kakao

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class KakaoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    companion object {
        const val TAG = "KakaoAuthViewModel"
    }

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    var loginSuccess = false

    // 해당 id가존재하지 않을경우 회원가입창으로 이동
    // 해당 id가 존재할경우 환영합니다 ""님하고 create_plan 창으로 이동

    fun handleKakaoLogin(
        navController: NavController
    ): Boolean {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        loginWithKakao()
                        loginSuccess = true
                    } else {
                        _loginResult.postValue(Result.failure(error))
                    }
                } else {
                    getUserInfo()
                    loginSuccess = true
                }
            }
        } else {
            loginWithKakao()
            loginSuccess = true
        }
        return loginSuccess
    }

    fun loginWithKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오 로그인 실패", error)
            } else if (token != null) {
                // 로그인 성공 시 액세스 토큰을 확인
                Log.i(TAG, "카카오 로그인 성공 - 액세스 토큰: ${token.accessToken}")
            }
        }

        // 카카오톡 로그인 시도
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            // 카카오 계정 로그인 시도
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }


    fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }

}


