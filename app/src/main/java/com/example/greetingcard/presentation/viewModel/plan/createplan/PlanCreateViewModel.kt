package com.example.greetingcard.presentation.viewModel.plan.createplan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.R
import com.example.greetingcard.data.model.dto.plan.PlanCreateDto
import com.example.greetingcard.data.repository.plan.PlanRepository
import com.example.greetingcard.presentation.viewModel.home.DestinationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class PlanCreateViewModel @Inject constructor(
    private val planRepository: PlanRepository
) : ViewModel() {
    // Repository

    init {
        Log.d("PlanCreateViewModel", "ViewModel 초기화됨")
    }

    /**
     * 날짜 선택 파트
     * */
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

    /**
     * 여행지 선택 파트
     * */

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ✅ 추천 여행지 (이제 이미지 포함)
    private val _recommendedDestinations = listOf(
        DestinationItem("제주도", R.drawable.jeju),
        DestinationItem("부산", R.drawable.busan),
        DestinationItem("강릉", R.drawable.gangneung),
        DestinationItem("경주", R.drawable.gyeongju),
        DestinationItem("여수", R.drawable.yeosu),
        DestinationItem("일본", R.drawable.japan),
        DestinationItem("몽골", R.drawable.mongolia),
        DestinationItem("상하이", R.drawable.shanghai),
        DestinationItem("필리핀", R.drawable.philippines)
    )
    val recommendedDestinations: List<DestinationItem> = _recommendedDestinations

    private val _allDestinations = listOf(
        DestinationItem("제주도", R.drawable.jeju),
        DestinationItem("부산", R.drawable.busan),
        DestinationItem("강릉", R.drawable.gangneung),
        DestinationItem("경주", R.drawable.gyeongju),
        DestinationItem("여수", R.drawable.yeosu),
    )
    private val _searchResults = MutableStateFlow<List<DestinationItem>>(emptyList())
    val searchResults: StateFlow<List<DestinationItem>> = _searchResults.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _searchResults.value = if (query.isBlank()) {
            emptyList()
        } else {
            _allDestinations.filter { it.name.contains(query) }
        }
    }

    private val _selectedDestination = MutableStateFlow("")
    val selectedDestination = _selectedDestination.asStateFlow()

    fun selectDestination(destination: String) {
        if (_selectedDestination.value == destination && _selectedDestination.value.isNotBlank()) {
            Log.d("여행지 클릭", "선택된 여행지 클릭함")
            _selectedDestination.value = ""
        } else {
            _selectedDestination.value = destination
        }
    }

    // 입력된 쿼리 클리어
    fun onClearQuery() {
        _searchQuery.value = ""
    }


    private val _recentQueries =
        MutableStateFlow<List<String>>(mutableListOf("부산", "여수", "일본", "코타키나발루"))
    val recentQueries: StateFlow<List<String>> = _recentQueries.asStateFlow()

    fun addRecentQuery(query: String) {
        if (query.isNotBlank() && !_recentQueries.value.contains(query)) {
            _recentQueries.update { listOf(query) + it } // 최근 검색어 추가
        }
    }

    fun deleteRecentQuery(query: String) {
        _recentQueries.update { it.filterNot { it == query } }
    }

    fun clearRecentQueries() {
        _recentQueries.value = emptyList()
    }

    // 여행 제목
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    // 제목 설정
    fun setTitle(input: String) {
        Log.d("제목 설정", input)
        _title.value = input
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun createPlan() {
        Log.d("플랜 생성", "클릭")
        viewModelScope.launch {
            _isLoading.value = true
            val newPlan: PlanCreateDto = PlanCreateDto(
                title = _title.value,
                startedDate = _selectedDates.value.startDate.toString(),
                endDate = _selectedDates.value.endDate.toString(),
                destination = _selectedDestination.value,
                userId = 1 // TODO: 실제 사용자 ID로 변경 필요
            )
            try {
                planRepository.createPlan(newPlan)
                Log.d("플랜 생성 성공", newPlan.toString())
            } catch (e: Exception) {
                Log.e("플랜 생성 실패", e.message.toString())
//                _isSuccess.value = false
//                _isLoading.value = false
//                return@launch
            }
            delay(2000) // 실제 API 연동 또는 로컬 저장
            _isLoading.value = false
            _isSuccess.value = true
        }
    }
}

// 플랜 생성
//    fun createPlan(
//
//    )
