package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSarafPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getSaraf().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset = response.result.price.items[base]

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = asset?.symbol,
                            buyPrice = asset?.price,
                            sellPrice = asset?.price,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.saraf } ?: ExchangeInfo(
                                exchange = Exchanges.saraf,
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

