package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetAppExchangesUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(): Flow<ApiCallResult<List<ExchangeInfo>?>> = flow {
        emit(ApiCallResult.Success(marketRepository.getSavedExchangesInfo()))
        emitAll(marketRepository.getExchanges())
    }
}

