package com.example.greetingcard.data.model.dto.plan

data class PlanCreateDto(
    val title: String,
    val destination: String,
    val startedDate: String,
    val endDate: String,
    val userId: Int,
)