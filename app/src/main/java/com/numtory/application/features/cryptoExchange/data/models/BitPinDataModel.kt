package com.numtory.application.features.cryptoExchange.data.models

import com.numtory.application.features.cryptoExchange.domain.entities.BitPin
import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.BitPinOTC
import com.numtory.application.features.cryptoExchange.domain.entities.BitPinPriceInfo


data class BitPinOTCDataModel constructor(
    @SerializedName("sell")
    var sell: String?,
    @SerializedName("buy")
    var buy: String?
) {

    fun toEntity(): BitPinOTC =
        BitPinOTC(sell, buy)
}

/////


data class BitPinDataModel constructor(
    @SerializedName("code")
    var code: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("price_info")
    var priceInfo: BitPinPriceInfoDataModel?,
) {

    fun toEntity(): BitPin =
        BitPin(id, code, priceInfo?.toEntity())
}


data class BitPinPriceInfoDataModel constructor(
    @SerializedName("price")
    var price: String?,
) {

    fun toEntity(): BitPinPriceInfo =
        BitPinPriceInfo(price)
}