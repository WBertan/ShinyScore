package com.bertan.shinyscore.di

import com.bertan.shinyscore.domain.interactor.report.GetReports
import com.bertan.shinyscore.domain.interactor.user.AddUser
import com.bertan.shinyscore.domain.interactor.user.GetUser
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val domainModule: Module = module {
    single {
        GetReports(
            repository = get(),
            executor = get()
        )
    }
    single {
        AddUser(
            repository = get(),
            executor = get()
        )
    }
    single {
        GetUser(
            repository = get(),
            executor = get()
        )
    }
}