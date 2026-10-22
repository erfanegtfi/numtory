package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoMarket.domain.entities.COPPER
import com.numtory.application.features.cryptoMarket.domain.entities.GOLD
import com.numtory.application.features.cryptoMarket.domain.entities.SILVER
import com.numtory.application.features.goldExchange.data.repositories.GoldMarketRepository
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMelliGoldPriceUseCase constructor(
    private val marketRepository: GoldMarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<GoldMarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()

        return marketRepository.getMelliGoldPrice(symbol).map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    ApiCallResult.Success(
                        GoldMarketPrice(
                            symbol = when(symbol){
                                "XAU18" -> GOLD
                                "XAG" -> SILVER
                                "XCU" -> COPPER
                                else -> ""
                            },
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

