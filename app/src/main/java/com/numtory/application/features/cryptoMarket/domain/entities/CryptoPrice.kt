package com.numtory.application.features.cryptoMarket.domain.entities


data class CryptoMarketPrice constructor(
    var persianName: String?,
    var price: String?,
    var symbol: String?,
    var name: String?,
    var image: String?,
    var dayChangePercent: Float = 0f,
    var dayChangePrice: Float = 0f,
    var labels: List<List<String>>?,
) {



}

data class EterexAssetLabel constructor(
    var id: String?,
    var name: String?,
    var persianName: String?,
    var fullName: String?,
    var labels: List<List<String>>?,
) {


}