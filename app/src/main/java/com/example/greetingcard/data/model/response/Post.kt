package com.example.greetingcard.data.model.response

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    val title: String,
    val detail: String,
    val planId: Int?,
    val userName: String,
    val createTime: String,
    val modifyTime: String,
    val userProfileImgUrl: String?,

    @SerializedName("uploadImage")
    val uploadImage: List<String>?,

    val likeCount: Int,
) {
    val safeUploadImage: List<String>
        get() = if (uploadImage.isNullOrEmpty()) {
            listOf(
                "https://img.freepik.com/free-photo/beautiful-landscape-around-lake-kawaguchiko_74190-3059.jpg?semt=ais_hybrid",
                "https://img.freepik.com/free-photo/fuji-mountain-kawaguchiko-lake-morning-autumn-seasons-fuji-mountain-yamanachi-japan_335224-102.jpg?semt=ais_hybrid"
            )
        } else {
            uploadImage
        }
}




