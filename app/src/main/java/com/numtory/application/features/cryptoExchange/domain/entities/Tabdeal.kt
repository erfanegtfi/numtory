package com.numtory.application.features.cryptoExchange.domain.entities


data class TabdealSwap constructor(
    var fromAmountData: List<Tabdeal>?,
) {

}
data class Tabdeal constructor(
    var buyPrice: String?,
    var sellPrice: String?,
)


data class TabdealMarketItem constructor(
    var price: String?,
    )