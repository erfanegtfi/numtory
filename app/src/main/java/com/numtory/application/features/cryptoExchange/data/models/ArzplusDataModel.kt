package com.numtory.application.features.cryptoExchange.data.models

import com.numtory.application.features.cryptoExchange.domain.entities.ArzplusMarketItem
import com.numtory.application.features.cryptoExchange.domain.entities.ArzplusSwap
import com.google.gson.annotations.SerializedName

data class ArzplusMarketListDataModel(
    @SerializedName("high_volume")
    val markets: List<ArzplusMarketItemDataModel>?
) {
    fun toEntity(): List<ArzplusMarketItem>? =
        markets?.map { it.toEntity() }
}

data class ArzplusMarketItemDataModel constructor(
    @SerializedName("price_usdt")
    var priceUsdt: String?,
    @SerializedName("price_irt")
    var priceIrt: String?,
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("name")
    var name: String?
) {

    fun toEntity(): ArzplusMarketItem =
        ArzplusMarketItem(priceUsdt, priceIrt, symbol, name)
}

//////
data class ArzplusSwapDataModel constructor(
    @SerializedName("base_asset")
    var baseAsset: String?,
    @SerializedName("asset")
    var asset: String?,
    @SerializedName("price")
    var price: String?,
) {

    fun toEntity(): ArzplusSwap =
        ArzplusSwap(baseAsset, asset, price)
}