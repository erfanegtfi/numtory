package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform


class GetRamzinexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(
        fromId: String,
        toId: String,
    ): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()


        val flow1 = marketRepository.getRamzinexSwapPrice(fromId, toId, true)
        val flow2 = marketRepository.getRamzinexSwapPrice(toId, fromId, false)
//        val flow3 = marketRepository.getTabdealMarket()

        return combine(flow1, flow2) { buy, sell ->
            Pair(buy, sell)
        }.transform { (buyPriceResponse, sellPriceResponse) ->

            val swapPrice = MarketPrice(
                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.ramzinex }
                    ?: ExchangeInfo(
                        exchange = Exchanges.ramzinex,
                        active = true,
                        display = true
                    ),
                lastRefresh = System.currentTimeMillis(),
            )

            when (buyPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.buyPrice = ((buyPriceResponse.result?.data?.fromAmount
                        ?: "0").toFloat() / 10).toString()
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyPriceResponse.error))
                }
            }

            when (sellPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.sellPrice = ((sellPriceResponse.result?.data?.toAmount
                        ?: "0").toFloat() / 10).toString()
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellPriceResponse.error))
                }
            }
            emit(ApiCallResult.Success(swapPrice))
//            when (marketResponse) {
//                is ApiCallResult.Success -> {
//                    val marketPrice = MarketPrice(
//                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.tabdealMarket }
//                            ?: ExchangeInfo(
//                                exchange = Exchanges.tabdealMarket,
//                                active = true,
//                                display = true
//                            ),
//                        marketPrice = marketResponse.result?.get(toCurrency)
//                            ?.get(fromCurrency)?.price,
//                        lastRefresh = System.currentTimeMillis()
//                    )
//
//                    emit(ApiCallResult.Success(marketPrice))
//                }
//
//                is ApiCallResult.Failure -> {
//                    emit(ApiCallResult.Failure(marketResponse.error))
//
//                }
//            }


        }

    }
}

