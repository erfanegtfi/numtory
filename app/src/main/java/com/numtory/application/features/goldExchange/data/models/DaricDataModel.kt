package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.Daric
import com.numtory.application.features.goldExchange.domain.entities.DaricPrice

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
    val price: Long,
) {
    fun toEntity(): DaricPrice = DaricPrice(price)
}
