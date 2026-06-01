package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.features.gold.domain.entities.GoldMarketPrice

class RemoveInvalidGoldExchangeUseCase constructor() {

    fun action(params: RemoveInvalidGoldExchangesParams): List<GoldMarketPrice> {

        return params.markets
            .filter {
                ((it.sellPrice != null && it.sellPrice?.toDouble() != 0.0) && (it.buyPrice != null && it.buyPrice?.toDouble() != 0.0))
                        || it.marketPrice != null && it.marketPrice?.toDouble() != 0.0
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

data class RemoveInvalidGoldExchangesParams(
//    val exchangesInfo: List<ExchangeInfo>? = null,
    var markets: List<GoldMarketPrice>
)
