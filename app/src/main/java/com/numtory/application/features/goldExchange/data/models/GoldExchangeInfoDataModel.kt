package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges


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