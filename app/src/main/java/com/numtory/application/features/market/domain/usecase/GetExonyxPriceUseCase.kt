package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExonyxPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()
        return marketRepository.getExonyxSwapPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset =
                        response.result?.firstOrNull { item -> item.iso?.lowercase() == symbol.lowercase() }

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = symbol,
                            buyPrice = ((asset?.priceBuy ?: "0").toDouble() / 10).toString(),
                            sellPrice = ((asset?.priceSell ?: "0").toDouble() / 10).toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.exonyx }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.exonyx,
                                    active = true,
                                    display = true
                                ),
                            lastRefresh = System.currentTimeMillis(),
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

