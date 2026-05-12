package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTetherLandPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getTetherLand().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset =
                        response.result.firstOrNull { item -> item.symbol?.lowercase() == "usdt" }

                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = asset?.tomanAmount,
                            sellPrice = asset?.tomanAmount,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.tetherland }?: ExchangeInfo(
                                exchange = Exchanges.tetherland,
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

