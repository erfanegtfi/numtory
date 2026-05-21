package com.numtory.application.features.gold.data.di

import com.numtory.application.features.gold.data.dataSource.GoldMarketRemoteDataSource
import com.numtory.application.features.gold.data.dataSource.GoldMarketRemoteDataSourceImpl
import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSourceImpl
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.data.repositories.GoldMarketRepositoryImpl
import com.numtory.application.features.market.data.dataSource.MarketRemoteDataSource
import com.numtory.application.features.market.data.dataSource.MarketRemoteDataSourceImpl
import com.numtory.application.features.market.data.local.ExchangesLocalDataSource
import com.numtory.application.features.market.data.local.ExchangesLocalDataSourceImpl
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.data.repositories.MarketRepositoryImpl
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