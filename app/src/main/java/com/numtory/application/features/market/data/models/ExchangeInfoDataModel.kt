package com.numtory.application.features.market.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.serialization.Serializable


data class ExchangeInfoDataModel constructor(
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

    fun toEntity(): ExchangeInfo =
        ExchangeInfo(id, Exchanges.fromString(name) , display, active, isMarket, hasMarket, fee)
}