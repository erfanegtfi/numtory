package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

class GetTwoxPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(fromCurrency: String, base: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flow1 = marketRepository.getTwoxPrice(fromCurrency, base)
        val flow2 = marketRepository.getTwoxPrice(base, fromCurrency)


        return combine(flow1, flow2) { buy, sell ->
            Pair(buy, sell)
        }.transform { (buyPriceResponse, sellPriceResponse) ->

            val swapPrice = MarketPrice(
                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.twox }?: ExchangeInfo(
                    exchange = Exchanges.twox,
                    active = true,
                    display = true
                ),
                lastRefresh = System.currentTimeMillis()
            )

            when (buyPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.buyPrice = buyPriceResponse.result.displayFee
                    swapPrice.symbol = base
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyPriceResponse.error))
                }
            }

            when (sellPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.sellPrice = sellPriceResponse.result.displayFee
                    swapPrice.symbol = base
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellPriceResponse.error))
                }
            }
            emit(ApiCallResult.Success(swapPrice))


        }

    }
}

