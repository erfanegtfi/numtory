package com.numtory.application.features.setting.data.di

import com.numtory.application.features.setting.data.dataSource.SettingsRemoteDataSource
import com.numtory.application.features.setting.data.dataSource.SettingsRemoteDataSourceImpl
import com.numtory.application.features.setting.data.local.SettingsLocalDataSource
import com.numtory.application.features.setting.data.local.SettingsLocalDataSourceImpl
import com.numtory.application.features.setting.data.repositories.SettingsRepository
import com.numtory.application.features.setting.data.repositories.SettingsRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsDataModule = module {
    single<SettingsRemoteDataSource> { SettingsRemoteDataSourceImpl(get(), get()) }
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            get(),
            get(),
            get(qualifier = named("IO"))
        )
    }


    factory<SettingsLocalDataSource> {
        SettingsLocalDataSourceImpl(get())
    }

}