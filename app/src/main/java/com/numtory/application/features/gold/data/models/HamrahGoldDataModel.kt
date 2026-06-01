package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Goldika
import com.numtory.application.features.gold.domain.entities.HamrahGold

data class HamrahGoldDataModel(
    @SerializedName("buy")
    val buy: List<Long>?,
    @SerializedName("sell")
    val sell: List<Long>?,
) {
    fun toEntity(): HamrahGold = HamrahGold(buy, sell)
}

