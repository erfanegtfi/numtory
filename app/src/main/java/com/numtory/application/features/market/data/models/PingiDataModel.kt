package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.PingiItem
import com.google.gson.annotations.SerializedName

data class PingiDataModel(
    @SerializedName("data")
    val stats: Map<String, PingiItemDataModel>
) {
    fun toEntity(): Map<String, PingiItem> =
        stats.mapValues { (_, item) ->
            item.toEntity()
        }
}

data class PingiItemDataModel constructor(
    @SerializedName("currentPrice")
    var price: String?,
    @SerializedName("market")
    var market: String?,
) {

    fun toEntity(): PingiItem =
        PingiItem(price, market)
}