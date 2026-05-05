package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAbanTetherPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(symbol: String): Flow<ApiCallResult<MarketPrice>> {
        return marketRepository.getAbanTether().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val tether =
                        response.result.firstOrNull { item -> item.symbol?.lowercase() == symbol.lowercase() }

                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = tether?.buy,
                            sellPrice = tether?.sell,
                            exchange = Exchanges.abantether,
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

