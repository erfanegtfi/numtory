package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.data.utils.GeneralError
import com.numtory.application.data.utils.withErrorMessage
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMilliPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getMilliPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    ApiCallResult.Success(
                        GoldMarketPrice(
                            buyPrice = ((response.result.data?.price ?: 0) * 100).toString(),
                            sellPrice = ((response.result.data?.price ?: 0) * 100).toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.milli }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.milli,
                                    active = true,
                                    display = true
                                ),
                            lastRefresh = System.currentTimeMillis()
                        )
                    )
                }

                is ApiCallResult.Failure -> {
                    ApiCallResult.Failure(response.error)
                }
            }
        }
    }
}

