package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.BitWana
import com.numtory.application.features.cryptoExchange.domain.entities.BitWanaContent
import com.numtory.application.features.cryptoExchange.domain.entities.Bitbarg
import com.numtory.application.features.cryptoExchange.domain.entities.BitbargItem
import com.numtory.application.features.cryptoExchange.domain.entities.BitbargItems


data class BitWanaDataModel constructor(
    @SerializedName("content")
    var content: BitWanaContentDataModel?,

    ) {

    fun toEntity(): BitWana =
        BitWana(content?.toEntity())
}

data class BitWanaContentDataModel constructor(
    @SerializedName("buyPrice")
    var buyPrice: String?,
    @SerializedName("sellPrice")
    var sellPrice: String?,
) {

    fun toEntity(): BitWanaContent =
        BitWanaContent(buyPrice, sellPrice)
}