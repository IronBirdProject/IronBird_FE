package com.example.greetingcard.data.model.dto.user

data class RegisterDto(
    var email: String = "",
    var password: String,
    var name: String = "",
    var defaultProfilePic: String = "",
) {
    companion object {
        fun from(
            email: String = "",
            password: String,
            name: String = "",
            defaultProfilePic: String,
        ): RegisterDto {
            return RegisterDto(
                email = email,
                password = password,
                name = name,
                defaultProfilePic = defaultProfilePic,
            )
        }
    }
}
