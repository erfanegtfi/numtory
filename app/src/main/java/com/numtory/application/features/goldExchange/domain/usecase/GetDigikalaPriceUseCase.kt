package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepository
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.GOLD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDigikalaPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getDigikalaPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = GOLD,
                            buyPrice = ((response.result.gold18?.price ?: 0) * 100).toString(),
                            sellPrice = ((response.result.gold18?.price ?: 0) * 100).toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.digikala }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.digikala,
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

