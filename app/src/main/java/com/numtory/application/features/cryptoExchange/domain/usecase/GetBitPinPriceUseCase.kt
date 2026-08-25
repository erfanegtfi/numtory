package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetBitPinPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String, quote: String): Flow<ApiCallResult<MarketPrice>> = flow {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        marketRepository.getBitPinMarket().collect { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    val asset =
                        response.result.firstOrNull { item -> item.code?.lowercase() == "${base}_$quote".lowercase() }


                    if (asset != null) {
                        emit(
                            ApiCallResult.Success(
                                MarketPrice(
                                    symbol =base,
                                    marketPrice = asset.results?.price,
                                    exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.bitpinMarket }
                                        ?: ExchangeInfo(
                                            exchange = Exchanges.bitpinMarket,
                                            active = true,
                                            display = true,
                                            hasMarket = true,
                                            isMarket = true,
                                        ),
                                    lastRefresh = System.currentTimeMillis()
                                )
                            )
                        )
                        if (asset.id != null)
                            emitAll(getOTCPrice(asset.id!!, base))
                    }

                }

                is ApiCallResult.Failure -> {
                    ApiCallResult.Failure(response.error)
                }
            }
        }
    }

    fun getOTCPrice(marketId: Int, base: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getBitPin(marketId).map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = base,
                            buyPrice = response.result.buy,
                            sellPrice = response.result.sell,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.bitpin }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.bitpin,
                                    active = true,
                                    display = true,
                                    hasMarket = true,
                                    isMarket = false,
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
