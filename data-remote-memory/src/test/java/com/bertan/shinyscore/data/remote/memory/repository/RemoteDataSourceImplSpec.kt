package com.bertan.shinyscore.data.remote.memory.repository

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RemoteDataSourceImplSpec {
    private lateinit var dataSource: RemoteDataSourceImpl

    private val minReports = 100
    private val maxReports = 200

    @Before
    fun setup() {
        dataSource = RemoteDataSourceImpl(minReports, maxReports)
    }

    @Test
    fun `given a response when getReports it should completes`() {
        val result = dataSource.getReports("userId").test()

        result.assertComplete()
    }

    @Test
    fun `given a response when getReports it should generate reports between the specified minReports and maxReports`() {
        val result = dataSource.getReports("userId").test().values().first()

        assertTrue(IntRange(minReports, maxReports).contains(result.size))
    }

    @Test
    fun `given a response when getReports it should generate reports with consistent data`() {
        val result = dataSource.getReports("userId").test().values().first()

        assertTrue(
            result.all { report ->
                val minScore = report.minScore
                val maxScore = report.maxScore
                val currentScore = report.currentScore

                val isMinBeforeMax = minScore <= maxScore
                val isCurrentScoreBetweenMinAndMax = LongRange(minScore, maxScore).contains(currentScore)

                isMinBeforeMax && isCurrentScoreBetweenMinAndMax
            }
        )
    }
}