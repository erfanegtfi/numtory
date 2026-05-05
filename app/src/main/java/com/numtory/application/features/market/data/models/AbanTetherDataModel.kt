package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.AbanTether
import com.google.gson.annotations.SerializedName

data class AbanTetherListDataModel(
    @SerializedName("data")
    val data: List<AbanTetherDataModel>
){
    fun toEntity(): List<AbanTether> =
        data.map { it.toEntity() }
}

data class AbanTetherDataModel constructor(
    @SerializedName("price_sell")
    var sell: String?,
    @SerializedName("price_buy")
    var buy: String? ,
    @SerializedName("symbol")
    var symbol: String?
) {

    fun toEntity(): AbanTether =
        AbanTether(sell, buy, symbol)
}