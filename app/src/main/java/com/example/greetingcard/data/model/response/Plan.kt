package com.example.greetingcard.data.model.response

data class Plan(
    val id: Int,
    val userId: Int,
    val userName: String,
    val destination: String,
    val title: String,
    val schedules: List<Schedule>,
    val startedDate: String,
    val endDate: String,
//    val backgroundImg: String?
)