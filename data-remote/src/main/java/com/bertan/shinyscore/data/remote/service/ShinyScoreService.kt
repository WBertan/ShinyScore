package com.bertan.shinyscore.data.remote.service

import com.bertan.shinyscore.data.remote.model.CreditReport
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ShinyScoreService {
    companion object {
        const val BASE_URL = "https://5lfoiyb0b3.execute-api.us-west-2.amazonaws.com/prod/mockcredit/"
    }

    @GET("values")
    fun getCreditReport(@Query("userId") userId: String): Observable<CreditReport>
}