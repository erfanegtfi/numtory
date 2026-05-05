package com.numtory.application.features.market.domain.entities


data class ArzplusMarketList(
    val markets: List<ArzplusMarketItem>
) {

}

data class ArzplusMarketItem constructor(
    var priceUsdt: String?,
    var priceIrt: String?,
    var symbol: String?,
    var name: String?,
) {

}

///

data class ArzplusSwap constructor(
    var baseAsset: String?,
    var asset: String?,
    var price: String?,
) {

}