package com.example.greetingcard.presentation.viewModel.plandetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.model.dto.plan.ScheduleAddDto
import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanDetailViewModel : ViewModel() {
    private val _planDetailState = MutableStateFlow(PlanDetailState())
    val planDetailState: StateFlow<PlanDetailState> = _planDetailState

    // 더미 데이터
    private val dummyPlan = Plan(
        id = 1,
        title = "나홀로 도쿄",
        startDate = "2024.01.04",
        endDate = "2024.01.10",
        schedule = listOf(
            Schedule(
                id = 1,
                day = 1,
                time = "08:40",
                description = "미마루 스위트 교토 시조",
                cost = 320000,
                memo = "8시에는 도착해야댐",
            ),
            Schedule(
                id = 2,
                day = 2,
                time = "09:11",
                description = "교토 -> 오사카 이동",
                cost = 21000,
                memo = null,
            ),
            Schedule(
                id = 3,
                day = 1,
                time = "10:00",
                description = "타코야끼 먹기",
                cost = 3000,
                memo = null,
            ),
            Schedule(
                id = 4,
                day = 3,
                time = "11:00",
                description = "관광 및 쇼핑",
                cost = null,
                memo = null,
            ),
            Schedule(
                id = 5,
                day = 4,
                time = "11:00",
                description = "관광 및 쇼핑",
                cost = null,
                memo = null,
            ),
            Schedule(
                id = 6,
                day = 5,
                time = "11:00",
                description = "관광 및 쇼핑",
                cost = null,
                memo = null,
            ),
            Schedule(
                id = 7,
                day = 6,
                time = "11:00",
                description = "관광 및 쇼핑",
                cost = null,
                memo = null,
            ),
        ),
        planId = 100,
        backgroundImg = "https://media.istockphoto.com/id/904453184/ko/%EC%82%AC%EC%A7%84/%ED%9B%84%EC%A7%80%EC%82%B0%EA%B3%BC-%EB%8F%84%EC%BF%84-%EC%8A%A4%EC%B9%B4%EC%9D%B4-%EB%9D%BC%EC%9D%B8.jpg?s=612x612&w=0&k=20&c=9NB3US7CKB9CYAXLqRvH5A-rRWSJ-BL3b7w3vAo99Kg=",
    )

    // planId를 받아서 API 호출
    fun fetchPlanDetails(planId: Int) {
        viewModelScope.launch {
            _planDetailState.value = PlanDetailState(isLoading = true) // 로딩 시작

            try {
//                val plan = planRepository.getPlanDetails(planId)
                val plan = dummyPlan
                _planDetailState.value = PlanDetailState(plan = plan)
            } catch (e: Exception) {
                _planDetailState.value = PlanDetailState(error = e.message)
            }
        }
    }

    fun addSchedule(
        description: String,
        time: String,
        day: Int,
        cost: Int?,
        memo: String?,
        planId: Int
    ) {
//        val currentState = _planDetailState.value
//        val currentPlan = currentState.plan ?: return

        viewModelScope.launch {
            val newSchedule = ScheduleAddDto(
                day = day,
                description = description,
                time = time,
                cost = cost,
                memo = memo,
            )

            try {
//                planRepository.addSchedule(newSchedule)
//                fetchPlanDetails(planId)
            } catch (e: Exception) {
                Log.d("스케쥴 생성 에러", "${e.message}")
            }
        }
    }


}


// 플랜 상태
data class PlanDetailState(
    val isLoading: Boolean = false,
    val plan: Plan? = null,
    val error: String? = null,
)

