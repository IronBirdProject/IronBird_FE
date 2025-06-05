package com.example.greetingcard.data.repository.user

import com.example.greetingcard.data.model.dto.user.LoginDto
import com.example.greetingcard.data.model.dto.user.RegisterDto
import com.example.greetingcard.data.source.api.AuthApi
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {

    // 자체 로그인
    suspend fun login(loginDto: LoginDto) = authApi.login(loginDto)

    // 자체 회원가입
    suspend fun register(registerDto: RegisterDto) = authApi.register(registerDto)


}