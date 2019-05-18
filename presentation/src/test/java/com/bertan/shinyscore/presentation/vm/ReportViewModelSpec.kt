package com.bertan.shinyscore.presentation.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.bertan.shinyscore.domain.interactor.report.GetReports
import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.presentation.mapper.ReportMapper.asReportView
import com.bertan.shinyscore.presentation.state.ViewState
import com.bertan.shinyscore.presentation.test.ReportDomainFactory
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReportViewModelSpec {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var reportViewModel: ReportViewModel

    private val getReportsUseCase: GetReports = mockk(relaxed = true)

    private val lifecycleOwner: LifecycleOwner = mockk()

    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        reportViewModel = ReportViewModel("userId", getReportsUseCase)

        lifecycle = LifecycleRegistry(lifecycleOwner)
        lifecycle.addObserver(reportViewModel)
    }

    @Test
    fun `given a lifecycle when ON_CREATE it should execute use case`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        verify(exactly = 1) { getReportsUseCase.execute(any(), any(), any(), any()) }
        confirmVerified(getReportsUseCase)
    }

    @Test
    fun `given a lifecycle when ON_CREATE it should post loading state`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        assertEquals(ViewState.Loading, reportViewModel.getState().value)
    }

    @Test
    fun `given a success use case with empty result when executes it should post success state`() {
        every { getReportsUseCase.execute(any(), captureLambda(), any(), any()) } answers {
            lambda<Function1<List<Report>, Unit>>().invoke(emptyList())
            mockk()
        }

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        assertEquals(ViewState.Success(null), reportViewModel.getState().value)
    }

    @Test
    fun `given a success use case with result when executes it should post success state`() {
        val reports = ReportDomainFactory.get(2)
        val reportsView = reports.map { it.asReportView }
        every { getReportsUseCase.execute(any(), captureLambda(), any(), any()) } answers {
            lambda<Function1<List<Report>, Unit>>().invoke(reports)
            mockk()
        }

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        assertEquals(ViewState.Success(reportsView.first()), reportViewModel.getState().value)
    }

    @Test
    fun `given a failure use case when executes it should post error state`() {
        val dummyError = Exception("dummyError")
        every { getReportsUseCase.execute(any(), any(), captureLambda(), any()) } answers {
            lambda<Function1<Throwable, Unit>>().invoke(dummyError)
            mockk()
        }

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        assertEquals(
            ViewState.Error("Failed to load Reports for User(userId)!", dummyError),
            reportViewModel.getState().value
        )
    }
}