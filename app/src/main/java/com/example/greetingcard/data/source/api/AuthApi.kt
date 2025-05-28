package com.example.greetingcard.data.source.api

import com.example.greetingcard.data.model.dto.user.LoginDto
import com.example.greetingcard.data.model.dto.user.UserDTO
import com.example.greetingcard.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(
        @Body loginDto: LoginDto
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body userDTO: UserDTO
    )


    @POST("api/user/test")
    suspend fun loginTest(
        @Body userDTO: UserDTO,
    ): Response<UserDTO>
}