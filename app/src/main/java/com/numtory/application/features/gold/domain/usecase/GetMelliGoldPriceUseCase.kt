package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.data.utils.GeneralError
import com.numtory.application.data.utils.withErrorMessage
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.GOLD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMelliGoldPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getMelliGoldPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = GOLD,
                            buyPrice = (response.result.data?.buy ?:"0").toString(),
                            sellPrice = (response.result.data?.sell ?:"0").toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.melligold }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.melligold,
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

