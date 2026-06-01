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

    fun action(base: String, quote:String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getPingi().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset = response.result.get("{$base}_$quote")

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = asset?.market,
                            buyPrice = asset?.price,
                            sellPrice = asset?.price,
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

