package com.example.greetingcard.data.repository.post

import com.example.greetingcard.data.model.response.Post
import com.example.greetingcard.data.source.api.PostApi
import jakarta.inject.Inject
import retrofit2.Response

class PostRepository @Inject constructor(
    private val postApi: PostApi
) {
    // postService 구현체
//    private val postApi: PostAPi = NetworkModule.create(PostAPi::class.java)

    // 게시글 조회
    suspend fun getPostList(): Response<List<Post>> {
        return postApi.getPostList()
    }
}
