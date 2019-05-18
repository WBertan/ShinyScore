package com.bertan.shinyscore.data.test

import com.bertan.shinyscore.data.model.ReportEntity
import com.bertan.shinyscore.data.model.UserEntity
import java.util.*

abstract class DataFactory<T> {
    abstract fun get(): T
    fun get(count: Int): List<T> = List(count) { get() }

    fun randomString(): String = UUID.randomUUID().toString()
    fun randomLong(): Long = Random().nextLong()
}

object UserDataFactory : DataFactory<UserEntity>() {
    override fun get(): UserEntity =
        UserEntity(
            randomString(),
            randomString()
        )
}

object ReportDataFactory : DataFactory<ReportEntity>() {
    override fun get(): ReportEntity =
        ReportEntity(
            randomString(),
            randomString(),
            randomLong(),
            randomLong(),
            randomLong()
        )
}