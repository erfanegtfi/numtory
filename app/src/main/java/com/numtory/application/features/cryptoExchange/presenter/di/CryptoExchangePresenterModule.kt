package com.numtory.application.features.cryptoExchange.presenter.di

import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.usecase.FilterMarketUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetAbanTetherPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetAppExchangesUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetArz3PriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetArzinjaPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetArzplusPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetArzyptoPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetAsacoinePriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetBestPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetBit24PriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetBitPinPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetBitbargPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetCoinkadePriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetEterexPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetExonyxPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetMarketAvgUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetMorbitPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetNobitexPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetPingiPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetPoolenoPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetRamzinexPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetSarafPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetSarmayexPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetTabtealPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetTetherLandPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetTwoxPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetUbitexPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.GetWallexPriceUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.RemoveInvalidExchangeUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.RemoveOutOfRangeExchangeUseCase
import com.numtory.application.features.cryptoExchange.domain.usecase.SortMarketUseCase
import com.numtory.application.features.cryptoExchange.presenter.CryptoExchangeViewModel
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val cryptoExchangePresenterModule = module {
    factory { GetBitPinPriceUseCase(get<MarketRepository>()) }
    factory { GetTetherLandPriceUseCase(get<MarketRepository>()) }
    factory { GetAbanTetherPriceUseCase(get<MarketRepository>()) }
    factory { GetNobitexPriceUseCase(get<MarketRepository>()) }
    factory { GetTabtealPriceUseCase(get<MarketRepository>()) }
    factory { GetBit24PriceUseCase(get<MarketRepository>()) }
    factory { GetArzplusPriceUseCase(get<MarketRepository>()) }
    factory { GetTwoxPriceUseCase(get<MarketRepository>()) }
    factory { GetCoinkadePriceUseCase(get<MarketRepository>()) }
    factory { GetPoolenoPriceUseCase(get<MarketRepository>()) }
    factory { GetEterexPriceUseCase(get<MarketRepository>()) }
    factory { GetSarmayexPriceUseCase(get<MarketRepository>()) }
    factory { GetPingiPriceUseCase(get<MarketRepository>()) }
    factory { GetWallexPriceUseCase(get<MarketRepository>()) }
    factory { GetSarafPriceUseCase(get<MarketRepository>()) }
    factory { GetArz3PriceUseCase(get<MarketRepository>()) }
    factory { GetUbitexPriceUseCase(get<MarketRepository>()) }
    factory { GetRamzinexPriceUseCase(get<MarketRepository>()) }
    factory { GetArzinjaPriceUseCase(get<MarketRepository>()) }
    factory { GetArzyptoPriceUseCase(get<MarketRepository>()) }
    factory { GetExonyxPriceUseCase(get<MarketRepository>()) }
    factory { GetBitbargPriceUseCase(get<MarketRepository>()) }
    factory { GetMorbitPriceUseCase(get<MarketRepository>()) }
    factory { GetAsacoinePriceUseCase(get<MarketRepository>()) }
    factory { SortMarketUseCase() }
    factory { FilterMarketUseCase() }
    factory { RemoveOutOfRangeExchangeUseCase() }
    factory { GetMarketAvgUseCase() }
    factory { GetBestPriceUseCase() }
    factory { GetAppExchangesUseCase(get()) }
    factory { RemoveInvalidExchangeUseCase() }

//    viewModel<MarketsViewModel>{ MarketsViewModel(get<GetBitPinPriceUseCase>()) }
    viewModel {
        CryptoExchangeViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}