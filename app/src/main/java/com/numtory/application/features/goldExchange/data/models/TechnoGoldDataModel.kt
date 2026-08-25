package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.TechnoGold
import com.numtory.application.features.goldExchange.domain.entities.TechnoGoldPrice

data class TechnoGoldDataModel(
    @SerializedName("results")
    val data: TechnoGoldPriceDataModel?,
) {
    fun toEntity(): TechnoGold = TechnoGold(data?.toEntity())
}

data class TechnoGoldPriceDataModel(
    @SerializedName("sell_price")
    val sellPrice: Long?,
    @SerializedName("buy_price")
    val buyPrice: Long?,
) {
    fun toEntity(): TechnoGoldPrice = TechnoGoldPrice(sellPrice, buyPrice)
}