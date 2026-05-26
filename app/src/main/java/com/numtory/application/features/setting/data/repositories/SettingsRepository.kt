package com.numtory.application.features.setting.data.repositories

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.setting.data.dataSource.SettingsRemoteDataSource
import com.numtory.application.features.setting.data.local.SettingsLocalDataSource
import com.numtory.application.features.setting.domain.entities.AppSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface SettingsRepository {
    fun getAppSettings(): Flow<ApiCallResult<AppSettings>>
    fun getAppSettingsLocal(): AppSettings?

}

class SettingsRepositoryImpl(
    private val settingsRemoteDataSource: SettingsRemoteDataSource,
    private val settingsLocalDataSource: SettingsLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : SettingsRepository {

    override fun getAppSettings(): Flow<ApiCallResult<AppSettings>> = flow {
        val response = getResult {
            settingsRemoteDataSource.getAppSettings()
        }
        if (response is ApiCallResult.Success) {
            settingsLocalDataSource.saveAppSettings(response.result)
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getAppSettingsLocal(): AppSettings? {
        return settingsLocalDataSource.getAppSettings()?.toEntity()
    }
}