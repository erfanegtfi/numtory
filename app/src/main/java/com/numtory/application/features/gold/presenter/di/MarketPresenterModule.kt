package com.numtory.application.features.gold.presenter.di

import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSourceImpl
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.usecase.FilterGoldMarketUseCase
import com.numtory.application.features.gold.domain.usecase.GetAppGoldExchangesUseCase
import com.numtory.application.features.gold.domain.usecase.GetDaricPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetDigikalaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetEcoGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldikaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetHamrahGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetMelliGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetMilliPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetNoghreseaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTalaseaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTechnoGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTlynPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetWallGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetZarminexPriceUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveInvalidGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveOutOfRangeGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.SortGoldMarketUseCase
import com.numtory.application.features.gold.presenter.GoldMarketsViewModel
import com.numtory.application.features.market.domain.usecase.GetAppExchangesUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val goldMarketPresenterModule = module {
    factory { GetDigikalaPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetGoldikaPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetTlynPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetHamrahGoldPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetMelliGoldPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetTalaseaPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetWallGoldPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetMilliPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetTechnoGoldPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetDaricPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetEcoGoldPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetZarminexPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetNoghreseaPriceUseCase(get<GoldMarketRepository>()) }
    factory { SortGoldMarketUseCase() }
    factory { FilterGoldMarketUseCase() }
    factory { GetAppGoldExchangesUseCase(get()) }
    factory { RemoveOutOfRangeGoldExchangeUseCase() }
    factory { GetGoldMarketAvgUseCase() }
    factory { GoldExchangesLocalDataSourceImpl(get()) }
    factory { GetAppExchangesUseCase(get()) }
    factory { RemoveInvalidGoldExchangeUseCase() }

    viewModel {
        GoldMarketsViewModel(
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
        )
    }
}