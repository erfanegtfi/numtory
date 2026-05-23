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

class GetTlynPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getTlynPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    if (response.result.prices?.isNotEmpty() == true && response.result.prices.first()
                            ?.isNotEmpty() == true
                    )
                        ApiCallResult.Success(
                            GoldMarketPrice(
                                buyPrice = ((response.result.prices.first()?.first()?.price?.buy
                                    ?: 0) / 10).toString(),
                                sellPrice = ((response.result.prices.first()?.first()?.price?.sell
                                    ?: 0) / 10).toString(),
                                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.taline }
                                    ?: GoldExchangeInfo(
                                        exchange = GoldExchanges.taline,
                                        active = true,
                                        display = true
                                    ),
                                lastRefresh = System.currentTimeMillis()
                            )
                        )
                    else ApiCallResult.Failure(GeneralError().withErrorMessage())
                }

                is ApiCallResult.Failure -> {
                    ApiCallResult.Failure(response.error)
                }
            }
        }
    }
}

