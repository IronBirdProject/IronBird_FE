package com.example.greetingcard.data.model.dto.user

data class LoginDto(
    val userName: String,
    val password: String
) {
    companion object {
        fun from(userName: String, password: String): LoginDto {
            return LoginDto(
                userName = userName,
                password = password
            )
        }
    }
}
