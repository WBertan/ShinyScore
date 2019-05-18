package com.bertan.shinyscore.data.mapper

import com.bertan.shinyscore.data.mapper.ReportMapper.asReport
import com.bertan.shinyscore.data.mapper.ReportMapper.asReportEntity
import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.test.ReportDataFactory
import com.bertan.shinyscore.data.test.ReportDomainFactory
import com.bertan.shinyscore.domain.model.Report
import org.junit.Assert.assertEquals
import org.junit.Test

class ReportMapperSpec {
    @Test
    fun `given domain when mapping to entity it should map`() {
        val domain = ReportDomainFactory.get()
        val expectedResult =
            ReportEntity(
                domain.id,
                domain.userId,
                domain.maxScore,
                domain.minScore,
                domain.currentScore
            )

        val result = domain.asReportEntity

        assertEquals(expectedResult, result)
    }

    @Test
    fun `given entity when mapping to domain it should map`() {
        val entity = ReportDataFactory.get()
        val expectedResult =
            Report(
                entity.id,
                entity.userId,
                entity.maxScore,
                entity.minScore,
                entity.currentScore
            )

        val result = entity.asReport

        assertEquals(expectedResult, result)
    }
}