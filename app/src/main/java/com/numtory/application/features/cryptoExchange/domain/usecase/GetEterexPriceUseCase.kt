package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEterexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(
        baseAsset: String,
        quoteAsset: String
    ): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getEterexAssetsPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    val asset =
                        response.result?.firstOrNull { item -> item.baseAsset.equals(baseAsset, ignoreCase = true) && item.quoteAsset.equals(
                            quoteAsset, ignoreCase = true) }

                    ApiCallResult.Success(
                        MarketPrice(
                            symbol = baseAsset,
                            buyPrice = (asset?.bestBuyPrice ?: "0").toString(),
                            sellPrice = (asset?.bestSellPrice ?: "0").toString(),
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