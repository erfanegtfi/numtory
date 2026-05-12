package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

class GetBit24PriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(fromCurrency: String, toCurrency: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flow1 = marketRepository.getBit24SwapPrice(fromCurrency, toCurrency)
        val flow2 = marketRepository.getBit24MarketPrice()

        return combine(flow1, flow2) { swap, market ->
            Pair(swap, market)
        }.transform { (swapResponse, marketResponse) ->
            when (swapResponse) {
                is ApiCallResult.Success -> {
                    val swapPrice = MarketPrice(
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.bit24 } ?: ExchangeInfo(
                            exchange = Exchanges.bit24,
                            active = true,
                            display = true
                        ),
                        buyPrice = ((swapResponse.result.data.metas.buyPrice
                            ?: "0").toInt() ).toString(),
                        sellPrice = ((swapResponse.result.data.metas.sellPrice
                            ?: "0").toInt() ).toString(),
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
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.bit24Market } ?: ExchangeInfo(
                            exchange = Exchanges.bit24Market,
                            active = true,
                            display = true
                        ),
                        marketPrice = (
                                (marketResponse.result.firstOrNull { it.symbol?.lowercase() == toCurrency.lowercase() }?.market?.get(
                                    fromCurrency.lowercase()
                                )?.price?:"0")),
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

