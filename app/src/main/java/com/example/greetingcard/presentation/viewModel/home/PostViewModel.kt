package com.example.greetingcard.presentation.viewModel.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.greetingcard.data.repository.post.PostRepository

class PostViewModel : ViewModel() {
    private val postDataSource: PostRepository = PostRepository()

    fun createPost(postText: String, selectedImages: List<Uri>) {
        Log.d("========== 게시글 생성 ==========", "클릭")
    }
}