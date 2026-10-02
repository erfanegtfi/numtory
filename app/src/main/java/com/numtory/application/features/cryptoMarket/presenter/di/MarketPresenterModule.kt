package com.numtory.application.features.cryptoMarket.presenter.di

import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepository
import com.numtory.application.features.cryptoMarket.domain.usecase.FilterCryptoPricesUseCase
import com.numtory.application.features.cryptoMarket.domain.usecase.GetCryptoGlobalMarketPricesUseCase
import com.numtory.application.features.cryptoMarket.presenter.CryptoGlobalMarketPriceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.*
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val cryptoGlobalMarketPresenterModule = module {
    factory { GetCryptoGlobalMarketPricesUseCase(get<CryptoGlobalMarketRepository>()) }
    factory { FilterCryptoPricesUseCase() }


    viewModel {
        CryptoGlobalMarketPriceViewModel(
            get(),
            get(),
            )
    }
}