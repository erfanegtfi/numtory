package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWallexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String, quote: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getWallex(base).map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset = response.result.results.firstOrNull()

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = asset?.baseAsset,
                            buyPrice = asset?.quotes?.get(quote)?.price,
                            sellPrice = asset?.quotes?.get(quote)?.price,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.wallex }?: ExchangeInfo(
                                exchange = Exchanges.wallex,
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

