package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Digikala
import com.numtory.application.features.gold.domain.entities.DigikalaPrice
import com.numtory.application.features.gold.domain.entities.Goldika
import com.numtory.application.features.gold.domain.entities.GoldikaData
import com.numtory.application.features.gold.domain.entities.GoldikaPrice

data class GoldikaDataModel(
    @SerializedName("data")
    val data: GoldikaDataDataModel?,
) {
    fun toEntity(): Goldika = Goldika(data?.toEntity())
}

data class GoldikaDataDataModel(
    @SerializedName("price")
    val price: GoldikaPriceDataModel?,
) {
    fun toEntity(): GoldikaData = GoldikaData(price?.toEntity())
}

data class GoldikaPriceDataModel(
    @SerializedName("sell")
    val sell: Long,
    @SerializedName("buy")
    val buy: Long,
) {
    fun toEntity(): GoldikaPrice = GoldikaPrice(sell, buy)
}
