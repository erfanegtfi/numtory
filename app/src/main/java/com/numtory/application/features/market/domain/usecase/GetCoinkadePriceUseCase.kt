package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.data.utils.GeneralError
import com.numtory.application.data.utils.withErrorMessage
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCoinkadePriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getCoinkadePrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    if (symbol.lowercase() == "usdt")
                        ApiCallResult.Success(
                            MarketPrice(
                                symbol = symbol,
                                buyPrice = response.result.usdtBuy,
                                sellPrice = response.result.usdtSell,
                                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.coinkade }
                                    ?: ExchangeInfo(
                                        exchange = Exchanges.coinkade,
                                        active = true,
                                        display = true
                                    ),
                                lastRefresh = System.currentTimeMillis()
                            )
                        )
                    else ApiCallResult.Failure(GeneralError().withErrorMessage())
                }

                is ApiCallResult.Failure -> {
                    ApiCallResult.Failure(response.error)
                }
            }
        }
    }
}

