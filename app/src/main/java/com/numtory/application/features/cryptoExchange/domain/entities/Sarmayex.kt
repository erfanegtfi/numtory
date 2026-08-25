package com.numtory.application.features.cryptoExchange.domain.entities

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
    var symbol: String?,
    var sell: SarmayexCurrencyPrice?,
    var buy: SarmayexCurrencyPrice?
)

data class SarmayexCurrencyPrice constructor(
    var price: String?,
    )