package com.bertan.shinyscore.presentation.vm

import com.bertan.shinyscore.domain.interactor.user.AddUser
import com.bertan.shinyscore.domain.interactor.user.GetUser
import com.bertan.shinyscore.presentation.mapper.UserMapper.asUser
import com.bertan.shinyscore.presentation.mapper.UserMapper.asUserView
import com.bertan.shinyscore.presentation.model.UserView

class UserViewModel(
    private val addUserUseCase: AddUser,
    private val getUserUseCase: GetUser
) : RxUseCaseViewModel<UserView>(addUserUseCase, getUserUseCase) {

    override fun onCreateViewModel() = Unit

    fun getUser(userId: String) {
        postLoading()

        getUserUseCase.execute(
            GetUser.Param(userId),
            onNext = { user -> user.asUserView.postSuccess() },
            onError = { it.postError("Failed to load User($userId)!") }
        )
    }

    fun addUser(user: UserView) {
        postLoading()

        addUserUseCase.execute(
            AddUser.Param(user.asUser),
            onComplete = { user.postSuccess() },
            onError = { it.postError("Failed to add $user!") }
        )
    }
}