package com.bertan.shinyscore.data.remote.mapper

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.remote.model.CreditReportInfo
import java.util.*

object ReportMapper {
    val CreditReportInfo.asReportEntity: ReportEntity
        get() =
            ReportEntity(
                UUID.randomUUID().toString(),
                clientRef,
                maxScoreValue,
                minScoreValue,
                score
            )
}