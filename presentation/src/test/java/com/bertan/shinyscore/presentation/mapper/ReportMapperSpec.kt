package com.bertan.shinyscore.presentation.mapper

import com.bertan.shinyscore.presentation.mapper.ReportMapper.asReportView
import com.bertan.shinyscore.presentation.model.ReportView
import com.bertan.shinyscore.presentation.test.ReportDomainFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class ReportMapperSpec {
    @Test
    fun `given domain when mapping to view it should map`() {
        val domain = ReportDomainFactory.get()
        val expectedResult =
            ReportView(
                domain.userId,
                domain.maxScore,
                domain.minScore,
                domain.currentScore
            )

        val result = domain.asReportView

        assertEquals(expectedResult, result)
    }
}