package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.Coinkade
import com.google.gson.annotations.SerializedName

data class CoinkadeDataModel(
    @SerializedName("usdtSell")
    val usdtSell: String,
    @SerializedName("usdtBuy")
    val usdtBuy: String
) {
    fun toEntity(): Coinkade = Coinkade(usdtSell, usdtBuy)
}

