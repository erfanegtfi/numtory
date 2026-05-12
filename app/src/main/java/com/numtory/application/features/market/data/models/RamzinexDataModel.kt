package com.numtory.application.features.market.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.Ramzinex
import com.numtory.application.features.market.domain.entities.RamzinexPrice


data class RamzinexDataModel constructor(
    @SerializedName("data")
    var data: RamzinexPriceDataModel?,

    ) {

    fun toEntity(): Ramzinex =
        Ramzinex(data?.toEntity())
}

data class RamzinexPriceDataModel constructor(
    @SerializedName("from_amount")
    var fromAmount: String?,
    @SerializedName("to_amount")
    var toAmount: String?
) {

    fun toEntity(): RamzinexPrice =
        RamzinexPrice(fromAmount, toAmount)
}