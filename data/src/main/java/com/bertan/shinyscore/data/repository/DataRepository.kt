package com.bertan.shinyscore.data.repository

import com.bertan.shinyscore.data.mapper.ReportMapper.asReport
import com.bertan.shinyscore.data.mapper.ReportMapper.asReportEntity
import com.bertan.shinyscore.data.mapper.UserMapper.asUser
import com.bertan.shinyscore.data.mapper.UserMapper.asUserEntity
import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.domain.model.User
import com.bertan.shinyscore.domain.repository.Repository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableDefer

class DataRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {
    override fun getUser(userId: String): Observable<User> =
        localDataSource.getUser(userId)
            .map { it.asUser }

    override fun addUser(user: User): Completable =
        localDataSource.addUser(user.asUserEntity)

    override fun getReports(userId: String): Observable<List<Report>> =
        remoteDataSource.getReports(userId)
            .flatMapCompletable { reports ->
                Completable.merge(reports.map { localDataSource.addReport(it) })
            }
            .onErrorComplete()
            .andThen(ObservableDefer.defer { localDataSource.getReports(userId) })
            .map { result -> result.map { it.asReport } }

    override fun addReport(report: Report): Completable =
        localDataSource.addReport(report.asReportEntity)
}