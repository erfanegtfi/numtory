package com.numtory.application

import android.app.Application
import com.numtory.application.data.di.dataModule
import com.numtory.application.features.market.data.di.marketDataModule
import com.numtory.application.features.market.presenter.di.marketPresenterModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule, marketDataModule, marketPresenterModule)
        }
    }
}