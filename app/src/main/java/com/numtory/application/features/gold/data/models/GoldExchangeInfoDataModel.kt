package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.serialization.Serializable


data class GoldExchangeInfoDataModel constructor(
    @SerializedName("id")
    var id: Integer,
    @SerializedName("name")
    var name: String,
    @SerializedName("display")
    var display: Boolean,
    @SerializedName("active")
    var active: Boolean,
    @SerializedName("isMarket")
    var isMarket: Boolean,
    @SerializedName("hasMarket")
    var hasMarket: Boolean,
    @SerializedName("fee")
    var fee: Float,
) {

    fun toEntity(): GoldExchangeInfo =
        GoldExchangeInfo(id, GoldExchanges.fromString(name), display, active, isMarket, hasMarket, fee)
}