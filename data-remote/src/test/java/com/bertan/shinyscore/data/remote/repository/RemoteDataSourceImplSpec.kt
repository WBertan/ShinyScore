package com.bertan.shinyscore.data.remote.repository

import com.bertan.shinyscore.data.remote.mapper.ReportMapper.asReportEntity
import com.bertan.shinyscore.data.remote.service.ShinyScoreService
import com.bertan.shinyscore.data.remote.test.CreditReportRemoteDataFactory
import com.bertan.shinyscore.data.repository.RemoteDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import java.util.*

class RemoteDataSourceImplSpec {
    private lateinit var dataSource: RemoteDataSourceImpl

    private val service: ShinyScoreService = mockk()

    @Before
    fun setup() {
        dataSource = RemoteDataSourceImpl(service)
    }

    private fun <T> TestObserver<T>.assertCompletedValue(expected: T) {
        assertComplete()
        assertValue(expected)
    }

    @Test
    fun `given a response when getReports it should completes and return data`() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID().toString() } returns "randomUUID"

        val creditReport = CreditReportRemoteDataFactory.get()
        every { service.getCreditReport(any()) } returns Observable.just(creditReport)

        val result = dataSource.getReports("userId").test()

        result.assertCompletedValue(listOf(creditReport.creditReportInfo.asReportEntity))

        unmockkStatic(UUID::class)
    }

    @Test
    fun `given an error response when getReports it should completes and return exception`() {
        every { service.getCreditReport(any()) } returns Observable.error(Exception("dummyError"))

        val result = dataSource.getReports("userId").test()

        result.assertError(RemoteDataSource.Error.GetReportsFailed("userId", "dummyError"))
    }
}