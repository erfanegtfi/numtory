package com.numtory.application.features.market.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.Morbit
import com.numtory.application.features.market.domain.entities.MorbitItem


data class MorbitDataModel constructor(
    @SerializedName("data")
    var data: List<MorbitItemDataModel>?,
) {

    fun toEntity(): Morbit =
        Morbit(data?.map { it.toEntity() })
}

data class MorbitItemDataModel constructor(
    @SerializedName("instrument")
    var instrument: String?,
    @SerializedName("buyPrice")
    var buyPrice: String?,
    @SerializedName("sellPrice")
    var sellPrice: String?,
    @SerializedName("unit")
    var unit: String?,
) {

    fun toEntity(): MorbitItem =
        MorbitItem(instrument, buyPrice, sellPrice, unit)
}
