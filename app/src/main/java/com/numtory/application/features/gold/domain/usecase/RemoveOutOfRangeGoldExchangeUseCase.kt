package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.ui.theme.PERCENT

class RemoveOutOfRangeGoldExchangeUseCase constructor() {


    fun action(params: RemoveOutOfRangeGoldExchangesParams): List<GoldMarketPrice> {

        return params.markets
            .filter {
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

data class RemoveOutOfRangeGoldExchangesParams(
    val avgBuy: Float,
    val avgSell: Float,
    var markets: List<GoldMarketPrice>
)
