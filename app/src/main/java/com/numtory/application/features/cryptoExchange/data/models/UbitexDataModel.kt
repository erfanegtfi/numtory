package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.Ubitex
import com.numtory.application.features.cryptoExchange.domain.entities.UbitexPrice


data class UbitexDataModel constructor(
    @SerializedName("bestBuy")
    var buy: UbitexPriceDataModel?,
    @SerializedName("bestSell")
    var sell: UbitexPriceDataModel?
) {

    fun toEntity(): Ubitex =
        Ubitex(buy?.toEntity(), sell?.toEntity())
}

data class UbitexPriceDataModel constructor(
    @SerializedName("price")
    var price: String?,
) {

    fun toEntity(): UbitexPrice =
        UbitexPrice(price)
}

