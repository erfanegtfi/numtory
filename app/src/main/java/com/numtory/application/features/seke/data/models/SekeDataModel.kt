package com.numtory.application.features.seke.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.seke.domain.entities.SekePrice


data class SekeDataModel constructor(
    @SerializedName("title")
    var title: String?,
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("sell")
    var sell: String?,
    @SerializedName("last_update")
    var lastUpdate: String?,
) {

    fun toEntity(): SekePrice =
        SekePrice(title, symbol, sell, lastUpdate)
}

