package com.example.greetingcard.data.repository.plan

import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.PlanPreview
import com.example.greetingcard.data.source.api.PlanApi
import com.example.greetingcard.data.source.api.RetrofitInstance
import retrofit2.Response

class PlanRepository {
    // planApi 구현체
    private val planApi: PlanApi = RetrofitInstance.create(PlanApi::class.java)

    suspend fun getAllPlans(): Response<List<Plan>> {
        return planApi.getAllPlans()
    }

    suspend fun getPlansByUserId(userId: Int): Response<List<PlanPreview>> {
        return planApi.getUserPlanList(userId)
    }

    suspend fun createPlan(plan: Plan): Response<Plan> {
        return planApi.createPlan(plan)
    }

    suspend fun updatePlan(planId: Long, plan: Plan): Response<Plan> {
        return planApi.updatePlan(planId, plan)
    }

    suspend fun deletePlan(planId: Long): Response<String> {
        return planApi.deletePlan(planId)
    }
}
