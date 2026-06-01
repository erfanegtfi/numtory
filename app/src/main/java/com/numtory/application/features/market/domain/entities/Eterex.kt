package com.numtory.application.features.market.domain.entities

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.data.models.Bit24DataModel

data class EterexPriceGroups(
    val results: List<Bit24DataModel>
) {
}

data class EterexGroups constructor(
    var name: String?,
    var price: EterexPrice?,
    var coins: List<String>
) {

}

data class EterexPrice constructor(
    var usdtIrt: String?,
    var irtUsdt: String?,
) {

}
/////
data class EterexAssetsPrice(
    val symbol: String?,
    val price: String?
) {
}