package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.gold.data.repositories.GoldMarketRepository
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGeramiPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getGeramiPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    val pair = response.result.pairs?.firstOrNull { it.type == symbol }

                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = symbol,
                            buyPrice = (pair?.buyPrice ?: "0"),
                            sellPrice = (pair?.sellPrice ?: "0"),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.gerami }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.gerami,
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
