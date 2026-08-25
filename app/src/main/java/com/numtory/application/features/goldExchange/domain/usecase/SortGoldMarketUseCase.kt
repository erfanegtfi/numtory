package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.SortField
import com.numtory.application.features.cryptoExchange.domain.enums.SortOrder

class SortGoldMarketUseCase constructor() {

    fun action(params: SortGoldParams): List<GoldMarketPrice> {
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

data class SortGoldParams(var sortField: SortField = SortField.BuyPrice, var sortOrder: SortOrder = SortOrder.Descending, var markets: List<GoldMarketPrice> = emptyList()){
    // Override equals to ignore markets list
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SortGoldParams) return false

        return sortField == other.sortField &&
                sortOrder == other.sortOrder
    }

    override fun hashCode(): Int {
        return 31 * sortField.hashCode() + sortOrder.hashCode()
    }
}
