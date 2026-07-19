package com.numtory.application.features.notification.di

import com.numtory.application.features.notification.data.DeepLinkRouter
import com.numtory.application.features.notification.data.NotificationPresenter
import com.numtory.application.features.notification.data.local.PushTokenLocalDataSource
import com.numtory.application.features.notification.data.local.PushTokenLocalDataSourceImpl
import com.numtory.application.features.notification.data.repositories.PushTokenRepository
import com.numtory.application.features.notification.data.repositories.PushTokenRepositoryImpl
import org.koin.dsl.module

val notificationModule = module {

    factory<PushTokenLocalDataSource> { PushTokenLocalDataSourceImpl(get()) }

    single<PushTokenRepository> { PushTokenRepositoryImpl(get()) }

    single<NotificationPresenter> { NotificationPresenter(get()) }

    single<DeepLinkRouter> { DeepLinkRouter() }
}
