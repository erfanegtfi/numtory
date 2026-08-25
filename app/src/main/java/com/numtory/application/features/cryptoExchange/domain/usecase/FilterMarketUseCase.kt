package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import com.numtory.application.features.cryptoExchange.domain.enums.FilterBy
import kotlin.collections.filter

class FilterMarketUseCase constructor() {

    fun action(params: FilterParams): List<MarketPrice> {
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
    var userExchanges: List<Exchanges>? = null,
    var exchangesInfo: List<ExchangeInfo>? = null,
    var markets: List<MarketPrice> = emptyList(),
    var addFee: Boolean = false
) {
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
