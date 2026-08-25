package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.WallGold
import com.numtory.application.features.goldExchange.domain.entities.WallGoldPrice

data class WallGoldDataModel(
    @SerializedName("result")
    val result: WallGoldPriceDataModel?,
) {
    fun toEntity(): WallGold = WallGold(result?.toEntity())
}

data class WallGoldPriceDataModel(
    @SerializedName("price")
    val price: String?,
) {
    fun toEntity(): WallGoldPrice = WallGoldPrice(price)
}