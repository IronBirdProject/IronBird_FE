package com.example.greetingcard.presentation.viewModel.plan.createplan

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class CalendarViewModel : ViewModel() {
    // 시작날짜와 종료날짜 상태
    private val _selectedDates = MutableStateFlow(SelectedDates())
    val selectedDates: StateFlow<SelectedDates> = _selectedDates

    fun selectDate(date: LocalDate) {
        Log.d("날짜선택", "클릭")
        _selectedDates.update { current ->
            when {
                // 시작 날짜가 비어 있으면 startDate 설정
                current.startDate == null -> current.copy(startDate = date)

                // 종료 날짜가 비어 있고, 선택한 날짜가 startDate 이후일 때 endDate 설정
                current.endDate == null && date.isAfter(current.startDate) -> current.copy(endDate = date)

                current.endDate != null && current.startDate != null && date.isBefore(current.endDate) -> current.copy(
                    startDate = date,
                    endDate = null,
                )

                current.startDate == date -> current.copy(startDate = null)
                // 모든 상태 초기화하고 새로운 startDate 설정
                else -> SelectedDates(startDate = date)
            }
        }
    }
}