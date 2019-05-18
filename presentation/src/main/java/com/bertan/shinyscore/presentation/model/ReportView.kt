package com.bertan.shinyscore.presentation.model

data class ReportView(
    val userId: String,
    val maxScore: Long,
    val minScore: Long,
    val currentScore: Long
)