package com.numtory.application.features.main.di

import com.numtory.application.common.getAppVersion
import com.numtory.application.features.main.SettingsViewModel
import com.numtory.application.features.menu.MenuViewModel
import com.numtory.application.features.setting.data.repositories.SettingsRepository
import com.numtory.application.features.setting.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val settingsPresenterModule = module {
    factory { GetSettingsUseCase(get<SettingsRepository>()) }

    single(named("appVersion")) {
        getAppVersion(get())
    }
//    viewModel<MarketsViewModel>{ MarketsViewModel(get<GetBitPinPriceUseCase>()) }
    viewModel {
        SettingsViewModel(
            get(),
            appVersion = get(named("appVersion")),
            )
    }
    viewModel {
        MenuViewModel(
            get(),
        )
    }
}