package com.bertan.shinyscore.data.local.memory.repository

import com.bertan.shinyscore.data.local.memory.test.ReportDataFactory
import com.bertan.shinyscore.data.local.memory.test.UserDataFactory
import com.bertan.shinyscore.data.model.UserEntity
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class LocalDataSourceImplSpec {
    private lateinit var dataSource: LocalDataSourceImpl

    @Before
    fun setup() {
        dataSource = LocalDataSourceImpl()
    }

    private fun <T> TestObserver<T>.assertCompletedValue(expected: T) {
        assertComplete()
        assertValue(expected)
    }

    @Test
    fun `given a found response when getUser it should completes and return data`() {
        val user = UserDataFactory.get().copy(id = "userId")
        val otherUser = UserDataFactory.get().copy(id = "otherUserId")

        dataSource.addUser(user).test()
        dataSource.addUser(otherUser).test()
        val result = dataSource.getUser("userId").test()

        result.assertCompletedValue(user)
    }

//    @Test
//    fun `given a not found response when getUser it should completes and return exception`() {
//        val user = UserDataFactory.get().copy(id = "userId")
//
//        dataSource.addUser(user).test()
//        val result = dataSource.getUser("notFoundId").test()
//
//        result.assertError(UserRepository.Error.UserNotFound("notFoundId"))
//    }

    @Test
    fun `given a not found response when getUser it should completes and return default user`() {
        val user = UserDataFactory.get().copy(id = "userId")
        val defaultUser = UserEntity("wonderful-user-id", "Wonderful User")

        dataSource.addUser(user).test()
        val result = dataSource.getUser("notFoundId").test()

        result.assertCompletedValue(defaultUser)
    }

    @Test
    fun `given a response when addUser it should completes`() {
        val user = UserDataFactory.get()

        val result = dataSource.addUser(user).test()

        result.assertComplete()
    }

    @Test
    fun `given a found response when getReports it should completes and return data`() {
        val reports = ReportDataFactory.get(2).map { it.copy(userId = "userId") }
        val otherReports = ReportDataFactory.get(2).map { it.copy(userId = "otherUserId") }

        dataSource.addReport(reports.first()).test()
        dataSource.addReport(reports.last()).test()
        dataSource.addReport(otherReports.first()).test()
        dataSource.addReport(otherReports.last()).test()

        val result = dataSource.getReports("userId").test()

        result.assertCompletedValue(reports)
    }

    @Test
    fun `given a not found response when getReports it should completes and return empty data`() {
        val reports = ReportDataFactory.get(2).map { it.copy(userId = "userId") }

        dataSource.addReport(reports.first()).test()
        dataSource.addReport(reports.last()).test()

        val result = dataSource.getReports("notFoundId").test()

        result.assertCompletedValue(emptyList())
    }

    @Test
    fun `given a response when addReport it should completes`() {
        val report = ReportDataFactory.get()

        val result = dataSource.addReport(report).test()

        result.assertComplete()
    }
}