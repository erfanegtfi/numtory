package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class GetArzinjaPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(baseCurrency: String, quoteCurrency: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flow1 = marketRepository.getArzinjaPrice(baseCurrency, "otc")
        val flow2 = marketRepository.getArzinjaPrice(baseCurrency, "p2p")

        return combine(flow1, flow2) { swap, market ->
            Pair(swap, market)
        }.transform { (swapResponse, marketResponse) ->
            when (swapResponse) {
                is ApiCallResult.Success -> {
                    if (swapResponse.result?.isNotEmpty() == true) {
                        val swapPrice = MarketPrice(
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.arzinja }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.arzinja,
                                    active = true,
                                    display = true
                                ),
                            buyPrice = ((swapResponse.result.first()["$baseCurrency$quoteCurrency"]?.stats?.lastPrice
                                ?: "0").toInt()).toString(),
                            sellPrice = ((swapResponse.result.first()["$baseCurrency$quoteCurrency"]?.stats?.lastPrice
                                ?: "0").toInt()).toString(),
                            lastRefresh = System.currentTimeMillis()
                        )
                        emit(ApiCallResult.Success(swapPrice))
                    }
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(swapResponse.error))
                }
            }

            when (marketResponse) {
                is ApiCallResult.Success -> {
                    if (marketResponse.result?.isNotEmpty() == true) {
                        val marketPrice = MarketPrice(
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.arzinjaMarket }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.arzinjaMarket,
                                    active = true,
                                    display = true
                                ),
                            buyPrice = ((marketResponse.result.first()["$baseCurrency$quoteCurrency"]?.stats?.askPrice
                                ?: "0").toInt()).toString(),
                            sellPrice = ((marketResponse.result.first()["$baseCurrency$quoteCurrency"]?.stats?.bidPrice
                                ?: "0").toInt()).toString(),
//                        marketPrice = ((marketResponse.result?.get("$baseCurrency$quoteCurrency")?.stats?.lastPrice
//                            ?: "0").toInt()).toString(),
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
