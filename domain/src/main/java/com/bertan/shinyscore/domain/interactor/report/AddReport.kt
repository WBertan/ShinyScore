package com.bertan.shinyscore.domain.interactor.report

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.interactor.CompletableUseCase
import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.domain.repository.Repository
import io.reactivex.Completable

class AddReport(
    private val repository: Repository,
    executor: SchedulerExecutor
) : CompletableUseCase<AddReport.Param>(executor) {

    override fun buildUseCase(params: Param?): Completable =
        params.validate { repository.addReport(it.report) }

    data class Param(val report: Report)
}