package com.numtory.application.features.market.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.Saraf
import com.numtory.application.features.market.domain.entities.SarafItems
import com.numtory.application.features.market.domain.entities.SarafPrice

data class SarafPriceDataModel(
    @SerializedName("price")
    val price: SarafItemsDataModel?
) {
    fun toEntity(): SarafPrice =
        SarafPrice(price?.toEntity())
}

data class SarafItemsDataModel(
    @SerializedName("Items")
    val items: Map<String, SarafDataModel>
) {
    fun toEntity() = SarafItems(
        items.mapValues { (_, item) ->
            item.toEntity()
        })
}

data class SarafDataModel constructor(
    @SerializedName("p")
    var price: String?,
    @SerializedName("s")
    var symbol: String?
) {

    fun toEntity(): Saraf =
        Saraf(price, symbol)
}

