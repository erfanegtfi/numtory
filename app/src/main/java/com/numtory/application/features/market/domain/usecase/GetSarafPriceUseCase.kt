package com.numtory.application.features.market.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.repositories.MarketRepository
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSarafPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(base: String): Flow<ApiCallResult<MarketPrice>> {
        return marketRepository.getSaraf().map { response ->
            when (response) {
                is ApiCallResult.Success -> {
                    val asset = response.result.price.items[base]

                    ApiCallResult.Success(
                        MarketPrice(
                            buyPrice = asset?.price,
                            sellPrice = asset?.price,
                            exchange = Exchanges.saraf,
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

