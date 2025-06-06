package com.example.greetingcard.presentation.viewModel.plan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.model.response.PlanPreview
import com.example.greetingcard.data.repository.plan.PlanRepository
import com.example.greetingcard.data.source.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 플랜 목록(요약 정보) 화면 전용 ViewModel
 * - 서버로부터 특정 유저의 PlanPreview 리스트를 불러옵니다.
 */
@HiltViewModel
class PlanPreviewViewModel @Inject constructor(
    private val repository: PlanRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _userId = MutableStateFlow<Int?>(null)

    init {
        viewModelScope.launch {
            sessionManager.userInfoFlow.collect { user ->
                Log.d("PlanPreviewViewModel", "세션에서 유저 정보 수집: ${user.id}, ${user.name}")
                if (user.id != 0) {
                    _userId.value = user.id
                    loadPlanPreviews() // ✅ 여기서 바로 호출!
                }
            }
        }
    }


    // 플랜 요약 리스트 상태 저장
    private val _planPreviews = MutableStateFlow<List<PlanPreview>>(emptyList())
    val planPreviews: StateFlow<List<PlanPreview>> = _planPreviews

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 실제 포맷에 맞게 수정

    val nearestPlan: StateFlow<PlanPreview?> = _planPreviews
        .map { plans ->
            plans
                .filter {
                    try {
                        val date = LocalDate.parse(it.startedDate, dateFormatter)
                        date.isAfter(LocalDate.now())
                    } catch (e: Exception) {
                        false // 날짜 파싱 실패 시 제외
                    }
                }
                .sortedBy {
                    LocalDate.parse(it.startedDate, dateFormatter)
                }
                .firstOrNull()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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
    fun loadPlanPreviews() {
        val uid = _userId.value
        if (uid == null || uid == 0) {
            _error.value = "유효한 유저 ID 없음"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("플랜 목록 불러오기", "유저 ID: $uid, 로딩 시작")
                val res = repository.getPlansByUserId(uid)
                if (res.isSuccessful) {
                    _planPreviews.value = res.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "오류 코드: ${res.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
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
                    loadPlanPreviews()
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
