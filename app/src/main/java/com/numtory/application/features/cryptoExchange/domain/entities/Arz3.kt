package com.numtory.application.features.cryptoExchange.domain.entities


data class Arz3coins(
    val coins: List<Arz3CoinItem>

) {

}

data class Arz3CoinItem constructor(
    var name: String?,
    var symbol: String?  ,
    var price: Arz3Price?
) {

}

data class Arz3Price constructor(
    var buy: String?,
    var sell: String?
) {

}