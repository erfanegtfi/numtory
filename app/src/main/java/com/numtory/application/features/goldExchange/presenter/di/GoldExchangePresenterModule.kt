package com.numtory.application.features.goldExchange.presenter.di

import com.numtory.application.features.goldExchange.data.local.GoldExchangesLocalDataSourceImpl
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepository
import com.numtory.application.features.goldExchange.domain.usecase.FilterGoldMarketUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldExchangeCatalogUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldPriceFlowsUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetSelectableGoldExchangesUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetUserGoldExchangesUseCase
import com.numtory.application.features.goldExchange.domain.usecase.MergeGoldPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.PrepareGoldMarketListUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetDaricPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetDigikalaPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetEcoGoldPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGeramiPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldBestPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldikaPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetHamrahGoldPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetMelliGoldPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetMilliPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetNoghreseaPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetTalaseaPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetTechnoGoldPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetTlynPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetWallGoldPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetZarafzaPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetZarminexPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.RemoveInvalidGoldExchangeUseCase
import com.numtory.application.features.goldExchange.domain.usecase.RemoveOutOfRangeGoldExchangeUseCase
import com.numtory.application.features.goldExchange.domain.usecase.SortGoldMarketUseCase
import com.numtory.application.features.goldExchange.presenter.GoldExchangesViewModel
import com.numtory.application.features.cryptoExchange.domain.usecase.GetAppExchangesUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val goldExchangePresenterModule = module {
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
    factory { GetGeramiPriceUseCase(get<GoldMarketRepository>()) }
    factory { GetZarafzaPriceUseCase(get<GoldMarketRepository>()) }
    factory { SortGoldMarketUseCase() }
    factory { FilterGoldMarketUseCase() }
    factory { GetGoldExchangeCatalogUseCase(get()) }
    factory { RemoveOutOfRangeGoldExchangeUseCase() }
    factory { GetGoldMarketAvgUseCase() }
    factory { GetGoldBestPriceUseCase() }
    factory { GoldExchangesLocalDataSourceImpl(get()) }
    factory { GetAppExchangesUseCase(get()) }
    factory { RemoveInvalidGoldExchangeUseCase() }
    factory { GetUserGoldExchangesUseCase(get()) }
    factory { GetSelectableGoldExchangesUseCase(get()) }
    factory { MergeGoldPriceUseCase(get(), get(), get()) }
    factory { PrepareGoldMarketListUseCase(get(), get()) }
    factory {
        GetGoldPriceFlowsUseCase(
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

    viewModel {
        GoldExchangesViewModel(
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