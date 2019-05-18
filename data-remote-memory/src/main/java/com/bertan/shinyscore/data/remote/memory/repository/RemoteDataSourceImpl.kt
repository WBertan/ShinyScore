package com.bertan.shinyscore.data.remote.memory.repository

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.repository.RemoteDataSource
import io.reactivex.Observable
import java.util.*
import kotlin.random.Random

class RemoteDataSourceImpl(private val minReports: Int, private val maxReports: Int) : RemoteDataSource {
    private fun randomReports(userId: String): List<ReportEntity> =
        List(Random.nextInt(minReports, maxReports)) {
            val maxScore = Random.nextLong(500, 1000)
            val minScore = Random.nextLong(0, maxScore)
            val currentScore = Random.nextLong(minScore, maxScore)
            ReportEntity(
                UUID.randomUUID().toString(),
                userId,
                maxScore,
                minScore,
                currentScore
            )
        }

    override fun getReports(userId: String): Observable<List<ReportEntity>> =
        Observable.just(randomReports(userId))
}