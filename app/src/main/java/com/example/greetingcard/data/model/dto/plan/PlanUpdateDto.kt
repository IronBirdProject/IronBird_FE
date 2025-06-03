package com.example.greetingcard.data.model.dto.plan

data class PlanUpdateDto(
    val title: String? = null,
    val startedDate: String? = null,
    val endDate: String? = null,
//    val destination: String,
) {
    companion object {
        fun fromPlan(
            title: String? = null,
            startedDate: String? = null,
            endDate: String? = null,
        ): PlanUpdateDto {
            return PlanUpdateDto(
                title = title,
                startedDate = startedDate,
                endDate = endDate
            )
        }
    }
}