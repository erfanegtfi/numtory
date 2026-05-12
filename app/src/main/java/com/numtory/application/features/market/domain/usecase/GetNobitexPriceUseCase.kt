package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

class GetNobitexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String, marketSymbol: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flow1 = marketRepository.getNobitex(symbol)
        val flow2 = marketRepository.getNobitexMarket()

        return combine(flow1, flow2) { swap, market ->
            Pair(swap, market)
        }.transform { (swapResponse, marketResponse) ->
            when (swapResponse) {
                is ApiCallResult.Success -> {
                    val swapPrice = MarketPrice(
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.nobitex } ?: ExchangeInfo(
                            exchange = Exchanges.nobitex,
                            active = true,
                            display = true
                        ),
                        buyPrice = ((swapResponse.result?.buy ?: "0").toFloat() / 10).toString(),
                        sellPrice = ((swapResponse.result?.sell ?: "0").toFloat() / 10).toString(),
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
                    val marketPrice = MarketPrice(
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.nobitexMarket } ?: ExchangeInfo(
                            exchange = Exchanges.nobitexMarket,
                            active = true,
                            display = true
                        ),
                        marketPrice = ((marketResponse.result[marketSymbol]?.price
                            ?: "0").toFloat() / 10).toString(),
                        lastRefresh = System.currentTimeMillis()
                    )
                    emit(ApiCallResult.Success(marketPrice))
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(marketResponse.error))

                }
            }
        }

    }
}

