package com.example.greetingcard.data.model.response

// 로그인 응답 모델
data class LoginResponse(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val refreshToken: String,
    val refreshTokenExpiresIn: Long,
)

