package com.numtory.application

import android.app.Application
import com.numtory.application.data.di.dataModule
import com.numtory.application.features.market.data.di.marketDataModule
import com.numtory.application.features.market.presenter.di.marketPresenterModule
import io.adtrace.sdk.AdTrace
import io.adtrace.sdk.AdTraceConfig
import io.adtrace.sdk.LogLevel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        val appToken = BuildConfig.ADTRACE_TOKEN
        val environment = AdTraceConfig.ENVIRONMENT_PRODUCTION
        val adtraceConfig = AdTraceConfig(this, appToken, environment)
        adtraceConfig.setLogLevel(LogLevel.VERBOSE)
        AdTrace.onCreate(adtraceConfig)
        registerActivityLifecycleCallbacks(AdTraceLifecycleCallbacks())
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule, marketDataModule, marketPresenterModule)
        }
    }

    private class AdTraceLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: android.app.Activity, savedInstanceState: android.os.Bundle?) {}
        override fun onActivityStarted(activity: android.app.Activity) {}
        override fun onActivityResumed(activity: android.app.Activity) {
            AdTrace.onResume()
        }
        override fun onActivityPaused(activity: android.app.Activity) {
            AdTrace.onPause()
        }
        override fun onActivityStopped(activity: android.app.Activity) {}
        override fun onActivitySaveInstanceState(activity: android.app.Activity, outState: android.os.Bundle) {}
        override fun onActivityDestroyed(activity: android.app.Activity) {}
    }
}