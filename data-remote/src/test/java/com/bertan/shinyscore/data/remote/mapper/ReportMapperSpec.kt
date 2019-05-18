package com.bertan.shinyscore.data.remote.mapper

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.remote.mapper.ReportMapper.asReportEntity
import com.bertan.shinyscore.data.remote.test.CreditReportInfoRemoteDatFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class ReportMapperSpec {
    @Test
    fun `given remote when mapping to entity it should map`() {
        val remote = CreditReportInfoRemoteDatFactory.get()
        val expectedResult =
            ReportEntity(
                "userId",
                remote.clientRef,
                remote.maxScoreValue,
                remote.minScoreValue,
                remote.score
            )

        val result = remote.asReportEntity.copy(id = "userId")

        assertEquals(expectedResult, result)
    }
}