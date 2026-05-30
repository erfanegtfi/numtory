package com.numtory.application.features.cryptoMarket.data.di

import com.numtory.application.features.cryptoMarket.data.dataSource.CryptoGlobalMarketRemoteDataSource
import com.numtory.application.features.cryptoMarket.data.dataSource.CryptoGlobalMarketRemoteDataSourceImpl
import com.numtory.application.features.cryptoMarket.data.local.CryptoGlobalExchangesLocalDataSource
import com.numtory.application.features.cryptoMarket.data.local.CryptoGlobalExchangesLocalDataSourceImpl
import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepository
import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val cryptoGlobalMarketDataModule = module {
    single<CryptoGlobalMarketRemoteDataSource> { CryptoGlobalMarketRemoteDataSourceImpl(get(), get()) }

    single<CryptoGlobalMarketRepository> {
        CryptoGlobalMarketRepositoryImpl(
            get(),
            get(),
            get(qualifier = named("IO"))
        )
    }


    factory<CryptoGlobalExchangesLocalDataSource> {
        CryptoGlobalExchangesLocalDataSourceImpl(get())
    }

}