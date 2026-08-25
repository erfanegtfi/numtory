package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.Tlyn
import com.numtory.application.features.goldExchange.domain.entities.TlynData
import com.numtory.application.features.goldExchange.domain.entities.TlynPrice

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
    val sell: Long,
    @SerializedName("buy")
    val buy: Long,
) {
    fun toEntity(): TlynPrice = TlynPrice(sell, buy, )
}