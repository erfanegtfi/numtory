package com.numtory.application.features.cryptoExchange.data.models

import com.numtory.application.features.cryptoExchange.domain.entities.Nobitex
import com.numtory.application.features.cryptoExchange.domain.entities.NobitexMarket
import com.numtory.application.features.cryptoExchange.domain.entities.NobitexResult
import com.google.gson.annotations.SerializedName

data class NobitexMarketListDataModel(
    @SerializedName("stats")
    val stats: Map<String, NobitexMarketItemDataModel>
) {
    fun toEntity(): Map<String, NobitexMarket> =
        stats.mapValues { (_, item) ->
            item.toEntity()
        }
}

data class NobitexMarketItemDataModel constructor(
    @SerializedName("latest")
    var latest: String?,
    @SerializedName("symbol")
    var symbol: String?
) {

    fun toEntity(): NobitexMarket =
        NobitexMarket(latest, symbol)
}



data class NobitexSwapDataModel(
    @SerializedName("stats")
    val stats: Map<String, NobitexDataModel>
) {
    fun toEntity(): Map<String, Nobitex> =
        stats.mapValues { (_, item) ->
            item.toEntity()
        }
}

data class NobitexDataModel constructor(
    @SerializedName("bestBuy")
    var baseToQuotePriceBuy: String?,
    @SerializedName("bestSell")
    var baseToQuotePriceSell: String?
) {

    fun toEntity(): Nobitex =
        Nobitex(baseToQuotePriceSell, baseToQuotePriceBuy)
}