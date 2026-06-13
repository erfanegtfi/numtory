package com.numtory.application.features.seke.data.repositories

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.seke.data.dataSource.SekeRemoteDataSource
import com.numtory.application.features.seke.domain.entities.SekePrice
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface SekeRepository {
    fun getSekePrices(): Flow<ApiCallResult<List<SekePrice>>>

}

class SekeRepositoryImpl(
    private val marketRemoteDataSource: SekeRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : SekeRepository {

    override fun getSekePrices(): Flow<ApiCallResult<List<SekePrice>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getSekePrices()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.map { it.toEntity() }))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

}