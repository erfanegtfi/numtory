package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetGoldExchangeCatalogUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<List<GoldExchangeInfo>?>> = flow {
        emit(ApiCallResult.Success(marketRepository.getSavedExchangesInfo()))
        emitAll(marketRepository.getExchanges())
    }
}
