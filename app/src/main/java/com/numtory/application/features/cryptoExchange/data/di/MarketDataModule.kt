package com.numtory.application.features.cryptoExchange.data.di

import com.numtory.application.features.cryptoExchange.data.dataSource.MarketRemoteDataSource
import com.numtory.application.features.cryptoExchange.data.dataSource.MarketRemoteDataSourceImpl
import com.numtory.application.features.cryptoExchange.data.local.ExchangesLocalDataSource
import com.numtory.application.features.cryptoExchange.data.local.ExchangesLocalDataSourceImpl
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val marketDataModule = module {
    single<MarketRemoteDataSource> { MarketRemoteDataSourceImpl(get(), get()) }
    single<MarketRepository> { MarketRepositoryImpl(get(), get(), get(qualifier = named("IO"))) }


    factory<ExchangesLocalDataSource> {
        ExchangesLocalDataSourceImpl(get())
    }

}