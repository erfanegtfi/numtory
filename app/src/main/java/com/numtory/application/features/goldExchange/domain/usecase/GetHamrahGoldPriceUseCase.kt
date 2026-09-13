package com.numtory.application.features.goldExchange.domain.usecase

import android.annotation.SuppressLint
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.data.utils.GeneralError
import com.numtory.application.data.utils.withErrorMessage
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepository
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import com.numtory.application.ui.theme.GOLD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class GetHamrahGoldPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        val flowBuy = marketRepository.getHamrahGoldPrice(true)
        val flowSell = marketRepository.getHamrahGoldPrice(false)

        return combine(flowBuy, flowSell) { buy, sell ->
            Pair(buy, sell)
        }.transform { (buyResponse, sellResponse) ->

            var sellPrice: Long? = null
            var buyPrice: Long? = null

            when (buyResponse) {
                is ApiCallResult.Success -> {
                    sellPrice = buyResponse.result.data?.current
//                    else ApiCallResult.Failure(GeneralError().withErrorMessage())
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyResponse.error))
                }
            }

            when (sellResponse) {
                is ApiCallResult.Success -> {
                    buyPrice = sellResponse.result.data?.current
//                    else ApiCallResult.Failure(GeneralError().withErrorMessage())
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellResponse.error))
                }
            }

            if (buyPrice != null && sellPrice != null) {
                val market = GoldMarketPrice(
                    symbol = GOLD,
                    buyPrice = ((buyPrice) / 10).toString(),
                    sellPrice = ((sellPrice) / 10).toString(),
                    exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == GoldExchanges.hamrahgold }
                        ?: GoldExchangeInfo(
                            exchange = GoldExchanges.hamrahgold,
                            active = true,
                            display = true
                        ),
                    lastRefresh = System.currentTimeMillis()
                )
                emit(ApiCallResult.Success(market))
            }
        }

    }
}

