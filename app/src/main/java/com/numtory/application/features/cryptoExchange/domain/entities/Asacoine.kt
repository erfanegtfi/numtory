package com.numtory.application.features.cryptoExchange.domain.entities


data class Asacoine(
    val pairs: List<AsacoinePair>?
) {
}

data class AsacoinePair constructor(
    /** Pair as the exchange spells it, e.g. "USDT-TMN". */
    var name: String?,
    var ask: String?,
    var bid: String?,
) {

}
