package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges

class GetMarketAvgUseCase constructor() {

    fun action(
        priceList: List<MarketPrice>,
        displayExchanges: List<Exchanges>,
    ): Pair<Double, Double> {
        var averageBuyPrice = 0.0
        var averageSellPrice = 0.0
        var size = 0


        priceList.filter {
            // in case that exchange info api not responded  (it.exchangeInfo == null) ||
            displayExchanges.contains(it.exchangeInfo.exchange)
        }.forEach { item ->
            if (item.exchangeInfo.hasMarket == true) {
                // if has quick swap and market we prefer market and skip quick swap
                if (item.marketPrice != null && item.marketPrice?.toDouble() != 0.0) {
                    size++
                    averageBuyPrice += (item.marketPrice ?: "0").toDouble()
                    averageSellPrice += (item.marketPrice ?: "0").toDouble()
                }
            } else {
                size++
                averageBuyPrice += (item.finalBuyPrice).toDouble()
                averageSellPrice += (item.finalSellPrice).toDouble()
            }
        }
        if (size == 0) size = 1

        // Calculate averages
        return Pair(averageBuyPrice / size, averageSellPrice / size)
    }
}