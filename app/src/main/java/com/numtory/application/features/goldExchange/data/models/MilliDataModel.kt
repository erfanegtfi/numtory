package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.Milli
import com.numtory.application.features.goldExchange.domain.entities.MilliPrice

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