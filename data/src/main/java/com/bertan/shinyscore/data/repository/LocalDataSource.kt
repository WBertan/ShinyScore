package com.bertan.shinyscore.data.repository

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.model.UserEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface LocalDataSource {
    fun getUser(userId: String): Observable<UserEntity>
    fun addUser(user: UserEntity): Completable

    fun getReports(userId: String): Observable<List<ReportEntity>>
    fun addReport(report: ReportEntity): Completable
}