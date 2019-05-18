package com.bertan.shinyscore.data.mapper

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.domain.model.Report

object ReportMapper {
    val ReportEntity.asReport: Report
        get() =
            Report(
                id,
                userId,
                maxScore,
                minScore,
                currentScore
            )

    val Report.asReportEntity: ReportEntity
        get() =
            ReportEntity(
                id,
                userId,
                maxScore,
                minScore,
                currentScore
            )
}