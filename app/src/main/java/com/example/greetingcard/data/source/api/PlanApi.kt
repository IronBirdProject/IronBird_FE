package com.example.greetingcard.data.source.api

import com.example.greetingcard.data.model.dto.plan.PlanCreateDto
import com.example.greetingcard.data.model.dto.plan.ScheduleAddDto
import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.PlanPreview
import com.example.greetingcard.data.model.response.Schedule
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

    // 특정 플랜 조회
    @GET("api/plan/{planId}")
    suspend fun getPlan(@Path("planId") planId: Int): Response<Plan>

    // 플랜 생성
    @POST("api/plan/create")
    suspend fun createPlan(@Body plan: PlanCreateDto): Response<Plan>

    // 플랜 수정
    @PUT("api/plan/update/{planId}")
    suspend fun updatePlan(@Path("planId") planId: Int, @Body plan: Plan): Response<Plan>

    // 플랜 삭제
    @DELETE("api/plan/{planId}")
    suspend fun deletePlan(@Path("planId") planId: Int): Response<String>

    // 스케쥴 생성
    @POST("api/plan/{planId}/schedules")
    suspend fun createSchedule(
        @Path("planId") planId: Int,
        @Body schedule: ScheduleAddDto
    ): Response<Schedule>
}
