package com.numtory.application.features.market.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.Arzypto
import com.numtory.application.features.market.domain.entities.ArzyptoPrice
import com.numtory.application.features.market.domain.entities.Ramzinex
import com.numtory.application.features.market.domain.entities.RamzinexPrice


data class ArzyptoDataModel constructor(
    @SerializedName("data")
    var data: ArzyptoPriceDataModel?,

    ) {

    fun toEntity(): Arzypto =
        Arzypto(data?.toEntity())
}

data class ArzyptoPriceDataModel constructor(
    @SerializedName("symbolTomanPrice")
    var symbolTomanPrice: String?,
) {

    fun toEntity(): ArzyptoPrice =
        ArzyptoPrice(symbolTomanPrice)
}