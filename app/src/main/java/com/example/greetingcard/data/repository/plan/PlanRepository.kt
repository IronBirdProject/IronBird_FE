package com.example.greetingcard.data.repository.plan

import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.source.api.PlanApi
import retrofit2.Response

class PlanRepository(private val planApi: PlanApi) {

    // planId로 상세 정보 가져오기
    suspend fun getPlanDetails(planId: Int): Response<Plan> {
        return planApi.getPlanDetails(planId) // API 호출
    }
}
