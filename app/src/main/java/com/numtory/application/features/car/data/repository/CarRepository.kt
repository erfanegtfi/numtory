package com.numtory.application.features.car.data.repository

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.car.data.dataSource.CarRemoteDataSource
import com.numtory.application.features.car.data.model.CarCategoryDataModel
import com.numtory.application.features.car.domain.entities.CarCategory
import com.numtory.application.features.setting.data.dataSource.SettingsRemoteDataSource
import com.numtory.application.features.setting.data.local.SettingsLocalDataSource
import com.numtory.application.features.setting.domain.entities.AppSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface CarRepository {
    fun getCarPrices(): Flow<ApiCallResult<List<CarCategory>?>>
}

class CarRepositoryImpl(
    private val carRemoteDataSource: CarRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : CarRepository {

    override fun getCarPrices(): Flow<ApiCallResult<List<CarCategory>?>> = flow {
        val response = getResult {
            carRemoteDataSource.getCarPrices()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

}