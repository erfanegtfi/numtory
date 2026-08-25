package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.ui.theme.PERCENT

class RemoveOutOfRangeExchangeUseCase constructor() {


    fun action(params: RemoveOutOfRangeExchangesParams): List<MarketPrice> {

        return params.markets
            .filter {
                if(params.avgBuy == 0.0) true
//                var buy = (it.marketPrice ?: it.buyPrice ?: "0").toFloat() - avgBuy
//                var sell = (it.marketPrice ?: it.sellPrice ?: "0").toFloat() - avgSell

                var buy = (it.finalBuyPrice).toDouble() - params.avgBuy
                var sell = (it.finalSellPrice).toDouble() - params.avgSell

                if (buy < 0) buy = buy * -1
                if (sell < 0) sell = buy * -1

                val baseBuy = params.avgBuy * PERCENT
                val baseSell = params.avgSell * PERCENT

                buy < baseBuy && sell < baseSell
            }
    }
}

data class RemoveOutOfRangeExchangesParams(
    val avgBuy: Double,
    val avgSell: Double,
    var markets: List<MarketPrice>
)
