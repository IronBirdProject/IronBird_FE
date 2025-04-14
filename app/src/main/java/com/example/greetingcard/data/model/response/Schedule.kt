package com.example.greetingcard.data.model.response

data class Schedule(
    val id: Int,
    val day: Int,
    val time: String,           // 일정 시간
    val description: String,    // 일정 이름
    val cost: Int? = null,             // 비용
    val memo: String?,          // 간단한 메모
)
