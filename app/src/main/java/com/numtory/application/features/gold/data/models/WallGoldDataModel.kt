package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.TalaSea
import com.numtory.application.features.gold.domain.entities.WallGold
import com.numtory.application.features.gold.domain.entities.WallGoldPrice

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