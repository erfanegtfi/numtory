package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBitWanaPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String, quote: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getBitWanaPrice("${symbol}_$quote").map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = symbol,
                            buyPrice = response.result.content?.sellPrice,
                            sellPrice = response.result.content?.buyPrice,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.bitwana }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.bitwana,
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
