package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.data.utils.GeneralError
import com.numtory.application.data.utils.withErrorMessage
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetEcoGoldPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getEcoGoldPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    val asset = response.result.data?.firstOrNull { it.symbol == symbol }

                    ApiCallResult.Success(
                        GoldMarketPrice(
                            buyPrice = (asset?.buyPrice).toString(),
                            sellPrice = (asset?.sellPrice).toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.ecogold }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.ecogold,
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

