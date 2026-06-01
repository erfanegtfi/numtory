package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

//sealed class MarketEvent {
//    data class Buy(val data: ApiCallResult<MarketPrice>) : MarketEvent()
//    data class Sell(val data: ApiCallResult<MarketPrice>) : MarketEvent()
//    data class Market(val data: ApiCallResult<MarketPrice>) : MarketEvent()
//}

class GetTabtealPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(
        fromCurrency: String,
        base: String,
    ): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()


//        val flow1 = marketRepository.getTabdeal(fromCurrency, toCurrency)
//            .map { MarketEvent.Buy(it) }
//        val flow2 = marketRepository.getTabdeal(toCurrency, fromCurrency)
//            .map { MarketEvent.Sell(it) }
//        val flow3 = marketRepository.getTabdealMarket()
//            .map { MarketEvent.Market(it) }
        val flow1 = marketRepository.getTabdeal(fromCurrency, base)
        val flow2 = marketRepository.getTabdeal(base, fromCurrency)
        val flow3 = marketRepository.getTabdealMarket()

        return combine(flow1, flow2, flow3) { buy, sell, market ->
            Triple(buy, sell, market)
        }.transform { (buyPriceResponse, sellPriceResponse, marketResponse) ->

            val swapPrice = MarketPrice(
                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.tabdeal }
                    ?: ExchangeInfo(
                        exchange = Exchanges.tabdeal,
                        active = true,
                        display = true
                    ),
                lastRefresh = System.currentTimeMillis(),
            )


            when (buyPriceResponse) {
                is ApiCallResult.Success -> {
                    val asset = buyPriceResponse.result.fromAmountData?.get(0)

                    if (buyPriceResponse.result.fromAmountData?.isNotEmpty() == true)
                        swapPrice.buyPrice = asset?.buyPrice
                    swapPrice.symbol = base
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyPriceResponse.error))
                }
            }

            when (sellPriceResponse) {
                is ApiCallResult.Success -> {
                    val asset = sellPriceResponse.result.fromAmountData?.get(0)

                    if (sellPriceResponse.result.fromAmountData?.isNotEmpty() == true)
                        swapPrice.sellPrice =
                            asset?.sellPrice

                    swapPrice.symbol = base
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellPriceResponse.error))
                }
            }
            emit(ApiCallResult.Success(swapPrice))
            when (marketResponse) {
                is ApiCallResult.Success -> {

                    val asset = marketResponse.result?.get(base)?.get(fromCurrency)

                    val marketPrice = MarketPrice(
                        symbol = base,
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.tabdealMarket }
                            ?: ExchangeInfo(
                                exchange = Exchanges.tabdealMarket,
                                active = true,
                                display = true
                            ),
                        marketPrice = asset?.price,
                        lastRefresh = System.currentTimeMillis()
                    )

                    emit(ApiCallResult.Success(marketPrice))
//                        marketResponse.result.firstOrNull { it.symbol?.lowercase() == marketSymbol.lowercase() }?.price
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(marketResponse.error))

                }
            }


        }

    }
}

