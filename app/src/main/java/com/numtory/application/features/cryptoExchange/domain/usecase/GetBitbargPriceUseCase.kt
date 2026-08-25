package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.firstOrNull

class GetBitbargPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getBitbargPrice(base = "toman", symbol = symbol).map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    val asset =
                        response.result.result?.items?.firstOrNull { item -> item.slug?.lowercase() == symbol.lowercase() }


                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = symbol,
                            buyPrice = asset?.price,
                            sellPrice = asset?.price,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.bitbarg }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.bitbarg,
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

