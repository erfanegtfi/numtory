package com.numtory.application.features.cryptoExchange.domain.entities


data class EterexAssetsPrice(
    val symbol: String?,
    val name: String?,
    val baseAsset: String?,
    val quoteAsset: String?,
    val bestBuyPrice: Double?,
    val bestSellPrice: Double?,
    val priceChange: Double?,
    val priceChangePercentage: Double?,
    val volume: Double?,
) {
}