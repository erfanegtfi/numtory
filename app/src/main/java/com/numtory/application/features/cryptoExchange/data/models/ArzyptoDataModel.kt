package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.Arzypto
import com.numtory.application.features.cryptoExchange.domain.entities.ArzyptoPrice


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