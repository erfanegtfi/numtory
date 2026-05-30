package com.numtory.application.features.cryptoMarket.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice


data class CryptoMarketItemDataModel constructor(
    @SerializedName("price")
    var price: String?,
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("daily_change_price")
    var dayChange: Float?
) {

    fun toEntity(): CryptoMarketPrice =
        CryptoMarketPrice(price, symbol, dayChange?:0f)
}

