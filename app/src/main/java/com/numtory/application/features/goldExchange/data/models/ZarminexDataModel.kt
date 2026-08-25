package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.Zarminex

data class ZarminexDataModel(
    @SerializedName("buy")
    val buy: Long?,
    @SerializedName("sell")
    val sell: Long?,
) {
    fun toEntity(): Zarminex = Zarminex(buy, sell)
}

