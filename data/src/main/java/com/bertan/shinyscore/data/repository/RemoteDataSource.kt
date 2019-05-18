package com.bertan.shinyscore.data.repository

import com.bertan.shinyscore.data.model.ReportEntity
import io.reactivex.Observable

interface RemoteDataSource {
    fun getReports(userId: String): Observable<List<ReportEntity>>

    sealed class Error : Throwable() {
        data class GetReportsFailed(val userId: String, val reason: String) : Error()
    }
}