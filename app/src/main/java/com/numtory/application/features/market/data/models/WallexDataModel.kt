package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.WallexMarketItem
import com.numtory.application.features.market.domain.entities.WallexMarkets
import com.numtory.application.features.market.domain.entities.WallexQuotePrice
import com.google.gson.annotations.SerializedName
import kotlin.collections.Map

data class WallexResultDataModel(
    @SerializedName("result")
    val results: WallexMarketsDataModel
) {
    fun toEntity(): WallexMarkets =
        WallexMarkets(results.toEntity())
}

data class WallexMarketsDataModel constructor(
    @SerializedName("markets")
    val results: List<WallexMarketItemDataModel>
) {
    fun toEntity(): List<WallexMarketItem> =
        results.map { it.toEntity() }
}

data class WallexMarketItemDataModel constructor(
    @SerializedName("baseAsset")
    val baseAsset: String,
    @SerializedName("quotes")
    val quotes: Map<String, WallexQuotePriceDataModel>
) {

    fun toEntity(): WallexMarketItem =
        WallexMarketItem(
            baseAsset,
            quotes.mapValues { (_, item) ->
                item.toEntity()
            })
}

data class WallexQuotePriceDataModel constructor(
    @SerializedName("price")
    var price: String?,

    ) {

    fun toEntity(): WallexQuotePrice =
        WallexQuotePrice(price)
}


