package com.example.greetingcard.data.model.dto.plan

data class ScheduleAddDto(
    val day: Int,
    val description: String,
    val time: String,
    val cost: Int?,
    val memo: String?,
)