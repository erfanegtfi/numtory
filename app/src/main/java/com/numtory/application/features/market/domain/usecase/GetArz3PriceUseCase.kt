package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetArz3PriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getArz3Price().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset =
                        response.result?.firstOrNull { item -> item.symbol?.lowercase() == "usdt" }

                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = asset?.price?.buy,
                            sellPrice = asset?.price?.sell,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.arz3 } ?: ExchangeInfo(
                                exchange = Exchanges.arz3,
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

