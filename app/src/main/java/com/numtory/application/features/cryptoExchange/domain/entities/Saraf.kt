package com.numtory.application.features.cryptoExchange.domain.entities

data class SarafPrice(
    val price: SarafItems?
) {
}
data class SarafItems(
    val items: Map<String, Saraf>
) {

}

data class Saraf constructor(
    var price: String?,
    var symbol: String?
) {

}

