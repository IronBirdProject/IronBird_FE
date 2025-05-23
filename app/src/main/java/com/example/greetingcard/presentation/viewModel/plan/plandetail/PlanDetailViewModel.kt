package com.example.greetingcard.presentation.viewModel.plan.plandetail

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
    // UI에서 관찰할 플랜 상세 상태
    private val _planDetailState = MutableStateFlow(PlanDetailState())
    val planDetailState: StateFlow<PlanDetailState> = _planDetailState

    // TODO: 실제 API 연동 전까지 임시로 사용하는 더미 플랜
    private val dummyPlan = Plan(
        id = 1,
        title = "나홀로 도쿄",
        startDate = "2024.01.04",
        endDate = "2024.01.10",
        schedule = listOf(
            Schedule(1, 1, "08:40", "미마루 스위트 교토 시조", 320000, "8시에는 도착해야댐"),
            Schedule(2, 2, "09:11", "교토 -> 오사카 이동", 21000, null),
            Schedule(3, 1, "10:00", "타코야끼 먹기", 3000, null),
            Schedule(4, 3, "11:00", "관광 및 쇼핑", null, null),
            Schedule(5, 4, "11:00", "관광 및 쇼핑", null, null),
            Schedule(6, 5, "11:00", "관광 및 쇼핑", null, null),
            Schedule(7, 6, "11:00", "관광 및 쇼핑", null, null),
        ),
        planId = 100,
        backgroundImg = "https://media.istockphoto.com/...jpg"
    )

    /**
     * planId에 해당하는 상세 플랜 정보를 가져옵니다
     */
    fun fetchPlanDetails(planId: Int) {
        viewModelScope.launch {
            _planDetailState.value = PlanDetailState(isLoading = true) // 로딩 시작

            try {
                // TODO: API 연동 시 아래 라인 교체
                val plan = dummyPlan
                _planDetailState.value = PlanDetailState(plan = plan)
            } catch (e: Exception) {
                _planDetailState.value = PlanDetailState(error = e.message)
            }
        }
    }

    /**
     * 새로운 일정을 추가하고, 플랜 상세를 갱신합니다
     */
    fun addSchedule(
        description: String,
        time: String,
        day: Int,
        cost: Int?,
        memo: String?,
        planId: Int
    ) {
        viewModelScope.launch {
            val newSchedule = ScheduleAddDto(
                day = day,
                description = description,
                time = time,
                cost = cost,
                memo = memo,
            )

            try {
                // TODO: scheduleService.addSchedule(newSchedule)
                // fetchPlanDetails(planId)
            } catch (e: Exception) {
                Log.d("스케쥴 생성 에러", "${e.message}")
            }
        }
    }


}


// UI 상태 데이터 클래스
data class PlanDetailState(
    val isLoading: Boolean = false,
    val plan: Plan? = null,
    val error: String? = null,
)


