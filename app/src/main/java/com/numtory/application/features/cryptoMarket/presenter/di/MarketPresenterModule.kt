package com.numtory.application.features.cryptoMarket.presenter.di

import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepository
import com.numtory.application.features.cryptoMarket.domain.usecase.GetCryptoGlobalMarketPricesUseCase
import com.numtory.application.features.cryptoMarket.presenter.CryptoGlobalMarketPriceViewModel
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val cryptoGlobalMarketPresenterModule = module {
    factory { GetCryptoGlobalMarketPricesUseCase(get<CryptoGlobalMarketRepository>()) }


//    viewModel<MarketsViewModel>{ MarketsViewModel(get<GetBitPinPriceUseCase>()) }
    viewModel {
        CryptoGlobalMarketPriceViewModel(
            get(),

            )
    }
}