package com.numtory.application.features.seke.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.seke.domain.entities.SekePrice


data class SekeDataModel constructor(
    @SerializedName("representation_name")
    var title: String?,
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("price_in_irt")
    var sell: String?,
    @SerializedName("last_updated_at")
    var lastUpdate: String?,
) {

    fun toEntity(): SekePrice =
        SekePrice(title, symbol, sell, lastUpdate)
}

