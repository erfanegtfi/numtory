package com.numtory.application.features.seke.data.di

import com.numtory.application.features.seke.data.dataSource.SekeRemoteDataSource
import com.numtory.application.features.seke.data.dataSource.SekeRemoteDataSourceImpl
import com.numtory.application.features.seke.data.repositories.SekeRepository
import com.numtory.application.features.seke.data.repositories.SekeRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sekeDataModule = module {
    single<SekeRemoteDataSource> { SekeRemoteDataSourceImpl(get(), get()) }

    single<SekeRepository> {
        SekeRepositoryImpl(
            get(),
            get(qualifier = named("IO"))
        )
    }


}