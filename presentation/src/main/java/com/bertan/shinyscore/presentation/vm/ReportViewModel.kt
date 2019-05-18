package com.bertan.shinyscore.presentation.vm

import com.bertan.shinyscore.domain.interactor.report.GetReports
import com.bertan.shinyscore.presentation.mapper.ReportMapper.asReportView
import com.bertan.shinyscore.presentation.model.ReportView

class ReportViewModel(
    private val userId: String,
    private val getReportsUseCase: GetReports
) : RxUseCaseViewModel<ReportView?>(getReportsUseCase) {
    override fun onCreateViewModel() {
        postLoading()

        getReportsUseCase.execute(
            GetReports.Param(userId),
            onNext = { reports -> reports.firstOrNull()?.asReportView.postSuccess() },
            onError = { it.postError("Failed to load Reports for User($userId)!") }
        )
    }
}