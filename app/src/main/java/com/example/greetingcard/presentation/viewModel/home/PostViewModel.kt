package com.example.greetingcard.presentation.viewModel.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.greetingcard.data.repository.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

// 여행지 아이템 데이터 클래스
data class DestinationItem(
    val name: String, val imageResId: Int
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    fun createPost(postText: String, selectedImages: List<Uri>) {
        Log.d("========== 게시글 생성 ==========", "클릭")
    }


}
