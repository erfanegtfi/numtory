package com.numtory.application.features.setting.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.setting.data.repositories.SettingsRepository
import com.numtory.application.features.setting.domain.entities.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetSettingsUseCase constructor(
    private val settingsRepository: SettingsRepository,
) {

    fun action(): Flow<ApiCallResult<AppSettings?>> = flow {
        emit(ApiCallResult.Success(settingsRepository.getAppSettingsLocal()))
        emitAll(settingsRepository.getAppSettings())
    }
}

