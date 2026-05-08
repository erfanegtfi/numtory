package com.numtory.application.features.market.domain.usecase

import com.numtory.application.features.market.domain.entities.ExchangeStatus
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import okhttp3.internal.connection.Exchange

class RemoveInvalidExchangeUseCase constructor() {

    fun action(params: RemoveInvalidExchangesParams): List<MarketPrice> {

        return params.markets
//            .filterNot { it.exchange == params.removeExchange }

            .filter {
                ((it.sellPrice != null && it.sellPrice?.toFloat() != 0f) && (it.buyPrice != null && it.buyPrice?.toFloat() != 0f))
                        || it.marketPrice != null && it.marketPrice?.toFloat() != 0f
            }
            .filter { marketPrice ->
                if (params.exchangesStatus?.isNotEmpty() != true) true
                params.exchangesStatus?.filter { it -> it.display }?.map { it ->
                    it.exchange
                }?.contains(marketPrice.exchange) == true

            }
    }
}

data class RemoveInvalidExchangesParams(
    val exchangesStatus: List<ExchangeStatus>? = null,
//    val removeExchange: Exchanges? = null,
    var markets: List<MarketPrice>
)
