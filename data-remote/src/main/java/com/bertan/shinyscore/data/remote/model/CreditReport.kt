package com.bertan.shinyscore.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditReport(
    val creditReportInfo: CreditReportInfo
)