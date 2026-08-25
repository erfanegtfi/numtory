package com.numtory.application.features.cryptoMarket.data.repositories

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoMarket.data.dataSource.CryptoGlobalMarketRemoteDataSource
import com.numtory.application.features.cryptoMarket.data.local.CryptoGlobalExchangesLocalDataSource
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface CryptoGlobalMarketRepository {
    fun getCryptoPrices(): Flow<ApiCallResult<List<CryptoMarketPrice>>>

}

class CryptoGlobalMarketRepositoryImpl(
    private val marketRemoteDataSource: CryptoGlobalMarketRemoteDataSource,
    private val exchangesLocalDataSource: CryptoGlobalExchangesLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : CryptoGlobalMarketRepository {

    override fun getCryptoPrices(): Flow<ApiCallResult<List<CryptoMarketPrice>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getCryptoGlobalMarketPrices(1)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.map { it.toEntity() }))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

}