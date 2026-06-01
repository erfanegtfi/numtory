package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.SarmayexCurrency
import com.numtory.application.features.market.domain.entities.SarmayexCurrencyPrice
import com.numtory.application.features.market.domain.entities.SarmayexMarketItem
import com.numtory.application.features.market.domain.entities.SarmayexSwap
import com.google.gson.annotations.SerializedName

data class SarmayexMarketListDataModel(
    @SerializedName("data")
    val stats: Map<String, SarmayexMarketItemDataModel>?
) {
    fun toEntity(): Map<String, SarmayexMarketItem>? =
        stats?.mapValues { (_, item) ->
            item.toEntity()
        }
}

data class SarmayexMarketItemDataModel constructor(
    @SerializedName("price")
    var price: String?,
    @SerializedName("base")
    var base: String?,
    @SerializedName("quote")
    var quote: String?
) {

    fun toEntity(): SarmayexMarketItem =
        SarmayexMarketItem(price, base, quote)
}

//
data class SarmayexSwapDataModel(
    @SerializedName("currency")
    var currency: SarmayexCurrencyDataModel?,
) {
    fun toEntity(): SarmayexSwap =
        SarmayexSwap(currency?.toEntity())
}


data class SarmayexCurrencyDataModel constructor(
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("sell")
    var sell: SarmayexCurrencyPriceDataModel?,
    @SerializedName("buy")
    var buy: SarmayexCurrencyPriceDataModel?
) {

    fun toEntity(): SarmayexCurrency =
        SarmayexCurrency(symbol, sell?.toEntity(), buy?.toEntity())
}

data class SarmayexCurrencyPriceDataModel constructor(
    @SerializedName("price")
    var price: String?,

) {

    fun toEntity(): SarmayexCurrencyPrice =
        SarmayexCurrencyPrice(price)
}