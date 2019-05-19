package com.bertan.shinyscore.di

import com.bertan.shinyscore.domain.executor.SchedulerExecutor
import com.bertan.shinyscore.ui.executor.UiSchedulerExecutor
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val appModule: Module = module {
    single { this }
    single { UiSchedulerExecutor as SchedulerExecutor }
}