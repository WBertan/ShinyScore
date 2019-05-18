package com.bertan.shinyscore.presentation.mapper

import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.presentation.model.ReportView

object ReportMapper {
    val Report.asReportView: ReportView
        get() =
            ReportView(
                userId,
                maxScore,
                minScore,
                currentScore
            )
}