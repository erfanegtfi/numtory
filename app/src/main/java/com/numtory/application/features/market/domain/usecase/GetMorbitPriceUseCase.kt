package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMorbitPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String, quote: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getMorbitPrice(symbol).map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    // The endpoint answers with one entry per quote currency ("irt" and "usdt").
                    val asset = response.result?.data
                        ?.firstOrNull { it.unit?.lowercase() == quote.lowercase() }

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = symbol,
                            // Morbit names its prices from the exchange's side, so they are swapped here.
                            buyPrice = asset?.sellPrice,
                            sellPrice = asset?.buyPrice,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.morbit }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.morbit,
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
