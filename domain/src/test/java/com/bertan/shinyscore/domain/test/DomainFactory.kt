package com.bertan.shinyscore.domain.test

import com.bertan.shinyscore.domain.model.Report
import com.bertan.shinyscore.domain.model.User
import java.util.*

abstract class DomainFactory<T> {
    abstract fun get(): T
    fun get(count: Int): List<T> = List(count) { get() }

    fun randomString(): String = UUID.randomUUID().toString()
    fun randomLong(): Long = Random().nextLong()
}

object UserDomainFactory : DomainFactory<User>() {
    override fun get(): User =
        User(
            randomString(),
            randomString()
        )
}

object ReportDomainFactory : DomainFactory<Report>() {
    override fun get(): Report =
        Report(
            randomString(),
            randomString(),
            randomLong(),
            randomLong(),
            randomLong()
        )
}