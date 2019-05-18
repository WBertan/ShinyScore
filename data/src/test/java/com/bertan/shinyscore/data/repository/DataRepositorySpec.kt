package com.bertan.shinyscore.data.repository

import com.bertan.shinyscore.data.mapper.ReportMapper.asReportEntity
import com.bertan.shinyscore.data.mapper.UserMapper.asUserEntity
import com.bertan.shinyscore.data.test.ReportDomainFactory
import com.bertan.shinyscore.data.test.UserDomainFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class DataRepositorySpec {
    private lateinit var dataRepository: DataRepository

    private val localDataSource: LocalDataSource = mockk()
    private val remoteDataSource: RemoteDataSource = mockk()

    @Before
    fun setup() {
        dataRepository = DataRepository(localDataSource, remoteDataSource)
    }

    private object MockError : Exception()

    private fun <T> TestObserver<T>.assertCompletedValue(expected: T) {
        assertComplete()
        assertValue(expected)
    }

    @Test
    fun `given a found response when getUser it should completes and return data`() {
        val user = UserDomainFactory.get()
        every { localDataSource.getUser(any()) } returns Observable.just(user.asUserEntity)

        val result = dataRepository.getUser("userId").test()

        result.assertCompletedValue(user)
    }

    @Test
    fun `given a not found response when getUser it should completes and return exception`() {
        every { localDataSource.getUser(any()) } returns Observable.error(MockError)

        val result = dataRepository.getUser("notFoundId").test()

        result.assertError(MockError)
    }

    @Test
    fun `given a response when addUser it should completes`() {
        every { localDataSource.addUser(any()) } returns Completable.complete()

        val result = dataRepository.addUser(UserDomainFactory.get()).test()

        result.assertComplete()
    }

    @Test
    fun `given a response when getReports it should completes and return data`() {
        val remoteReports = ReportDomainFactory.get(2)
        val remoteReportsEntity = remoteReports.map { it.asReportEntity }
        every { remoteDataSource.getReports(any()) } returns Observable.just(remoteReportsEntity)

        val localReports = ReportDomainFactory.get(2)
        val localReportsEntity = localReports.map { it.asReportEntity }

        every { localDataSource.addReport(any()) } returns Completable.complete()
        every { localDataSource.getReports(any()) } returns Observable.just(localReportsEntity + remoteReportsEntity)

        val result = dataRepository.getReports("userId").test()

        verify { localDataSource.addReport(remoteReportsEntity.first()) }
        verify { localDataSource.addReport(remoteReportsEntity.last()) }
        result.assertCompletedValue(localReports + remoteReports)
    }

    @Test
    fun `given remote failure when getReports it should completes and return local data`() {
        val localReports = ReportDomainFactory.get(2)
        val localReportsEntity = localReports.map { it.asReportEntity }

        every { localDataSource.getReports(any()) } returns Observable.just(localReportsEntity)

        every { remoteDataSource.getReports(any()) } returns Observable.error(MockError)

        val result = dataRepository.getReports("userId").test()

        result.assertCompletedValue(localReports)
    }

    @Test
    fun `given remote response when getReports it should completes and populate local with remote data and return data`() {
        val remoteReports = ReportDomainFactory.get(2)
        val remoteReportsEntity = remoteReports.map { it.asReportEntity }
        every { remoteDataSource.getReports(any()) } returns Observable.just(remoteReportsEntity)

        every { localDataSource.addReport(any()) } returns Completable.complete()
        every { localDataSource.getReports(any()) } returns Observable.just(remoteReportsEntity)

        val result = dataRepository.getReports("userId").test()

        verify { localDataSource.addReport(remoteReportsEntity.first()) }
        verify { localDataSource.addReport(remoteReportsEntity.last()) }
        result.assertCompletedValue(remoteReports)
    }

    @Test
    fun `given a response when addReport it should completes`() {
        every { localDataSource.addReport(any()) } returns Completable.complete()

        val result = dataRepository.addReport(ReportDomainFactory.get()).test()

        result.assertComplete()
    }
}