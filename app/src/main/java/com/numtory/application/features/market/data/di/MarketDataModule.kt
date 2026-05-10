package com.numtory.application.features.market.data.di

import com.numtory.application.features.market.data.dataSource.MarketRemoteDataSource
import com.numtory.application.features.market.data.dataSource.MarketRemoteDataSourceImpl
import com.numtory.application.features.market.data.local.ExchangesLocalDataSource
import com.numtory.application.features.market.data.local.ExchangesLocalDataSourceImpl
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.data.repositories.MarketRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val marketDataModule = module {
    single<MarketRemoteDataSource> { MarketRemoteDataSourceImpl(get(), get()) }
    single<MarketRepository> { MarketRepositoryImpl(get(), get(), get(qualifier = named("IO"))) }


    factory<ExchangesLocalDataSource> {
        ExchangesLocalDataSourceImpl(get())
    }

}