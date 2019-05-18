package com.bertan.shinyscore.data.remote.repository

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.remote.mapper.ReportMapper.asReportEntity
import com.bertan.shinyscore.data.remote.service.ShinyScoreService
import com.bertan.shinyscore.data.repository.RemoteDataSource
import io.reactivex.Observable

class RemoteDataSourceImpl(private val service: ShinyScoreService) : RemoteDataSource {
    override fun getReports(userId: String): Observable<List<ReportEntity>> =
        service.getCreditReport(userId)
            .map { listOf(it.creditReportInfo.asReportEntity) }
            .onErrorResumeNext { error: Throwable ->
                Observable.error(RemoteDataSource.Error.GetReportsFailed(userId, error.localizedMessage))
            }
}