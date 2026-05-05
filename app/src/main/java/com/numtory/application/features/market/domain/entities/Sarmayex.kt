package com.numtory.application.features.market.domain.entities

data class SarmayexMarketList(
    val stats: Map<String, SarmayexMarketItem>
)

data class SarmayexMarketItem constructor(
    var price: String?,
    var base: String?,
    var quote: String?
)

//
data class SarmayexSwap(
    var currency: SarmayexCurrency?,
)


data class SarmayexCurrency constructor(
    var sell: SarmayexCurrencyPrice?,
    var buy: SarmayexCurrencyPrice?
)

data class SarmayexCurrencyPrice constructor(
    var price: String?,
    )