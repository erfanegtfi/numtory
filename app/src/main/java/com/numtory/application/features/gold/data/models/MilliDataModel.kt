package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Milli
import com.numtory.application.features.gold.domain.entities.MilliPrice
import com.numtory.application.features.gold.domain.entities.TalaSea
import com.numtory.application.features.gold.domain.entities.WallGold
import com.numtory.application.features.gold.domain.entities.WallGoldPrice

data class MilliDataModel(
    @SerializedName("data")
    val data: MilliPriceDataModel?,
) {
    fun toEntity(): Milli = Milli(data?.toEntity())
}

data class MilliPriceDataModel(
    @SerializedName("price18")
    val price: Long?,
) {
    fun toEntity(): MilliPrice = MilliPrice(price)
}