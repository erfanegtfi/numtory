package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPingiPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(market: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getPingi().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val tether = response.result.get(market)

                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = tether?.price,
                            sellPrice = tether?.price,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.pingi } ?: ExchangeInfo(
                                exchange = Exchanges.pingi,
                                active = true,
                                display = true
                            ),
                            lastRefresh = System.currentTimeMillis()
                        )
                    )
                }

                is ApiCallResult.Failure -> {
                    ApiCallResult.Failure(response.error)
                }
            }
        }
    }
}

