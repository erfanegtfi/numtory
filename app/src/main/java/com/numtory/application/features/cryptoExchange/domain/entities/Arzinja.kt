package com.numtory.application.features.cryptoExchange.domain.entities


data class ArzinjaItem constructor(
    var pair: String?,
    var baseAsset: String?,
    var quoteAsset: String?,
    var stats: ArzinjaState?,
) {
}

data class ArzinjaState constructor(
    var lastPrice: String?,
    var bidPrice: String?,
    var askPrice: String?,
) {

}