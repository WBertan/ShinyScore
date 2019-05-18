package com.bertan.shinyscore.domain.repository

import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Repository :
    UserRepository,
    ReportRepository

interface UserRepository {
    fun getUser(userId: String): Single<User>
    fun addUser(user: User): Completable

    sealed class Error {
        data class UserNotFound(val userId: String)
    }
}

interface ReportRepository {
    fun getReports(userId: String): Observable<List<Report>>
    fun addReport(report: Report): Completable
}