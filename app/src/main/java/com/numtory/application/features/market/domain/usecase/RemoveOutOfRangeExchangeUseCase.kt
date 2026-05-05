package com.numtory.application.features.market.domain.usecase

import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.ui.theme.PERCENT

class RemoveOutOfRangeExchangeUseCase constructor(val getMarketAvgUseCase: GetMarketAvgUseCase) {


    fun action(params: RemoveOutOfRangeExchangesParams): List<MarketPrice> {

        return params.markets
            .filter {
                ((it.sellPrice != null && it.sellPrice?.toFloat() != 0f) && (it.buyPrice != null && it.buyPrice?.toFloat() != 0f))
                        || it.marketPrice != null && it.marketPrice?.toFloat() != 0f
            }.filter {
                if(params.avgBuy == 0f) true
//                var buy = (it.marketPrice ?: it.buyPrice ?: "0").toFloat() - avgBuy
//                var sell = (it.marketPrice ?: it.sellPrice ?: "0").toFloat() - avgSell

                var buy = (it.finalBuyPrice).toFloat() - params.avgBuy
                var sell = (it.finalSellPrice).toFloat() - params.avgSell

                if (buy < 0) buy = buy * -1
                if (sell < 0) sell = buy * -1

                val baseBuy = params.avgBuy * PERCENT
                val baseSell = params.avgSell * PERCENT

                buy < baseBuy && sell < baseSell
            }
    }
}

data class RemoveOutOfRangeExchangesParams(
//    val exchange: Exchanges? = null,
    val avgBuy: Float,
    val avgSell: Float,
    var markets: List<MarketPrice>
)
