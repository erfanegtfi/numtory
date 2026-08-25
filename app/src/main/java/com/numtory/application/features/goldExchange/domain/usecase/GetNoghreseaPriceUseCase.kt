package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepository
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.SILVER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNoghreseaPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getNoghreseaPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = SILVER,
                            buyPrice = ((response.result.price?.toDouble()?.toLong() ?: 0) * 1000).toString(),
                            sellPrice = ((response.result.price?.toDouble()?.toLong() ?: 0) * 1000).toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.noghresea }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.noghresea,
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

