package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.Tabdeal
import com.numtory.application.features.market.domain.entities.TabdealMarketItem
import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.TabdealSwap

data class TabdealMarketListDataModel(
    @SerializedName("currencies")
    val currencies: Map<String, Map<String, TabdealMarketItemDataModel>>?
) {
    fun toEntity(): Map<String, Map<String, TabdealMarketItem>>? =
        currencies?.mapValues { (_, innerMap) ->
            innerMap.mapValues { (_, item) ->
                item.toEntity()
            }
        }
}


data class TabdealMarketItemDataModel constructor(
    @SerializedName("price")
    var price: String?,

    ) {

    fun toEntity(): TabdealMarketItem =
        TabdealMarketItem(price)
}

data class TabdealSwapDataModel constructor(
    @SerializedName("from_amount_data")
    var fromAmountData: List<TabdealDataModel>?,
) {

    fun toEntity(): TabdealSwap =
        TabdealSwap(fromAmountData?.map { it.toEntity() })
}

data class TabdealDataModel constructor(
    @SerializedName("price_inverse")
    var priceInverse: String?,
    @SerializedName("price")
    var price: String?,
) {

    fun toEntity(): Tabdeal =
        Tabdeal(priceInverse, price)
}