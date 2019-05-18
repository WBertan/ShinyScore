package com.bertan.shinyscore.data.model

data class ReportEntity(
    val id: String,
    val userId: String,
    val maxScore: Long,
    val minScore: Long,
    val currentScore: Long
)