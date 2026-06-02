package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.ArzplusMarketItem
import com.numtory.application.features.market.domain.entities.ArzplusSwap
import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.ExonyxItem

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

