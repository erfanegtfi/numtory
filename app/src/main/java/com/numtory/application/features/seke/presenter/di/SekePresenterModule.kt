package com.numtory.application.features.seke.presenter.di

import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepository
import com.numtory.application.features.cryptoMarket.domain.usecase.GetCryptoGlobalMarketPricesUseCase
import com.numtory.application.features.cryptoMarket.presenter.CryptoGlobalMarketPriceViewModel
import com.numtory.application.features.seke.data.repositories.SekeRepository
import com.numtory.application.features.seke.domain.usecase.GetSekePricesUseCase
import com.numtory.application.features.seke.presenter.SekePriceViewModel
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val sekePresenterModule = module {
    factory { GetSekePricesUseCase(get<SekeRepository>()) }


//    viewModel<MarketsViewModel>{ MarketsViewModel(get<GetBitPinPriceUseCase>()) }
    viewModel {
        SekePriceViewModel(
            get(),

            )
    }
}