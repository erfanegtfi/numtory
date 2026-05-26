package com.numtory.application.features.setting.data.repositories

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.dataSource.MarketRemoteDataSource
import com.numtory.application.features.market.data.local.ExchangesLocalDataSource
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface MarketRepository {
    fun getExchanges(): Flow<ApiCallResult<List<ExchangeInfo>>>

}

class MarketRepositoryImpl(
    private val marketRemoteDataSource: MarketRemoteDataSource,
    private val exchangesLocalDataSource: ExchangesLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : MarketRepository {

    override fun getExchanges(): Flow<ApiCallResult<List<ExchangeInfo>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getExchanges()
        }
        if (response is ApiCallResult.Success) {
            exchangesLocalDataSource.saveExchangesInfo(response.result)
            emit(ApiCallResult.Success(response.result.map { it.toEntity() }))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)


}