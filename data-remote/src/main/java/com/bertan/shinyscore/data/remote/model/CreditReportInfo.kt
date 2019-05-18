package com.bertan.shinyscore.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditReportInfo(
    val clientRef: String,
    val score: Long,
    val maxScoreValue: Long,
    val minScoreValue: Long
)