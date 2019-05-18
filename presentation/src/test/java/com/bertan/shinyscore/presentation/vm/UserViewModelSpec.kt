package com.bertan.shinyscore.presentation.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.bertan.shinyscore.domain.interactor.user.AddUser
import com.bertan.shinyscore.domain.interactor.user.GetUser
import com.bertan.shinyscore.domain.model.User
import com.bertan.shinyscore.presentation.mapper.UserMapper.asUserView
import com.bertan.shinyscore.presentation.state.ViewState
import com.bertan.shinyscore.presentation.test.UserDomainFactory
import com.bertan.shinyscore.presentation.test.UserViewFactory
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelSpec {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userViewModel: UserViewModel

    private val getUserUseCase: GetUser = mockk(relaxed = true)
    private val addUserUseCase: AddUser = mockk(relaxed = true)

    private val lifecycleOwner: LifecycleOwner = mockk()

    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        userViewModel = UserViewModel(addUserUseCase, getUserUseCase)

        lifecycle = LifecycleRegistry(lifecycleOwner)
        lifecycle.addObserver(userViewModel)
    }

    @Test
    fun `given a lifecycle when ON_CREATE it should not interact with the use cases`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        confirmVerified(getUserUseCase)
        confirmVerified(addUserUseCase)
    }

    @Test
    fun `given a userId when call getUser it should post loading state`() {
        userViewModel.getUser("userId")

        assertEquals(ViewState.Loading, userViewModel.getState().value)
    }

    @Test
    fun `given a success use case when executes getUser it should post success state`() {
        val user = UserDomainFactory.get()
        every { getUserUseCase.execute(any(), captureLambda(), any(), any()) } answers {
            lambda<Function1<User, Unit>>().invoke(user)
            mockk()
        }

        userViewModel.getUser("userId")

        assertEquals(ViewState.Success(user.asUserView), userViewModel.getState().value)
    }

    @Test
    fun `given a failure use case when executes getUser it should post error state`() {
        val dummyError = Exception("dummyError")
        every { getUserUseCase.execute(any(), any(), captureLambda(), any()) } answers {
            lambda<Function1<Throwable, Unit>>().invoke(dummyError)
            mockk()
        }

        userViewModel.getUser("userId")

        assertEquals(
            ViewState.Error("Failed to load User(userId)!", dummyError),
            userViewModel.getState().value
        )
    }

    @Test
    fun `given a user when call addUser it should post loading state`() {
        val userView = UserViewFactory.get()
        userViewModel.addUser(userView)

        assertEquals(ViewState.Loading, userViewModel.getState().value)
    }

    @Test
    fun `given a success use case when executes addUser it should post success state`() {
        val userView = UserViewFactory.get()
        every { addUserUseCase.execute(any(), captureLambda(), any()) } answers {
            lambda<Function0<Unit>>().invoke()
            mockk()
        }

        userViewModel.addUser(userView)

        assertEquals(ViewState.Success(userView), userViewModel.getState().value)
    }

    @Test
    fun `given a failure use case when executes addUser it should post error state`() {
        val userView = UserViewFactory.get()
        val dummyError = Exception("dummyError")
        every { addUserUseCase.execute(any(), any(), captureLambda()) } answers {
            lambda<Function1<Throwable, Unit>>().invoke(dummyError)
            mockk()
        }

        userViewModel.addUser(userView)

        assertEquals(
            ViewState.Error("Failed to add $userView!", dummyError),
            userViewModel.getState().value
        )
    }
}