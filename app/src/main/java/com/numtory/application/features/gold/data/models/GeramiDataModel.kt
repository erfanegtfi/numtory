package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Gerami
import com.numtory.application.features.gold.domain.entities.GeramiPair

data class GeramiPairDataModel(
    @SerializedName("base_symbol")
    val baseSymbol: String?,
    @SerializedName("buy_price")
    val buyPrice: String?,
    @SerializedName("sell_price")
    val sellPrice: String?,
    @SerializedName("base_asset")
    val baseAsset: GeramiAssetDataModel?,
) {
    fun toEntity(): GeramiPair = GeramiPair(
        symbol = baseAsset?.symbol ?: baseSymbol,
        type = baseAsset?.type,
        sellPrice = sellPrice,
        buyPrice = buyPrice,
    )
}

data class GeramiAssetDataModel(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("type")
    val type: String?,
)

data class GeramiDataModel(
    val pairs: List<GeramiPairDataModel>?,
) {
    fun toEntity(): Gerami = Gerami(pairs?.map { it.toEntity() })
}
