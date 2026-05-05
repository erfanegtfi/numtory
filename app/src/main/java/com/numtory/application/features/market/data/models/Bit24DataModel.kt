package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.Bit24
import com.numtory.application.features.market.domain.entities.Bit24MetaPrice
import com.numtory.application.features.market.domain.entities.Bit24Metas
import com.numtory.application.features.market.domain.entities.Bit24Price
import com.numtory.application.features.market.domain.entities.Bit24Swap
import com.google.gson.annotations.SerializedName

data class Bit24MarketListDataModel(
    @SerializedName("results")
    val results: List<Bit24DataModel>
) {
    fun toEntity(): List<Bit24> =
        results.map { it.toEntity() }
}

data class Bit24DataModel constructor(
    @SerializedName("name")
    var name: String?,
    @SerializedName("fa_name")
    var faName: String?,
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("markets")
    var market: Map<String, Bit24PriceMDataModel>?
) {

    fun toEntity(): Bit24 =
        Bit24(name, faName, symbol, market?.mapValues { (_, item) ->
            item.toEntity()
        })
}

data class Bit24PriceMDataModel constructor(
    @SerializedName("each_price")
    var price: String?,
) {

    fun toEntity(): Bit24Price =
        Bit24Price(price)
}
/////// swap

data class Bit24SwapDataModel(
    @SerializedName("data")
    val data: Bit24MetasDataModel
) {
    fun toEntity(): Bit24Swap = Bit24Swap(data.toEntity())
}

data class Bit24MetasDataModel(
    @SerializedName("metas")
    val metas: Bit24MetaPriceDataModel
) {
    fun toEntity(): Bit24Metas = Bit24Metas(metas.toEntity())
}

data class Bit24MetaPriceDataModel constructor(
    @SerializedName("sell_price")
    var sellPrice: String?,
    @SerializedName("buy_price")
    var buyPrice: String?,
) {

    fun toEntity(): Bit24MetaPrice =  Bit24MetaPrice(sellPrice, buyPrice)

}