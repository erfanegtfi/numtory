package com.numtory.application.features.market.domain.usecase

import com.numtory.application.features.market.domain.entities.MarketPrice

class GetMarketAvgUseCase constructor() {

    fun action(priceList: List<MarketPrice>): Pair<Float, Float> {
        var averageBuyPrice = 0f
        var averageSellPrice = 0f
        var size = 0

//        val filteredList = priceList.filter {
//            ((it.sellPrice != null && it.sellPrice?.toFloat() != 0f) &&
//                    (it.buyPrice != null && it.buyPrice?.toFloat() != 0f)) ||
//                    (it.marketPrice != null && it.marketPrice?.toFloat() != 0f)
//        }


        priceList.forEach { item ->
            if (item.exchange?.bothTypes == true) {
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