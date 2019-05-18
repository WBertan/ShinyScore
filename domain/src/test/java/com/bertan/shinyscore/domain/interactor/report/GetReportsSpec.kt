package com.bertan.shinyscore.domain.interactor.report

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.domain.repository.Repository
import com.bertan.shinyscore.domain.test.ReportDomainFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class GetReportsSpec {
    private lateinit var getReports: GetReports

    private val repository: Repository = mockk()
    private val executor: SchedulerExecutor = mockk()

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun setup() {
        getReports = GetReports(repository, executor)
    }

    @Test
    fun `given a response when executes it should completes`() {
        every { repository.getReports(any()) } returns Observable.just(ReportDomainFactory.get(2))

        val result = getReports.buildUseCase(GetReports.Param("userId")).test()

        result.assertComplete()
    }

    @Test
    fun `given a null param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        getReports.buildUseCase(null)
    }

    @Test
    fun `given an empty param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        getReports.buildUseCase()
    }

    @Test
    fun `given a found response when executes it should return data`() {
        val reports = ReportDomainFactory.get(2)
        every { repository.getReports(any()) } returns Observable.just(reports)

        val result = getReports.buildUseCase(GetReports.Param("userId")).test()

        result.assertValue(reports)
    }

    @Test
    fun `given a not found response when executes it should return empty data`() {
        val reports: List<Report> = emptyList()
        every { repository.getReports(any()) } returns Observable.just(reports)

        val result = getReports.buildUseCase(GetReports.Param("notFoundId")).test()

        result.assertValue(reports)
    }
}