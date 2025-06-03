package com.example.greetingcard.presentation.viewModel.plan.plandetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.model.dto.plan.PlanUpdateDto
import com.example.greetingcard.data.model.dto.plan.ScheduleAddDto
import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.repository.plan.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlanDetailViewModel @Inject constructor(
    private val planRepository: PlanRepository
) : ViewModel() {

    // UI에서 관찰할 플랜 상세 상태
    private val _planDetailState = MutableStateFlow(PlanDetailState())
    val planDetailState: StateFlow<PlanDetailState> = _planDetailState

    /**
     * planId에 해당하는 상세 플랜 정보를 가져옵니다
     */
    fun fetchPlanDetails(planId: Int) {
        viewModelScope.launch {
            _planDetailState.value = PlanDetailState(isLoading = true) // 로딩 시작

            try {
                // 플랜 상세 정보를 가져오기
                val plan = planRepository.getPlan(planId).body()
                if (plan != null) {
                    _planDetailState.value = PlanDetailState(plan = plan) // 플랜 정보 업데이트
                    Log.d("플랜 상세 정보", plan.toString())
                } else {
                    _planDetailState.value = PlanDetailState(error = "플랜을 찾을 수 없습니다.") // 에러 처리
                }

            } catch (e: Exception) {
                _planDetailState.value = PlanDetailState(error = e.message)
            }
        }
    }

    /**
     * 새로운 일정을 추가하고, 플랜 상세를 갱신합니다
     */
    fun addSchedule(
        description: String, time: String, day: Int, cost: Int?, memo: String?, planId: Int
    ) {
        viewModelScope.launch {
            val newSchedule = ScheduleAddDto(
                day = day,
                description = description,
                time = time,
                cost = cost,
                memo = memo,
                planId = planId
            )

            try {
                planRepository.createSchedule(planId, newSchedule).let { response ->
                    if (response.isSuccessful) {
                        Log.d("일정 추가 성공", "새로운 일정이 추가되었습니다.")
                        // 일정 추가 후 플랜 상세를 다시 불러오기
                        fetchPlanDetails(planId)
                    } else {
                        Log.d("일정 추가 실패", "오류 코드: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.d("스케쥴 생성 에러", "${e.message}")
            }
        }
    }

    /**
     * 플랜 삭제 메서드
     */
    fun deletePlan(planId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = planRepository.deletePlan(planId)
                if (response.isSuccessful) {
                    Log.d("플랜 삭제 성공", "플랜 ID: $planId")
                    // 삭제 후 다시 목록을 불러오기
                    fetchPlanDetails(1) // 예시로 userId를 1로 설정
                    onSuccess() // 성공 콜백 호출
                } else {
                    Log.e("플랜 삭제 실패", "오류 코드: ${response.code()}")
                    onError("플랜 삭제 실패: 오류 코드 ${response.code()}") // 에러 콜백 호출
                }
            } catch (e: Exception) {
                Log.e("플랜 삭제 예외", e.toString())
                onError("플랜 삭제 실패: ${e.localizedMessage}") // 에러 콜백 호출
            }
        }
    }

    /**
     * 플랜 수정 메서드
     */
    fun updatePlan(
        planId: Int,
        title: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedPlanDto = PlanUpdateDto.fromPlan(
                    title = title,
                    startedDate = startDate,
                    endDate = endDate
                )
                val response = planRepository.updatePlan(planId, updatedPlanDto)
                if (response.isSuccessful) {
                    fetchPlanDetails(planId)
                    onSuccess()
                } else {
                    onFailure("오류 코드: ${response.code()}")
                }
            } catch (e: Exception) {
                onFailure(e.localizedMessage ?: "알 수 없는 오류")
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


