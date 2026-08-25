package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.cryptoExchange.domain.entities.BestPrices

class GetGoldBestPriceUseCase constructor() {

    /**
     * The best of both sides across the list: the least you pay to buy and the most
     * you get for selling, each with the exchange offering it. Either side is null
     * while no exchange has quoted a usable price.
     */
    fun action(priceList: List<GoldMarketPrice>): BestPrices {
        val bestBuy = priceList
            .mapNotNull { market ->
                market.finalBuyPrice.toDoubleOrNull()?.takeIf { it > 0 }?.let { market to it }
            }
            .minByOrNull { it.second }

        val bestSell = priceList
            .mapNotNull { market ->
                market.finalSellPrice.toDoubleOrNull()?.takeIf { it > 0 }?.let { market to it }
            }
            .maxByOrNull { it.second }

        return BestPrices(
            buyPrice = bestBuy?.second,
            buyExchange = bestBuy?.first?.exchangeInfo?.exchange?.title,
            sellPrice = bestSell?.second,
            sellExchange = bestSell?.first?.exchangeInfo?.exchange?.title,
        )
    }
}
