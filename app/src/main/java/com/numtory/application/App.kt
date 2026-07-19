package com.numtory.application

import android.app.Application
import com.numtory.application.data.di.dataModule
import com.numtory.application.features.cryptoMarket.data.di.cryptoGlobalMarketDataModule
import com.numtory.application.features.cryptoMarket.presenter.di.cryptoGlobalMarketPresenterModule
import com.numtory.application.features.gold.data.di.goldMarketDataModule
import com.numtory.application.features.gold.presenter.di.goldMarketPresenterModule
import com.numtory.application.features.main.di.settingsPresenterModule
import com.numtory.application.features.market.data.di.marketDataModule
import com.numtory.application.features.market.presenter.di.marketPresenterModule
import com.numtory.application.features.notification.data.NotificationPresenter
import com.numtory.application.features.notification.di.notificationModule
import com.numtory.application.features.scan.data.di.scanDataModule
import com.numtory.application.features.scan.presenter.di.scanPresenterModule
import com.numtory.application.features.seke.data.di.sekeDataModule
import com.numtory.application.features.seke.presenter.di.sekePresenterModule
import com.numtory.application.features.setting.data.di.settingsDataModule
import io.adtrace.sdk.AdTrace
import io.adtrace.sdk.AdTraceConfig
import io.adtrace.sdk.LogLevel
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val notificationPresenter: NotificationPresenter by inject()

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
            modules(
                dataModule,
                marketDataModule,
                goldMarketDataModule,
                marketPresenterModule,
                goldMarketPresenterModule,
                settingsDataModule,
                settingsPresenterModule,
                cryptoGlobalMarketDataModule,
                cryptoGlobalMarketPresenterModule,
                sekeDataModule,
                sekePresenterModule,
                scanDataModule,
                scanPresenterModule,
                notificationModule
            )
        }

        // The channel must exist before the first notification arrives, including ones FCM renders
        // itself while the app is backgrounded.
        notificationPresenter.createChannel()
    }

    private class AdTraceLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(
            activity: android.app.Activity,
            savedInstanceState: android.os.Bundle?
        ) {
        }

        override fun onActivityStarted(activity: android.app.Activity) {}
        override fun onActivityResumed(activity: android.app.Activity) {
            AdTrace.onResume()
        }

        override fun onActivityPaused(activity: android.app.Activity) {
            AdTrace.onPause()
        }

        override fun onActivityStopped(activity: android.app.Activity) {}
        override fun onActivitySaveInstanceState(
            activity: android.app.Activity,
            outState: android.os.Bundle
        ) {
        }

        override fun onActivityDestroyed(activity: android.app.Activity) {}
    }
}