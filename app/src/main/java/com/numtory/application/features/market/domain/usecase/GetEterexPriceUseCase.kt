package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEterexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(assetSymbol: String): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getEterexPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val tether =
                        response.result?.firstOrNull { item -> item.coins.contains(assetSymbol.uppercase()) }

                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = tether?.price?.irtUsdt,
                            sellPrice = tether?.price?.usdtIrt,
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.eterex }
                                ?: ExchangeInfo(
                                    exchange = Exchanges.eterex,
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

