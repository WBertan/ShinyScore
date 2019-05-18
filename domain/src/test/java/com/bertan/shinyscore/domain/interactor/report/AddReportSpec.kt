package com.bertan.shinyscore.domain.interactor.report

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.repository.Repository
import com.bertan.shinyscore.domain.test.ReportDomainFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class AddReportSpec {
    private lateinit var addReport: AddReport

    private val repository: Repository = mockk()
    private val executor: SchedulerExecutor = mockk()

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun setup() {
        addReport = AddReport(repository, executor)
    }

    @Test
    fun `given a response when executes it should completes`() {
        every { repository.addReport(any()) } returns Completable.complete()

        val result = addReport.buildUseCase(AddReport.Param(ReportDomainFactory.get())).test()

        result.assertComplete()
    }

    @Test
    fun `given a null param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        addReport.buildUseCase(null)
    }

    @Test
    fun `given an empty param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        addReport.buildUseCase()
    }
}