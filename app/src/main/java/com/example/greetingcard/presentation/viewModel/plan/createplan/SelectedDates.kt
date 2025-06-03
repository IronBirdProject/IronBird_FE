package com.example.greetingcard.presentation.viewModel.plan.createplan

import java.time.LocalDate

data class SelectedDates(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)


fun SelectedDates.updateWith(date: LocalDate): SelectedDates {
    return when {
        startDate == null -> copy(startDate = date)
        endDate == null && date.isAfter(startDate) -> copy(endDate = date)
        endDate == null && date == startDate -> copy(startDate = date, endDate = date)
        else -> copy(startDate = date, endDate = null)
    }
}
