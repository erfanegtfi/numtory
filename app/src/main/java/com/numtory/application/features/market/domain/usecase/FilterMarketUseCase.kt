package com.numtory.application.features.market.domain.usecase

import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import com.numtory.application.features.market.domain.enums.FilterBy

class FilterMarketUseCase constructor() {

    fun action(params: FilterParams): List<MarketPrice> {
        return params.markets
            .let { markets ->
                if (params.activeExchanges != null)
                    markets.filter { params.activeExchanges?.contains(it.exchangeInfo?.exchange) == true }
                else markets
            }
            .let { markets ->
                for (market in markets) {
                    market.addFee = params.addFee
                }
                markets
            }
            .let { markets ->
                when (params.filter) {
                    FilterBy.QuickSwap ->
                        markets.filter { it.marketPrice == null || it.marketPrice?.toFloat() == 0f }

                    FilterBy.Market ->
                        markets.filter { it.marketPrice != null && it.marketPrice?.toFloat() != 0f }

                    else -> markets
                }
            }
    }

}

data class FilterParams(
    val filter: FilterBy = FilterBy.All,
    var activeExchanges: List<Exchanges>? = null,
    var markets: List<MarketPrice> = emptyList(),
    var addFee: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FilterParams) return false

        return filter == other.filter &&
                addFee == other.addFee
    }

    override fun hashCode(): Int {
        return 31 * filter.hashCode() + addFee.hashCode()
    }
}
