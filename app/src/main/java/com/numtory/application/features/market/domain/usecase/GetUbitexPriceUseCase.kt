package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUbitexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String, quote: String): Flow<ApiCallResult<MarketPrice>> {
        return marketRepository.getUbitexPrice(base,quote ).map { response ->
            when (response) {
                is ApiCallResult.Success -> {

                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = response.result?.sell?.price,
                            sellPrice = response.result?.buy?.price,
                            exchange = Exchanges.ubitex,
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

