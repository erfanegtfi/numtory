package com.numtory.application.features.market.domain.usecase

import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges

class GetMarketAvgUseCase constructor() {

    fun action(
        priceList: List<MarketPrice>,
        displayExchanges: List<Exchanges>,
    ): Pair<Float, Float> {
        var averageBuyPrice = 0f
        var averageSellPrice = 0f
        var size = 0


        priceList.filter {
            // in case that exchange info api not responded  (it.exchangeInfo == null) ||
            displayExchanges.contains(it.exchangeInfo?.exchange)
        }.forEach { item ->
            if (item.exchangeInfo?.hasMarket == true) {
                // if has quick swap and market we prefer market and skip quick swap
                if (item.marketPrice != null && item.marketPrice?.toFloat() != 0f) {
                    size++
                    averageBuyPrice += (item.marketPrice ?: "0").toFloat()
                    averageSellPrice += (item.marketPrice ?: "0").toFloat()
                }
            } else {
                size++
                averageBuyPrice += (item.finalBuyPrice).toFloat()
                averageSellPrice += (item.finalSellPrice).toFloat()
            }
        }
        if (size == 0) size = 1

        // Calculate averages
        return Pair(averageBuyPrice / size, averageSellPrice / size)
    }
}