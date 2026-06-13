package com.numtory.application.features.cryptoMarket.domain.entities


data class CryptoMarketPrice constructor(
    var String: String?,
    var price: String?,
    var symbolUSDT: String?,
    var name: String?,
    var image: String?,
    var dayChangePercent: Float = 0f,
    var dayChangePrice: Float = 0f,
) {

    val symbol: String?
        get() = symbolUSDT?.replace("_USDT", "")

//    val name: String?
//        get() = cryptoMap[symbol]

}
