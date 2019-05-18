package com.bertan.shinyscore.data.remote.service

import com.bertan.shinyscore.data.remote.model.CreditReport
import io.reactivex.Observable
import retrofit2.http.GET

interface ShinyScoreService {
    companion object {
        const val BASE_URL = "https://5lfoiyb0b3.execute-api.us-west-2.amazonaws.com/prod/mockcredit/"
    }

    @GET("values")
    fun getCreditReport(userId: String): Observable<CreditReport>
}