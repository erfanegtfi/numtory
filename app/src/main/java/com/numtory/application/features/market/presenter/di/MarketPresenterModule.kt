package com.numtory.application.features.market.presenter.di

import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.usecase.FilterMarketUseCase
import com.numtory.application.features.market.domain.usecase.GetAbanTetherPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetAppExchangesUseCase
import com.numtory.application.features.market.domain.usecase.GetArz3PriceUseCase
import com.numtory.application.features.market.domain.usecase.GetArzinjaPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetArzplusPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetArzyptoPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBit24PriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBitPinPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBitbargPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetCoinkadePriceUseCase
import com.numtory.application.features.market.domain.usecase.GetEterexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetExonyxPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetMarketAvgUseCase
import com.numtory.application.features.market.domain.usecase.GetNobitexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetPingiPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetPoolenoPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetRamzinexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetSarafPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetSarmayexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTabtealPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTetherLandPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTwoxPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetUbitexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetWallexPriceUseCase
import com.numtory.application.features.market.domain.usecase.RemoveInvalidExchangeUseCase
import com.numtory.application.features.market.domain.usecase.RemoveOutOfRangeExchangeUseCase
import com.numtory.application.features.market.domain.usecase.SortMarketUseCase
import com.numtory.application.features.market.presenter.MarketsViewModel
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val marketPresenterModule = module {
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
    factory { SortMarketUseCase() }
    factory { FilterMarketUseCase() }
    factory { RemoveOutOfRangeExchangeUseCase() }
    factory { GetMarketAvgUseCase() }
    factory { GetAppExchangesUseCase(get()) }
    factory { RemoveInvalidExchangeUseCase() }

//    viewModel<MarketsViewModel>{ MarketsViewModel(get<GetBitPinPriceUseCase>()) }
    viewModel {
        MarketsViewModel(
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