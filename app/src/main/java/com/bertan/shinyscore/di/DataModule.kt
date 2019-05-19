package com.bertan.shinyscore.di

import com.bertan.shinyscore.BuildConfig
import com.bertan.shinyscore.data.remote.repository.RemoteDataSourceImpl
import com.bertan.shinyscore.data.remote.service.ShinyScoreServiceFactory
import com.bertan.shinyscore.data.repository.DataRepository
import com.bertan.shinyscore.data.repository.LocalDataSource
import com.bertan.shinyscore.data.repository.RemoteDataSource
import com.bertan.shinyscore.domain.repository.Repository
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
    single {
        RemoteDataSourceImpl(
            service = get()
        ) as RemoteDataSource
    }
//    single {
//        RemoteDataSourceMemoryImpl(
//            minReports = 1,
//            maxReports = 100
//        ) as RemoteDataSource
//    }
    single {
        LocalDataSourceMemoryImpl() as LocalDataSource
    }
    single {
        DataRepository(
            localDataSource = get(),
            remoteDataSource = get()
        ) as Repository
    }
}