package com.bertan.shinyscore.domain.interactor.report

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.interactor.ObservableUseCase
import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.domain.repository.Repository
import io.reactivex.Observable

class GetReports(
    private val repository: Repository,
    executor: SchedulerExecutor
) : ObservableUseCase<List<Report>, GetReports.Param>(executor) {

    override fun buildUseCase(params: Param?): Observable<List<Report>> =
        params.validate { repository.getReports(it.userId) }

    data class Param(val userId: String)
}