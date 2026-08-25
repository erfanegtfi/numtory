package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

class GetArzyptoPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(
        currency: String,
        base: String,
    ): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flow1 = marketRepository.getArzyptoSwapPrice(currency, "BUY", base)
        val flow2 = marketRepository.getArzyptoSwapPrice(currency, "SELL", base)

        return combine(flow1, flow2) { swap, market ->
            Pair(swap, market)
        }.transform { (buyResponse, sellResponse) ->

            val swapPrice = MarketPrice(
                symbol = base,
                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.arzypto }
                    ?: ExchangeInfo(
                        exchange = Exchanges.arzypto,
                        active = true,
                        display = true
                    ),

                lastRefresh = System.currentTimeMillis()
            )

            when (buyResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.buyPrice = ((buyResponse.result.data?.symbolTomanPrice
                        ?: "0"))

                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyResponse.error))
                }
            }

            when (sellResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.sellPrice = ((sellResponse.result.data?.symbolTomanPrice
                        ?: "0"))

                    emit(ApiCallResult.Success(swapPrice))
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellResponse.error))

                }
            }
        }

    }
}

