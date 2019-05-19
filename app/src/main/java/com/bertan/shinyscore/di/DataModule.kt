package com.bertan.shinyscore.di

import com.bertan.shinyscore.BuildConfig
import com.bertan.shinyscore.data.remote.service.ShinyScoreServiceFactory
import com.bertan.shinyscore.data.repository.DataRepository
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import com.bertan.shinyscore.data.local.memory.repository.LocalDataSourceImpl as LocalDataSourceMemoryImpl
import com.bertan.shinyscore.data.remote.memory.repository.RemoteDataSourceImpl as RemoteDataSourceMemoryImpl

val dataModule: Module = module {
    single {
        ShinyScoreServiceFactory.build(
            isDebug = BuildConfig.DEBUG
        )
    }
//    single {
//        RemoteDataSourceImpl(
//            service = get()
//        )
//    }
    single {
        RemoteDataSourceMemoryImpl(
            minReports = 1,
            maxReports = 100
        )
    }
    single {
        LocalDataSourceMemoryImpl()
    }
    single {
        DataRepository(
            localDataSource = get(),
            remoteDataSource = get()
        )
    }
}