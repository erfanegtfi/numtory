package com.numtory.application.features.main.di

import com.numtory.application.features.main.SettingsViewModel
import com.numtory.application.features.setting.data.repositories.SettingsRepository
import com.numtory.application.features.setting.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val settingsPresenterModule = module {
    factory { GetSettingsUseCase(get<SettingsRepository>()) }


//    viewModel<MarketsViewModel>{ MarketsViewModel(get<GetBitPinPriceUseCase>()) }
    viewModel {
        SettingsViewModel(
            get(),
            get(),

            )
    }
}