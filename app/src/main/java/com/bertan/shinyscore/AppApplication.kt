package com.bertan.shinyscore

import android.app.Application
import com.bertan.shinyscore.di.appModule
import com.bertan.shinyscore.di.dataModule
import com.bertan.shinyscore.di.domainModule
import com.bertan.shinyscore.di.presentationModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupTimber()
    }

    private fun setupKoin() {
        startKoin(
            this,
            listOf(
                domainModule,
                dataModule,
                presentationModule,
                appModule
            )
        )
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}