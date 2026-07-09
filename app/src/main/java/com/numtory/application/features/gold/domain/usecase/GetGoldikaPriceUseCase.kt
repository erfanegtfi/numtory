package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.GOLD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGoldikaPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getGoldikaPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = GOLD,
                            buyPrice = ((response.result.data?.price?.buy ?: 0) / 10).toString(),
                            sellPrice = ((response.result.data?.price?.sell ?: 0) / 10).toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.goldika }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.goldika,
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

