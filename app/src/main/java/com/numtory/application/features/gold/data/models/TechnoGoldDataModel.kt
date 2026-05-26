package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Milli
import com.numtory.application.features.gold.domain.entities.MilliPrice
import com.numtory.application.features.gold.domain.entities.TalaSea
import com.numtory.application.features.gold.domain.entities.TechnoGold
import com.numtory.application.features.gold.domain.entities.TechnoGoldPrice
import com.numtory.application.features.gold.domain.entities.WallGold
import com.numtory.application.features.gold.domain.entities.WallGoldPrice

data class TechnoGoldDataModel(
    @SerializedName("results")
    val data: TechnoGoldPriceDataModel?,
) {
    fun toEntity(): TechnoGold = TechnoGold(data?.toEntity())
}

data class TechnoGoldPriceDataModel(
    @SerializedName("sell_price")
    val sellPrice: Int?,
    @SerializedName("buy_price")
    val buyPrice: Int?,
) {
    fun toEntity(): TechnoGoldPrice = TechnoGoldPrice(sellPrice, buyPrice)
}