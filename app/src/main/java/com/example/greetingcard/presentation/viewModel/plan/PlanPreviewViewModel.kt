package com.example.greetingcard.presentation.viewModel.plan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.model.response.PlanPreview
import com.example.greetingcard.data.repository.plan.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 플랜 목록(요약 정보) 화면 전용 ViewModel
 * - 서버로부터 특정 유저의 PlanPreview 리스트를 불러옵니다.
 */
@HiltViewModel
class PlanPreviewViewModel @Inject constructor(
    private val repository: PlanRepository
) : ViewModel() {

    // 플랜 목록을 불러오기 위한 Repository
//    private val repository: PlanRepository = PlanRepository()

    // 플랜 요약 리스트 상태 저장
    private val _planPreviews = MutableStateFlow<List<PlanPreview>>(emptyList())
    val planPreviews: StateFlow<List<PlanPreview>> = _planPreviews

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 에러 메시지 상태
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * 특정 유저의 플랜 목록을 서버에서 불러오는 함수
     * @param userId 유저 식별자
     */
    fun loadPlanPreviews(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d(
                    "플랜 목록 불러오기",
                    "유저 ID: $userId, 로딩 시작"
                )
                val res = repository.getPlansByUserId(userId)
                if (res.isSuccessful) {
                    _planPreviews.value = res.body() ?: emptyList()
                    _error.value = null
                    Log.d("플랜 목록 불러오기 성공", planPreviews.value.toString())
                } else {
                    _error.value = "오류 코드: ${res.code()}"
                    Log.e("플랜 목록 불러오기 실패", "오류 코드: ${res.code()}")
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
                Log.e("플랜 목록 불러오기 예외", e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePlan(
        planId: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.deletePlan(planId)
                if (response.isSuccessful) {
                    loadPlanPreviews(userId = 1)
                    onSuccess()
                } else {
                    onFailure("삭제 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                onFailure(e.localizedMessage ?: "알 수 없는 오류")
            }
        }
    }
}
