package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import com.numtory.application.features.market.domain.enums.FilterBy
import kotlin.collections.filter

class FilterGoldMarketUseCase constructor() {

    fun action(params: FilterGoldParams): List<GoldMarketPrice> {
        return params.markets
            .let { markets ->
                if (params.userExchanges != null)
                    markets.filter { params.userExchanges?.contains(it.exchangeInfo.exchange) == true }
                else markets
            }.let { markets ->
                if (params.exchangesInfo?.isNotEmpty() != true) markets
                else
                    markets.filter { market -> market.exchangeInfo.display
//                        if (params.exchangesInfo?.isNotEmpty() != true) true

//                        params.exchangesInfo?.filter { it -> it.display }?.map { it ->
//                            it.exchange
//                        }?.contains(market.exchangeInfo.exchange) == true
                    }
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
                        markets.filter { it.marketPrice == null || it.marketPrice?.toDouble() == 0.0 }

                    FilterBy.Market ->
                        markets.filter { it.marketPrice != null && it.marketPrice?.toDouble() != 0.0 }

                    else -> markets
                }
            }
    }

}

data class FilterGoldParams(
    val filter: FilterBy = FilterBy.All,
    var userExchanges: List<GoldExchanges>? = null,
    var exchangesInfo: List<GoldExchangeInfo>? = null,
    var markets: List<GoldMarketPrice> = emptyList(),
    var addFee: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FilterGoldParams) return false

        return filter == other.filter &&
                addFee == other.addFee
    }

    override fun hashCode(): Int {
        return 31 * filter.hashCode() + addFee.hashCode()
    }
}
