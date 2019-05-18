package com.bertan.shinyscore.domain.interactor.user

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.domain.interactor.ObservableUseCase
import com.bertan.shinyscore.domain.model.User
import com.bertan.shinyscore.domain.repository.Repository
import io.reactivex.Observable

class GetUser(
    private val repository: Repository,
    executor: SchedulerExecutor
) : ObservableUseCase<User, GetUser.Param>(executor) {

    override fun buildUseCase(params: Param?): Observable<User> =
        params.validate { repository.getUser(it.userId) }

    data class Param(val userId: String)
}