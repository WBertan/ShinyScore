package com.bertan.shinyscore.presentation.test

import com.bertan.shinyscore.presentation.model.ReportView
import com.bertan.shinyscore.presentation.model.UserView
import java.util.*

abstract class ViewFactory<T> {
    abstract fun get(): T
    fun get(count: Int): List<T> = List(count) { get() }

    fun randomString(): String = UUID.randomUUID().toString()
    fun randomLong(): Long = Random().nextLong()
}

object UserViewFactory : ViewFactory<UserView>() {
    override fun get(): UserView =
        UserView(
            randomString(),
            randomString(),
            randomString()
        )
}

object ReportViewFactory : ViewFactory<ReportView>() {
    override fun get(): ReportView =
        ReportView(
            randomString(),
            randomLong(),
            randomLong(),
            randomLong()
        )
}