package com.numtory.application.features.market.domain.usecase

import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice

class RemoveInvalidExchangeUseCase constructor() {

    fun action(params: RemoveInvalidExchangesParams): List<MarketPrice> {

        return params.markets
            .filter {
                ((it.sellPrice != null && it.sellPrice?.toFloat() != 0f) && (it.buyPrice != null && it.buyPrice?.toFloat() != 0f))
                        || it.marketPrice != null && it.marketPrice?.toFloat() != 0f
            }
//            .filter { marketPrice ->
//                if (params.exchangesInfo?.isNotEmpty() != true) true
//                else {
//                    params.exchangesInfo.filter { it -> it.display }.map { it ->
//                        it.exchange
//                    }.contains(marketPrice.exchangeInfo.exchange)
//                }
//
//            }
    }
}

data class RemoveInvalidExchangesParams(
//    val exchangesInfo: List<ExchangeInfo>? = null,
    var markets: List<MarketPrice>
)
