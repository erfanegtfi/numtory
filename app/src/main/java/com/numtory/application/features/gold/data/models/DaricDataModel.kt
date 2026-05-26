package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Daric
import com.numtory.application.features.gold.domain.entities.DaricPrice
import com.numtory.application.features.gold.domain.entities.Digikala
import com.numtory.application.features.gold.domain.entities.DigikalaPrice

data class DaricDataModel(
    @SerializedName("bestBuy")
    val bestBuy: DaricPriceDataModel?,
    @SerializedName("bestSell")
    val bestSell: DaricPriceDataModel?,
) {
    fun toEntity(): Daric = Daric(bestBuy?.toEntity(), bestSell?.toEntity())
}

data class DaricPriceDataModel(
    @SerializedName("price")
    val price: Int,
) {
    fun toEntity(): DaricPrice = DaricPrice(price)
}
