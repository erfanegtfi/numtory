package com.numtory.application.features.car.presenter.di

import com.numtory.application.features.car.domain.usecase.FilterCarPricesUseCase
import com.numtory.application.features.car.domain.usecase.GetCarPricesUseCase
import com.numtory.application.features.car.presenter.CarPriceViewModel
import com.numtory.application.features.seke.data.repositories.SekeRepository
import com.numtory.application.features.seke.domain.usecase.GetSekePricesUseCase
import com.numtory.application.features.seke.presenter.SekePriceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.*
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val carPresenterModule = module {
    factory { GetCarPricesUseCase(get()) }
    factory { FilterCarPricesUseCase() }


    viewModel {
        CarPriceViewModel(get(), get())
    }
}