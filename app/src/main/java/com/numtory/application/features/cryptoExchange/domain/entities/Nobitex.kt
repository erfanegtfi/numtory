package com.numtory.application.features.cryptoExchange.domain.entities

data class NobitexMarket constructor(
    var price: String?,
    var symbol: String?
) {

}


data class NobitexResult(
    var result: Nobitex?,
){

}
data class Nobitex constructor(
    var sell: String?,
    var buy: String?,
) {


}