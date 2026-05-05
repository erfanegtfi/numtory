package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

class GetPoolenoPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(baseCurrency: String, quoteCurrency: String): Flow<ApiCallResult<MarketPrice>> {

        val flow1 = marketRepository.getPoolenoPrice(baseCurrency, quoteCurrency, true)
        val flow2 = marketRepository.getPoolenoPrice(baseCurrency, quoteCurrency, false)


        return combine(flow1, flow2) { buy, sell ->
            Pair(buy, sell)
        }.transform { (buyPriceResponse, sellPriceResponse) ->

            val swapPrice = MarketPrice(
                exchange = Exchanges.pooleno,
                lastRefresh = System.currentTimeMillis()
            )

            when (buyPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.buyPrice = buyPriceResponse.result.payload?.rate
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyPriceResponse.error))
                }
            }

            when (sellPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.sellPrice = sellPriceResponse.result.payload?.rate
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellPriceResponse.error))
                }
            }
            emit(ApiCallResult.Success(swapPrice))


        }

    }
}

