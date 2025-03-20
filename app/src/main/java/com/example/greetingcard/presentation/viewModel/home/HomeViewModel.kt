package com.example.greetingcard.presentation.viewModel.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greetingcard.data.model.response.Comment
import com.example.greetingcard.data.model.response.Post
import com.example.greetingcard.data.repository.post.PostRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    // 임시로 HomeViewModel 내부에 postDataSource 생성 추후 수정
    private val postDataSource: PostRepository = PostRepository()

    // 전체 게시글 리스트
    private val _allPostList: MutableLiveData<List<Post>> by lazy { MutableLiveData<List<Post>>() }
    val allPostList: LiveData<List<Post>> = _allPostList

//    private val filteredPostList: List<Post> = _allPostList.value ?: emptyList()

    // 메인 탭 리스트
    val tabs = listOf("Planning", "Posting")

    // 선택된 탭
    var selectedTabIndex by mutableIntStateOf(0)
    var isDropDownExpanded by mutableStateOf(false)

    // 구독한 여행지 목록
    val myTravelList = listOf("제주", "부산", "강릉", "경주", "서울", "대구", "인천")

    // 선택된 여행지
    var selectedDestination by mutableStateOf<String?>("")

    // 댓글 더미 데이터
    val commentList = List(30) { i ->
        Comment(
            i,
            if (i % 2 == 0) {
                "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/202208/11/e4727123-666e-4603-a2fa-b2478b3130bd.jpg"
            } else {
                null
            },
            "userNickName${i + 1}",
            if (i % 2 == 0) {
                "댓글 ${i + 1}번 내용"
            } else {
                "댓글 ${i + 1}번 텍스트 길이 테스트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ"
            },
            "2022.08.11"
        )
    }

    init {
        getPostList()
        Log.d("viewModel init", "뷰모델 init 실행 ==========================")
    }

    // 탭 변경 메서드
    fun selectTab(index: Int) {
        selectedTabIndex = index
    }

    // 드롭다운 메뉴 토글
    fun toggleDropDownMenu() {
        isDropDownExpanded = !isDropDownExpanded
    }

    // 검색 쿼리
    var searchQuery by mutableStateOf("")

    // 쿼리 변경 메서드(임시 검색)
    fun changeQuery(query: String) {
        searchQuery = query

//        filteredPostList = allPostList.filter {
//            it.caption.contains(searchQuery) ||
//                    it.destination.contains(searchQuery)
//        }
    }

    // 여행지 선택 메서드
    fun selectDestination(destination: String) {
        selectedDestination = destination
    }

    // 게시글 리스트 조회
    private fun getPostList() {
        viewModelScope.launch {
            try {
                val response = postDataSource.getPostList()
                println("response: $response")
                if (response.isSuccessful) {
                    Log.d("게시글 조회", response.body().toString())
                    _allPostList.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("게시글 조회 실패", e.message.toString())
            }
        }
    }


}
