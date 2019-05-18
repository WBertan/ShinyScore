package com.bertan.shinyscore.domain.interactor.user

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.repository.Repository
import com.bertan.shinyscore.domain.test.UserDomainFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class AddUserSpec {
    private lateinit var addUser: AddUser

    private val repository: Repository = mockk()
    private val executor: SchedulerExecutor = mockk()

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun setup() {
        addUser = AddUser(repository, executor)
    }

    @Test
    fun `given a response when executes it should completes`() {
        every { repository.addUser(any()) } returns Completable.complete()

        val result = addUser.buildUseCase(AddUser.Param(UserDomainFactory.get())).test()

        result.assertComplete()
    }

    @Test
    fun `given a null param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        addUser.buildUseCase(null)
    }

    @Test
    fun `given an empty param when executes it should throw IllegalArgumentException`() {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Parameter should not be null!")
        addUser.buildUseCase()
    }
}