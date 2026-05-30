package com.numtory.application.features.cryptoMarket.domain.entities


data class CryptoMarketPrice constructor(
    var price: String?,
    var symbolUSDT: String?,
    var dayChange: Float = 0f,
) {

    val symbol: String?
        get() = symbolUSDT?.replace("_USDT", "")

    val name: String?
        get() = cryptoMap[symbol]

}
