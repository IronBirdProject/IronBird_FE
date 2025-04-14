package com.example.greetingcard.data.model.response

data class Plan(
    val id: Int,
    val planId: Int,
    val title: String,
    val schedule: List<Schedule>,
    val startDate: String,
    val endDate: String,
    val backgroundImg: String?
)