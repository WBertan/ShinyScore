package com.bertan.shinyscore.domain.interactor.user

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.repository.Repository
import com.bertan.shinyscore.domain.repository.UserRepository
import com.bertan.shinyscore.domain.test.UserDomainFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class GetUserSpec {
    private lateinit var getUser: GetUser

    private val repository: Repository = mockk()
    private val executor: SchedulerExecutor = mockk()

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun setup() {
        getUser = GetUser(repository, executor)
    }

    @Test
    fun `given a response when executes it should completes`() {
        every { repository.getUser(any()) } returns Observable.just(UserDomainFactory.get())

        val result = getUser.buildUseCase(GetUser.Param("userId")).test()

        result.assertComplete()
    }

    @Test
    fun `given a null param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        getUser.buildUseCase(null)
    }

    @Test
    fun `given an empty param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        getUser.buildUseCase()
    }

    @Test
    fun `given a found response when executes it should return data`() {
        val user = UserDomainFactory.get()
        every { repository.getUser(any()) } returns Observable.just(user)

        val result = getUser.buildUseCase(GetUser.Param(user.id)).test()

        result.assertValue(user)
    }

    @Test
    fun `given a not found response when executes it should return exception`() {
        val exception = UserRepository.Error.UserNotFound("notFoundId")
        every { repository.getUser(any()) } returns Observable.error(exception)

        val result = getUser.buildUseCase(GetUser.Param("notFoundId")).test()

        result.assertError(exception)
    }
}