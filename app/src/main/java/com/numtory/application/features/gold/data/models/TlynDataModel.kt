package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Tlyn
import com.numtory.application.features.gold.domain.entities.TlynData
import com.numtory.application.features.gold.domain.entities.TlynPrice

data class TlynDataModel(
    @SerializedName("prices")
    val prices: List<List<TlynDataDataModel>?>?,

    ) {
    fun toEntity(): Tlyn = Tlyn(prices?.map {
        it?.map { ii->
            ii.toEntity()
        }
    })
}

data class TlynDataDataModel(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("price")
    val price: TlynPriceDataModel?,

    ) {
    fun toEntity(): TlynData = TlynData(symbol, price?.toEntity())
}

data class TlynPriceDataModel(
    @SerializedName("sell")
    val sell: Int,
    @SerializedName("buy")
    val buy: Int,
) {
    fun toEntity(): TlynPrice = TlynPrice(sell, buy, )
}