package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import kotlin.text.lowercase

class GetNobitexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String, quote: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flow1 = marketRepository.getNobitex("$base-$quote")
        val flow2 = marketRepository.getNobitexMarket()

        return combine(flow1, flow2) { swap, market ->
            Pair(swap, market)
        }.transform { (swapResponse, marketResponse) ->
            when (swapResponse) {
                is ApiCallResult.Success -> {
                    val swapPrice = MarketPrice(
                        symbol = base,
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.nobitex } ?: ExchangeInfo(
                            exchange = Exchanges.nobitex,
                            active = true,
                            display = true,
                            hasMarket = true,
                            isMarket = false,
                        ),
                        buyPrice = ((swapResponse.result?.buy ?: "0").toDouble() / 10).toString(),
                        sellPrice = ((swapResponse.result?.sell ?: "0").toDouble() / 10).toString(),
                        lastRefresh = System.currentTimeMillis()
                    )
                    emit(ApiCallResult.Success(swapPrice))
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(swapResponse.error))
                }
            }

            when (marketResponse) {
                is ApiCallResult.Success -> {

                    val asset = marketResponse.result["$base$quote"]

                    if (asset != null) {
                        val marketPrice = MarketPrice(
                            symbol = asset.symbol,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.nobitexMarket }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.nobitexMarket,
                                    active = true,
                                    display = true,
                                    hasMarket = true,
                                    isMarket = true,
                                ),
                            marketPrice = ((asset.price
                                ?: "0").toDouble() / 10).toString(),
                            lastRefresh = System.currentTimeMillis()
                        )
                        emit(ApiCallResult.Success(marketPrice))
                    }
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(marketResponse.error))

                }
            }
        }

    }
}

