package com.bertan.shinyscore.data.local.memory.repository

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.model.UserEntity
import com.bertan.shinyscore.data.repository.LocalDataSource
import com.bertan.shinyscore.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Observable

class LocalDataSourceImpl : LocalDataSource {
    private val users: MutableList<UserEntity> = mutableListOf()
    private val reports: MutableList<ReportEntity> = mutableListOf()

    override fun getUser(userId: String): Observable<UserEntity> =
        users.find { it.id == userId }
            ?.let { Observable.just(it) }
            ?: Observable.error(UserRepository.Error.UserNotFound(userId))

    override fun addUser(user: UserEntity): Completable =
        Completable.fromAction { users.add(user) }

    override fun getReports(userId: String): Observable<List<ReportEntity>> =
        Observable.just(reports.filter { it.userId == userId })

    override fun addReport(report: ReportEntity): Completable =
        Completable.fromAction { reports.add(report) }
}