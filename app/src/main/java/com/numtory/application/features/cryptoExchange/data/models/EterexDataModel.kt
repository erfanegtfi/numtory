package com.numtory.application.features.cryptoExchange.data.models

import com.numtory.application.features.cryptoExchange.domain.entities.EterexGroups
import com.numtory.application.features.cryptoExchange.domain.entities.EterexPrice
import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.EterexAssetsPrice

data class EterexPriceGroupsDataModel(
    @SerializedName("priceGroups")
    val results: List<EterexGroupsDataModel>?
) {
    fun toEntity(): List<EterexGroups>? =
        results?.map { it.toEntity() }
}

data class EterexGroupsDataModel constructor(
    @SerializedName("name")
    var name: String?,
    @SerializedName("prices")
    var price: EterexPriceDataModel?,
    @SerializedName("coins")
    var coins: List<String>
) {

    fun toEntity(): EterexGroups =
        EterexGroups(name, price?.toEntity(), coins)
}

data class EterexPriceDataModel constructor(
    @SerializedName("usdtIrt")
    var usdtIrt: String?,
    @SerializedName("irtUsdt")
    var irtUsdt: String?,
) {

    fun toEntity(): EterexPrice =
        EterexPrice(usdtIrt, irtUsdt)
}

///////////////
data class EterexAssetsPriceDataModel(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("price")
    val price: String?
) {
    fun toEntity(): EterexAssetsPrice = EterexAssetsPrice(symbol, price)
}
