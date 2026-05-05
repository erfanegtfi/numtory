package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBitPinPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(marketId: Int): Flow<ApiCallResult<MarketPrice>> {
        return marketRepository.getBitPin(marketId).map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = response.result.buy,
                            sellPrice = response.result.sell,
                            exchange = Exchanges.bitpin,
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

