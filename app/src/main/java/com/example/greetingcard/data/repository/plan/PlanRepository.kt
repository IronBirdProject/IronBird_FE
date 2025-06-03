package com.example.greetingcard.data.repository.plan

import com.example.greetingcard.data.model.dto.plan.PlanCreateDto
import com.example.greetingcard.data.model.dto.plan.PlanUpdateDto
import com.example.greetingcard.data.model.dto.plan.ScheduleAddDto
import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.PlanPreview
import com.example.greetingcard.data.model.response.Schedule
import com.example.greetingcard.data.source.api.PlanApi
import jakarta.inject.Inject
import retrofit2.Response


class PlanRepository @Inject constructor(
    private val planApi: PlanApi
) {
    // planApi 구현체
//    private val planApi: PlanApi = RetrofitInstance.create(PlanApi::class.java)

//    suspend fun getAllPlans(): Response<List<Plan>> {
//        return planApi.getAllPlans()
//    }

    // 특정 유저의 플랜 목록 조회
    suspend fun getPlansByUserId(userId: Int): Response<List<PlanPreview>> {
        return planApi.getUserPlanList(userId)
    }

    // 특정 플랜 조회
    suspend fun getPlan(planId: Int): Response<Plan> {
        return planApi.getPlan(planId)
    }

    // 플랜 생성
    suspend fun createPlan(plan: PlanCreateDto): Response<Plan> {
        return planApi.createPlan(plan)
    }

    // 플랜 수정
    suspend fun updatePlan(planId: Int, planUpdateDto: PlanUpdateDto): Response<Plan> {
        return planApi.updatePlan(planId, planUpdateDto)
    }

    // 플랜 삭제
    suspend fun deletePlan(planId: Int): Response<Unit> {
        return planApi.deletePlan(planId)
    }

    // 스케쥴 생성
    suspend fun createSchedule(planId: Int, schedule: ScheduleAddDto): Response<Schedule> {
        return planApi.createSchedule(planId, schedule)
    }
}
