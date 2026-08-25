package com.numtory.application.features.goldExchange.data.di

import com.numtory.application.features.goldExchange.data.dataSource.GoldMarketRemoteDataSource
import com.numtory.application.features.goldExchange.data.dataSource.GoldMarketRemoteDataSourceImpl
import com.numtory.application.features.goldExchange.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.goldExchange.data.local.GoldExchangesLocalDataSourceImpl
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepository
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val goldMarketDataModule = module {
    single<GoldMarketRemoteDataSource> { GoldMarketRemoteDataSourceImpl(get(), get()) }
    single<GoldMarketRepository> {
        GoldMarketRepositoryImpl(
            get(),
            get(),
            get(qualifier = named("IO"))
        )
    }


    factory<GoldExchangesLocalDataSource> {
        GoldExchangesLocalDataSourceImpl(get())
    }

}