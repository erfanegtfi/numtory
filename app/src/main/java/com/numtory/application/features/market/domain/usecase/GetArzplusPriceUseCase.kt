package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

class GetArzplusPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(fromCurrency: String, toCurrency: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flow1 = marketRepository.getArzplusSwapPrice(fromCurrency, toCurrency)
        val flow2 = marketRepository.getArzplusSwapPrice(toCurrency, fromCurrency)
        val flow3 = marketRepository.getArzplusMarketPrice()

        return combine(flow1, flow2, flow3) { buy, sell, market ->
            Triple(buy, sell, market)
        }.transform { (buyResponse, sellResponse, marketResponse) ->

            val swapPrice = MarketPrice(
                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.arzplus } ?: ExchangeInfo(
                    exchange = Exchanges.arzplus,
                    active = true,
                    display = true
                ),
                lastRefresh = System.currentTimeMillis()

            )

            when (buyResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.buyPrice = ((buyResponse.result.price ?: "0").toInt()).toString()
                    emit(ApiCallResult.Success(swapPrice))
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyResponse.error))
                }
            }

            when (sellResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.sellPrice = ((sellResponse.result.price ?: "0").toInt()).toString()
                    emit(ApiCallResult.Success(swapPrice))
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellResponse.error))
                }
            }

            when (marketResponse) {
                is ApiCallResult.Success -> {
                    val marketPrice = MarketPrice(
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.arzplusMarket } ?: ExchangeInfo(
                            exchange = Exchanges.arzplusMarket,
                            active = true,
                            display = true
                        ),
                        marketPrice = (
                                (marketResponse.result.firstOrNull { it.symbol?.lowercase() == toCurrency.lowercase() }?.priceIrt
                                    ?: "0").toInt()).toString(),
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

