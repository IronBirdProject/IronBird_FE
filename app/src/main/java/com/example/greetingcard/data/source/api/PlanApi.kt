package com.example.greetingcard.data.source.api

import com.example.greetingcard.data.model.response.Plan
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PlanApi {

    // 플랜 상세 조회
    @GET("plans/{id}")
    suspend fun getPlanDetails(@Path("id") planId: Int): Response<Plan>
}