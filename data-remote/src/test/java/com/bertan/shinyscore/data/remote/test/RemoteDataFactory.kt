package com.bertan.shinyscore.data.remote.test

import com.bertan.shinyscore.data.remote.model.CreditReport
import com.bertan.shinyscore.data.remote.model.CreditReportInfo
import java.util.*

abstract class RemoteDataFactory<T> {
    abstract fun get(): T
    fun get(count: Int): List<T> = List(count) { get() }

    fun randomString(): String = UUID.randomUUID().toString()
    fun randomLong(): Long = Random().nextLong()
}

object CreditReportRemoteDataFactory : RemoteDataFactory<CreditReport>() {
    override fun get(): CreditReport =
        CreditReport(
            CreditReportInfoRemoteDatFactory.get()
        )
}

object CreditReportInfoRemoteDatFactory : RemoteDataFactory<CreditReportInfo>() {
    override fun get(): CreditReportInfo =
        CreditReportInfo(
            randomString(),
            randomLong(),
            randomLong(),
            randomLong()
        )
}