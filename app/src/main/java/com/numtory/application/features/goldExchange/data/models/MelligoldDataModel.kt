package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.MelliGold
import com.numtory.application.features.goldExchange.domain.entities.MelliGoldPrice

data class MelliGoldDataModel(
    @SerializedName("data")
    val data: MelliGoldPriceDataModel?,
) {
    fun toEntity(): MelliGold = MelliGold(data?.toEntity())
}

data class MelliGoldPriceDataModel(
    @SerializedName("price_buy")
    val buy: Long?,
    @SerializedName("price_sell")
    val sell: Long?,
) {
    fun toEntity(): MelliGoldPrice = MelliGoldPrice(buy, sell)
}