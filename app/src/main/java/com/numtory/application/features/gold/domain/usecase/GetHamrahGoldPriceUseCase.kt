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

class GetHamrahGoldPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getHamrahGoldPrice().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    if (response.result.buy?.isNotEmpty() == true && response.result.sell?.isNotEmpty() == true)
                        ApiCallResult.Success(
                            GoldMarketPrice(
                                symbol = GOLD,
                                buyPrice = (response.result.buy.first() / 10).toString(),
                                sellPrice = (response.result.sell.first() / 10).toString(),
                                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.hamrahgold }
                                    ?: GoldExchangeInfo(
                                        exchange = GoldExchanges.hamrahgold,
                                        active = true,
                                        display = true
                                    ),
                                lastRefresh = System.currentTimeMillis()
                            )
                        )
                    else ApiCallResult.Failure(GeneralError().withErrorMessage())
                }

                is ApiCallResult.Failure -> {
                    ApiCallResult.Failure(response.error)
                }
            }
        }
    }
}

