package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.ArzinjaItem
import com.numtory.application.features.cryptoExchange.domain.entities.ArzinjaState

data class ArzinjaDataModel(
    @SerializedName("result")
    val stats: List<Map<String, ArzinjaItemDataModel>>
) {
    fun toEntity(): List<Map<String, ArzinjaItem>> =
       stats.map {
           it.mapValues { (_, item) ->
               item.toEntity()
           }
       }
}

data class ArzinjaItemDataModel constructor(
    @SerializedName("pair")
    var pair: String?,
    @SerializedName("baseAsset")
    var baseAsset: String?,
    @SerializedName("quoteAsset")
    var quoteAsset: String?,
    @SerializedName("stats")
    var stats: ArzinjaStateDataModel?,
) {

    fun toEntity(): ArzinjaItem =
        ArzinjaItem(pair, baseAsset, quoteAsset, stats?.toEntity())
}

data class ArzinjaStateDataModel constructor(
    @SerializedName("lastPrice")
    var lastPrice: String?,
    @SerializedName("bidPrice")
    var bidPrice: String?,
    @SerializedName("askPrice")
    var askPrice: String?,
) {

    fun toEntity(): ArzinjaState =
        ArzinjaState(lastPrice, bidPrice, askPrice)
}