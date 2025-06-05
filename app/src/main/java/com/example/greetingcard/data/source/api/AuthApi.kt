package com.example.greetingcard.data.source.api

import com.example.greetingcard.data.model.dto.user.LoginDto
import com.example.greetingcard.data.model.dto.user.RegisterDto
import com.example.greetingcard.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(
        @Body loginDto: LoginDto
    ): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body registerDto: RegisterDto
    ): Response<Unit>


    @POST("api/user/test")
    suspend fun loginTest(
        @Body registerDto: RegisterDto,
    ): Response<RegisterDto>
}