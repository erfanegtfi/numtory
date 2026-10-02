package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.EterexAssetsPrice


///////////////
data class EterexAssetsPriceDataModel(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("baseAsset")
    val baseAsset: String?,
    @SerializedName("quoteAsset")
    val quoteAsset: String?,
    @SerializedName("bestBuyPrice")
    val bestBuyPrice: Double?,
    @SerializedName("bestSellPrice")
    val bestSellPrice: Double?,
    @SerializedName("price_change_usdt")
    val priceChange: Double?,
    @SerializedName("price_change_usdt_percentage")
    val priceChangePercentage: Double?,
    @SerializedName("quote_volume")
    val volume: Double?,
) {
    fun toEntity(): EterexAssetsPrice =
        EterexAssetsPrice(
            symbol,
            name,
            baseAsset,
            quoteAsset,
            bestBuyPrice,
            bestSellPrice,
            priceChange,
            priceChangePercentage,
            volume,
        )
}
