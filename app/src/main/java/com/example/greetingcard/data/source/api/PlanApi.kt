package com.example.greetingcard.data.source.api

import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.PlanPreview
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlanApi {

    // 전체 플랜 조회
    @GET("api/plan")
    suspend fun getAllPlans(): Response<List<Plan>>

    // 특정 유저의 플랜 조회
    @GET("api/plan/user/{userId}")
    suspend fun getUserPlanList(@Path("userId") userId: Int): Response<List<PlanPreview>>

    // 플랜 상세 조회는 현재 컨트롤러에 없음 → 대신 PlanList를 받아 filter하거나, 서버에 단일 조회 API 추가 필요

    // 플랜 생성
    @POST("api/plan/create")
    suspend fun createPlan(@Body plan: Plan): Response<Plan>

    // 플랜 수정
    @PUT("api/plan/update/{planId}")
    suspend fun updatePlan(@Path("planId") planId: Long, @Body plan: Plan): Response<Plan>

    // 플랜 삭제
    @DELETE("api/plan/{planId}")
    suspend fun deletePlan(@Path("planId") planId: Long): Response<String>
}
