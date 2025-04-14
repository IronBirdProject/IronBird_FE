package com.example.greetingcard.data.source.api

import com.example.greetingcard.data.model.response.Post
import retrofit2.Response
import retrofit2.http.GET

interface PostAPi {

    // 게시글 조회
    @GET("/api/post")
    suspend fun getPostList(
    ): Response<List<Post>>
}
