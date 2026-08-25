package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepository
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.GOLD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetZarafzaPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getZarafzaPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    val gold18 = response.result.data?.gold18

                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = GOLD,
                            // zarafza names prices from its own side: what it sells is what the user buys.
                            buyPrice = ((gold18?.sell?.price)?.toLong() ?: 0).toString(),
                            sellPrice = ((gold18?.buy?.price)?.toLong() ?: 0).toString(),
                            exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.zarafza }
                                ?: GoldExchangeInfo(
                                    exchange = GoldExchanges.zarafza,
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
