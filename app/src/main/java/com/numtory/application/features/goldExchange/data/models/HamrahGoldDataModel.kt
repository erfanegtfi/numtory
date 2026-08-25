package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.HamrahGold

data class HamrahGoldDataModel(
    @SerializedName("buy")
    val buy: List<Long>?,
    @SerializedName("sell")
    val sell: List<Long>?,
) {
    fun toEntity(): HamrahGold = HamrahGold(buy, sell)
}

