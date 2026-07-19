package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAsacoinePriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String, quote: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getAsacoinePrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    // Only the Toman side is of interest; the same coin is also listed against USDT.
                    val pair = response.result?.pairs
                        ?.firstOrNull { it.name.equals("$symbol-$quote", ignoreCase = true) }

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = symbol,
                            buyPrice = pair?.ask,
                            sellPrice = pair?.bid,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.asacoine }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.asacoine,
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
