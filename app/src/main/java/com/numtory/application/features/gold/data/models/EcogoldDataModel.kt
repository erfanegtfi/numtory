package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.EcoGold
import com.numtory.application.features.gold.domain.entities.EcoGoldPrice
import com.numtory.application.features.gold.domain.entities.TalaSea
import com.numtory.application.features.gold.domain.entities.WallGold
import com.numtory.application.features.gold.domain.entities.WallGoldPrice

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