package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.ExonyxItem

data class ExonyxDataModel(
    @SerializedName("data")
    val markets: List<ExonyxItemDataModel>?
) {
    fun toEntity(): List<ExonyxItem>? =
        markets?.map { it.toEntity() }
}

data class ExonyxItemDataModel constructor(
    @SerializedName("ISO")
    var iso: String?,
    @SerializedName("price_buy")
    var priceBuy: String?,
    @SerializedName("price_sell")
    var priceSell: String?,

) {

    fun toEntity(): ExonyxItem =
        ExonyxItem(iso, priceBuy, priceSell)
}

