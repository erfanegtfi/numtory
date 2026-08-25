package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.EcoGold
import com.numtory.application.features.goldExchange.domain.entities.EcoGoldPrice

data class EcoGoldDataModel(
    @SerializedName("data")
    val data: List<EcoGoldPriceDataModel>?,
) {
    fun toEntity(): EcoGold = EcoGold(data?.map { it.toEntity() })
}

data class EcoGoldPriceDataModel(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("sell_price")
    val sellPrice: String?,
    @SerializedName("buy_price")
    val buyPrice: String?,
) {
    fun toEntity(): EcoGoldPrice = EcoGoldPrice(symbol, sellPrice, buyPrice)
}