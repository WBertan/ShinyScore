package com.bertan.shinyscore.di

import com.bertan.shinyscore.presentation.vm.ReportViewModel
import com.bertan.shinyscore.presentation.vm.UserViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val presentationModule: Module = module {
    viewModel {
        UserViewModel(
            addUserUseCase = get(),
            getUserUseCase = get()
        )
    }
    viewModel {
        ReportViewModel(
            userId = getProperty("userId"),
            getReportsUseCase = get()
        )
    }
}