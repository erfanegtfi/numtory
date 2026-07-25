package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges


class MergeGoldPriceUseCase constructor(
    private val removeInvalidExchangeUseCase: RemoveInvalidGoldExchangeUseCase,
    private val getMarketAvgUseCase: GetGoldMarketAvgUseCase,
    private val removeOutOfRangeExchangesUseCase: RemoveOutOfRangeGoldExchangeUseCase,
) {

    fun action(params: MergeGoldPriceParams): GoldMarketSnapshot {
        val merged = params.markets
            .filterNot { it.exchangeInfo.exchange == params.price.exchangeInfo.exchange }
            .toMutableList()

        if (params.price.symbol?.lowercase()?.contains(params.symbol.lowercase()) == true)
            merged.add(params.price)

        val allMarkets = removeInvalidExchangeUseCase
            .action(RemoveInvalidGoldExchangesParams(markets = merged))

        // Average over every exchange, not just the user's, so a bad price stands out.
        // Adding exchanges into the average would let one out-of-range first price
        // skew it and drag the rest of the list out with it.
        val (avgBuy, avgSell) = getMarketAvgUseCase.action(allMarkets, params.userExchanges)

        val validMarkets = removeOutOfRangeExchangesUseCase.action(
            RemoveOutOfRangeGoldExchangesParams(
                avgSell = avgSell,
                avgBuy = avgBuy,
                markets = allMarkets,
            )
        )

        return GoldMarketSnapshot(allMarkets = allMarkets, validMarkets = validMarkets)
    }
}

data class MergeGoldPriceParams(
    val price: GoldMarketPrice,
    val symbol: String,
    val markets: List<GoldMarketPrice>,
    val userExchanges: List<GoldExchanges>,
)

data class GoldMarketSnapshot(
    val allMarkets: List<GoldMarketPrice>,
    val validMarkets: List<GoldMarketPrice>,
)
