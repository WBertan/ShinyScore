package com.bertan.shinyscore.domain.interactor.user

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.interactor.CompletableUseCase
import com.bertan.shinyscore.domain.model.User
import com.bertan.shinyscore.domain.repository.Repository
import io.reactivex.Completable

class AddUser(
    private val repository: Repository,
    executor: SchedulerExecutor
) : CompletableUseCase<AddUser.Param>(executor) {

    override fun buildUseCase(params: Param?): Completable =
        params.validate { repository.addUser(it.user) }

    data class Param(val user: User)
}