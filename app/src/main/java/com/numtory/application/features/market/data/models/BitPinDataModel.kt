package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.BitPin
import com.google.gson.annotations.SerializedName


data class BitPinDataModel constructor(
    @SerializedName("sell")
    var sell: String?,
    @SerializedName("buy")
    var buy: String?
) {

    fun toEntity(): BitPin =
        BitPin(sell, buy)
}