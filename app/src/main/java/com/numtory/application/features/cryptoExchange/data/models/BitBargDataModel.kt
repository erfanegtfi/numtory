package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.Bitbarg
import com.numtory.application.features.cryptoExchange.domain.entities.BitbargItem
import com.numtory.application.features.cryptoExchange.domain.entities.BitbargItems


data class BitbargDataModel constructor(
    @SerializedName("result")
    var result: BitbargItemsDataModel?,

    ) {

    fun toEntity(): Bitbarg =
        Bitbarg(result?.toEntity())
}

data class BitbargItemsDataModel constructor(
    @SerializedName("items")
    var items: List<BitbargItemDataModel>?,
) {

    fun toEntity(): BitbargItems =
        BitbargItems(items?.map { it.toEntity() })
}

data class BitbargItemDataModel constructor(
    @SerializedName("currency_price")
    var price: String?,
    @SerializedName("slug")
    var slug: String?,
) {

    fun toEntity(): BitbargItem =
        BitbargItem(price, slug)
}