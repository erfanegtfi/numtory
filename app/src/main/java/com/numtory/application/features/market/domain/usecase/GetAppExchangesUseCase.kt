package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetAppExchangesUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(): Flow<ApiCallResult<List<ExchangeStatus>?>> = flow {
        emit(ApiCallResult.Success(marketRepository.getSavedExchanges()))
        emitAll(marketRepository.getExchanges())
    }
}

