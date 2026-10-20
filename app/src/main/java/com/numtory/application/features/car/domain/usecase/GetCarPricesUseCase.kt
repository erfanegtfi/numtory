package com.numtory.application.features.car.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.car.data.repository.CarRepository
import com.numtory.application.features.car.domain.entities.CarCategory
import kotlinx.coroutines.flow.Flow

class GetCarPricesUseCase constructor(
    private val carRepository: CarRepository,
) {

    fun action(): Flow<ApiCallResult<List<CarCategory>?>> {
        return carRepository.getCarPrices()
    }
}

