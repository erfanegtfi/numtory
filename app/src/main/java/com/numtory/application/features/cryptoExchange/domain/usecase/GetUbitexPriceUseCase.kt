package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUbitexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String, quote: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getUbitexPrice(base,quote ).map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = base,
                            buyPrice = response.result?.sell?.price,
                            sellPrice = response.result?.buy?.price,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.ubitex }?: ExchangeInfo(
                                exchange = Exchanges.ubitex,
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

