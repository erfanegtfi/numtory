package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.Arz3CoinItem
import com.numtory.application.features.cryptoExchange.domain.entities.Arz3Price

data class Arz3coinsDataModel(
    @SerializedName("coins")
    val coins: List<Arz3CoinItemDataModel>?

) {
    fun toEntity(): List<Arz3CoinItem>? =
        coins?.map { it.toEntity() }
}


data class Arz3CoinItemDataModel constructor(
    @SerializedName("name")
    var name: String?,
    @SerializedName("symbol")
    var symbol: String?  ,
    @SerializedName("price")
    var price: Arz3PriceDataModel?
) {

    fun toEntity(): Arz3CoinItem =
        Arz3CoinItem(name, symbol, price?.toEntity())
}

data class Arz3PriceDataModel constructor(
    @SerializedName("buy")
    var buy: String?,
    @SerializedName("sell")
    var sell: String?
) {

    fun toEntity(): Arz3Price =
        Arz3Price(buy, sell)
}