package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.GOLD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDaricPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getDaricPrice(symbol).map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = symbol,
                            buyPrice = (response.result.bestBuy?.price?:"0").toString(),
                            sellPrice = (response.result.bestBuy?.price?:"0").toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.daric }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.daric,
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

