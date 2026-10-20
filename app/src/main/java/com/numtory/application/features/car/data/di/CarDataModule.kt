package com.numtory.application.features.car.data.di

import com.numtory.application.features.car.data.dataSource.CarRemoteDataSource
import com.numtory.application.features.car.data.dataSource.CarRemoteDataSourceImpl
import com.numtory.application.features.car.data.repository.CarRepository
import com.numtory.application.features.car.data.repository.CarRepositoryImpl
import com.numtory.application.features.cryptoExchange.data.local.ExchangesLocalDataSource
import com.numtory.application.features.cryptoExchange.data.local.ExchangesLocalDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val carDataModule = module {
    single<CarRemoteDataSource> { CarRemoteDataSourceImpl(get(), get()) }
    single<CarRepository> { CarRepositoryImpl(get(), get(qualifier = named("IO"))) }


    factory<ExchangesLocalDataSource> {
        ExchangesLocalDataSourceImpl(get())
    }

}