package com.example.greetingcard.data.model.response


data class PlanPreview(
    val id: Int,
    val userId: Int,
    val userName: String,
    val title: String,
    val destination: String,
    val startedDate: String,
    val endDate: String,
//    val imageUrl: String? = null
)
