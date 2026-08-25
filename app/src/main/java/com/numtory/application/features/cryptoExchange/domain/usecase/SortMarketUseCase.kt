package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.SortField
import com.numtory.application.features.cryptoExchange.domain.enums.SortOrder

class SortMarketUseCase constructor() {

    fun action(params: SortParams): List<MarketPrice> {
        if (params.sortOrder == SortOrder.Descending)
            return params.markets.sortedByDescending {
                val price = (if (params.sortField == SortField.BuyPrice) it.finalBuyPrice else it.finalSellPrice)


                price.toDoubleOrNull() ?: 0.0
            }

        return params.markets.sortedBy {
            val price = (if (params.sortField == SortField.BuyPrice) it.finalBuyPrice else it.finalSellPrice)


            price.toDoubleOrNull() ?: 0.0
        }

    }

}

data class SortParams(var sortField: SortField = SortField.BuyPrice, var sortOrder: SortOrder = SortOrder.Descending, var markets: List<MarketPrice> = emptyList()){
    // Override equals to ignore markets list
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SortParams) return false

        return sortField == other.sortField &&
                sortOrder == other.sortOrder
    }

    override fun hashCode(): Int {
        return 31 * sortField.hashCode() + sortOrder.hashCode()
    }
}
