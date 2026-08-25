package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTetherLandPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getTetherLand().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset =
                        response.result.firstOrNull { item -> item.symbol?.lowercase() == symbol.lowercase() }

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = asset?.symbol,
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

