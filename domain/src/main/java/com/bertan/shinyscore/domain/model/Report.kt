package com.bertan.shinyscore.domain.model

data class Report(
    val id: String,
    val userId: String,
    val maxScore: Long,
    val minScore: Long,
    val currentScore: Long
)